package engine.utils;

import org.lwjgl.glfw.GLFW;

import engine.renderEngine.TRDisplayManager;

public class TRKeyboard {
	
	public static boolean isKeyDown(int glfwkey) {
		return GLFW.glfwGetKey(TRDisplayManager.getWindowID(), glfwkey) == GLFW.GLFW_PRESS;
	}

}
