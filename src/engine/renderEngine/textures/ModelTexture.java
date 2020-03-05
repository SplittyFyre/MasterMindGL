package engine.renderEngine.textures;

public abstract class ModelTexture {
	
	public static enum Types {
		UV, SC
	}
	
	private int specularMap;
	
	public final Types type;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean transparent = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;
	private float brightDamper = 0;
	
	public ModelTexture(Types type) {
		this.type = type;
	}
	
	public void setBrightDamper(float f) {
		this.brightDamper = f;
	}
	
	public float getBrightDamper() {
		return this.brightDamper;
	}
	
	public void setSpecularMap(int specMap) {
		this.specularMap = specMap;
		this.hasSpecularMap = true;
	}
	
	public boolean hasSpecularMap() {
		return hasSpecularMap;
	}
	
	public int getSpecularMap() {
		return specularMap;
	}
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
}
