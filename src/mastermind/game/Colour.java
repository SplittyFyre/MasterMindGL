package mastermind.game;
import org.lwjgl.util.vector.Vector4f;

public enum Colour {
	
	RED 	(new Vector4f(1, 0, 0, 1)),
	GREEN 	(new Vector4f(0, 1, 0, 1)),
	BLUE 	(new Vector4f(0, 0, 1, 1)),
	YELLOW	(new Vector4f(1, 1, 0, 1)),
	BROWN	(new Vector4f(115 / 255f, 77 / 255f, 38 / 255f, 1)),
	ORANGE	(new Vector4f(1, 0.5f, 0, 1)),
	BLACK	(new Vector4f(0.05f, 0.05f, 0.05f, 1)),
	WHITE	(new Vector4f(1, 1, 1, 1)),
	UNKNOWN (new Vector4f(0.3f, 0.3f, 0.3f, 1));

	public final Vector4f col;
	
	private Colour(Vector4f col) {
		this.col = col;
	}
	
}
