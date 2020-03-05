package engine.utils;

import org.lwjgl.glfw.GLFW;

import engine.renderEngine.TRDisplayManager;

public class TRMouse {
	
	public static final int LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT, RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT, MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	
	public static boolean isMouseButtonDown(int button) {
		return GLFW.glfwGetMouseButton(TRDisplayManager.getWindowID(), button) == GLFW.GLFW_PRESS;
	}

}
