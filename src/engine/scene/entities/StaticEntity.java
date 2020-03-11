package engine.scene.entities;

import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.models.TexturedModel;

public class StaticEntity extends TREntity {
	
	private Vector3f initialPosition;

	public StaticEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.initialPosition = position;
	}
	
	public StaticEntity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, index, position, rotX, rotY, rotZ, scale);
	}

	public void offset(float y) {
		super.setPosition(new Vector3f(this.initialPosition.x, this.initialPosition.y + y, this.initialPosition.z));
	}

	public void unoffset() {
		super.setPosition(this.initialPosition);
	}
	
	public Vector3f getInitialPosition() {
		return initialPosition;
	}
	
}
