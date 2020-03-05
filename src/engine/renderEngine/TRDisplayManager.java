package engine.renderEngine;



import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

public class TRDisplayManager {
	
	private static String[] ICON_PATHS = {/*"sficon16", "sficon32",*/ "sficon"};
	
	private static int FPS_CAP;
	
	private static long windowID;
	
	private static long lastFrameTime;
	private static float delta;
	
	private static Runnable resizeCallBack;
	
	private static GLFWCursorPosCallback cursorPosCallback;
	private static GLFWWindowSizeCallback windowSizeCallback;
	private static GLFWScrollCallback scrollCallback;
	
	public static class Mouse {
		private static int x, y;
		private static int px, py;
		private static int dx, dy;
		private static int dwheel;
		// we don't want accidental changing of these values
		public static int getX() {
			return x;
		}
		public static int getY() {
			return y;
		}
		
		public static int getDX() {
			return dx;
		}
		public static int getDY() {
			return dy;
		}
		
		public static int getDWheel() {
			return dwheel;
		}
	}
	
	private static int width, height;
	public static int getWidth() {
		return width;
	}
	public static int getHeight() {
		return height;
	}
	
	public static long getWindowID() {
		return windowID;
	}
	
	public static void createDisplay(int width, int height, int fpsCap, String title, Runnable resizeCallBack) {
		
		TRDisplayManager.width = width;
		TRDisplayManager.height = height;
		
		GLFW.glfwInit();
		
		GLFW.glfwDefaultWindowHints();
		//GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, GL11.GL_TRUE);
		
		windowID = GLFW.glfwCreateWindow(width, height, title, 0, 0);	
		
		GLFW.glfwMakeContextCurrent(windowID);
		GL.createCapabilities();
		
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(windowID);
		
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		
		GL11.glViewport(0, 0, width, height);
		lastFrameTime = getCurrentTime();

		
		setupCallbacks();
	}
	
	
	
	private static void setupCallbacks() {
		cursorPosCallback = new GLFWCursorPosCallback() {
			
			@Override
			public void invoke(long windowID, double x, double y) {
				Mouse.x = (int) x;
				Mouse.y = height - (int) y;
			}
		};
		GLFW.glfwSetCursorPosCallback(windowID, cursorPosCallback);
		
		
		windowSizeCallback = new GLFWWindowSizeCallback() {
			
			@Override
			public void invoke(long windowID, int width, int height) {
				System.out.printf("Window resized from %d, %d to %d, %d\n", TRDisplayManager.width, TRDisplayManager.height, width, height);
				TRDisplayManager.width = width;
				TRDisplayManager.height = height;
			}
		};
		GLFW.glfwSetWindowSizeCallback(windowID, windowSizeCallback);
		
		
		scrollCallback = new GLFWScrollCallback() {
			
			@Override
			public void invoke(long windowID, double x, double y) {
				Mouse.dwheel = (int) y * 10;
			}
		};
		GLFW.glfwSetScrollCallback(windowID, scrollCallback);
	}
	
	
	
	public static boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(windowID);
	}
	
	public static void updateDisplay() {
		GLFW.glfwSwapBuffers(windowID);
		GLFW.glfwPollEvents();
		
		Mouse.dx = Mouse.px - Mouse.x;
		Mouse.dy = Mouse.py - Mouse.y;
		
		Mouse.px = Mouse.x;
		Mouse.py = Mouse.y;
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
		
	}
	
	public static float getFrameDeltaTime() {
		return delta;
	}
	
	public static void closeDisplay() {
		GLFW.glfwHideWindow(windowID);
		GLFW.glfwDestroyWindow(windowID);
	}
	
	public static long getCurrentTime() {
		return (long) (GLFW.glfwGetTime() * 1000);
	}
	
	public static Vector2f getNormalizedMouseCoords() {
		return new Vector2f(-1.0f + 2.0f * Mouse.getX() / getWidth()
				, 1.0f - 2.0f * Mouse.getY() / getHeight());
	}
	
	public static float getAspectRatio() {
		//System.out.println((float) Display.getWidth() / (float) Display.getHeight());
		return (float) getWidth()/ (float) getHeight();
		//return (float) Display.getWidth() / (float) Display.getHeight();
	}
	
}
