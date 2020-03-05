package engine.scene.entities.camera;

import org.lwjgl.glfw.GLFW;

import engine.renderEngine.TRDisplayManager;
import engine.utils.TRKeyboard;

public class RogueCamera extends TRCamera {
	
	private static float SPEED = 1000;
	private static float UPSPEED = 500;

	@Override
	public void move() {
		
		float mov = 0;
		float up = 0;
		
		if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			mov = SPEED;
		}
		else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			mov = -SPEED;
		}
		
		if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
			yaw -= 90f * TRDisplayManager.getFrameDeltaTime();
		}
		else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			yaw += 90f * TRDisplayManager.getFrameDeltaTime();
		}
		
		if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			up = UPSPEED;
		}
		else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			up = -UPSPEED;
		}
		
		if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_K)) {
			pitch -= 45f * TRDisplayManager.getFrameDeltaTime();
		}
		else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_J)) {
			pitch += 45f * TRDisplayManager.getFrameDeltaTime();
		}
		
		float distanceMoved = mov * TRDisplayManager.getFrameDeltaTime();
		
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(180 - yaw)));
		float dy = up * TRDisplayManager.getFrameDeltaTime();
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(180 - yaw)));
		
		position.x += dx;
		position.y += dy;
		position.z += dz;
		
		
		
	}

}
