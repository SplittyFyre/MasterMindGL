package engine.renderEngine.textures;

import org.lwjgl.util.vector.Vector4f;

public class SCTexture extends ModelTexture {
	
	private Vector4f value;

	public Vector4f getValue() {
		return value;
	}

	public void setValue(Vector4f val) {
		this.value = val;
	}
	
	public SCTexture(Vector4f value) {
		super(ModelTexture.Types.SC);
		this.value = value;
	}

}
