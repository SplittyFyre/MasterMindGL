package mastermind.game;

import org.lwjgl.glfw.GLFW;

import engine.renderEngine.TRDisplayManager;
import engine.scene.entities.camera.TRCamera;
import engine.utils.TRKeyboard;

public class MasterMindCamera extends TRCamera {
	
	private static float SPEED = 1000;
	private static float SIDESPEED = 500;
	private static float UPSPEED = 500;

	@Override
	public void move() {
		
		float mov = 0;
		float sidemov = 0;
		float up = 0;
		
		if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			mov = -SPEED;
		}
		else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			mov = SPEED;
		}
		
		if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
			sidemov = -SIDESPEED;
		}
		else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			sidemov = SIDESPEED;
		}
		
		if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			up = UPSPEED;
		}
		else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			up = -UPSPEED;
		}
		
		float dx = sidemov * TRDisplayManager.getFrameDeltaTime();
		float dy = up * TRDisplayManager.getFrameDeltaTime();
		float dz = mov * TRDisplayManager.getFrameDeltaTime();
		
		position.x += dx;
		position.y += dy;
		position.z += dz;
		
		
		
	}

}
