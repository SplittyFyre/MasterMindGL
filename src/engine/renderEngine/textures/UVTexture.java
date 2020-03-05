package engine.renderEngine.textures;

public class UVTexture extends ModelTexture {
	
	private int textureID;
	private int numRows = 1;
	
	public UVTexture(int texture) {
		super(ModelTexture.Types.UV);
		this.textureID = texture;
	}
	
	public int getID() {
		return textureID;
	}
	

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}


}
