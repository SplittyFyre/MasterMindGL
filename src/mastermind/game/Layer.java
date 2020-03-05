package mastermind.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.objStuff.OBJParser;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.renderEngine.textures.SCTexture;
import engine.scene.TRScene;
import engine.scene.entities.StaticEntity;
import engine.utils.TRMouse;
import engine.utils.TRRayCaster;

public class Layer {
	
	static RawModel rawBody = OBJParser.loadObjModelWProperTexSeams("cube");
	
	private static final float layerSpacing = 50.0f;
	private static final float shapeSpacing = 60.0f;
	
	private Colour[] colours = {Colour.UNKNOWN, Colour.UNKNOWN, Colour.UNKNOWN, Colour.UNKNOWN};
	private StaticEntity[] shapes;
	
	public Layer(int lidx, TRScene scene) {
		shapes = new StaticEntity[4];
		for (int i = 0; i < 4; i++) {
			ModelTexture newtex = new SCTexture(Colour.UNKNOWN.col);
			newtex.setReflectivity(5);
			newtex.setShineDamper(25);
			
			shapes[i] = new StaticEntity(new TexturedModel(rawBody, newtex), new Vector3f(i * shapeSpacing, lidx * layerSpacing, 0), 0, 0, 0, 10f);
			scene.addEntityToRoot(shapes[i]);
		}
	}
	
	public Layer() {
		
	}
	
	public void setColour(int i, Colour col) {
		if (i < 0 || i > 3) {
			throw new IndexOutOfBoundsException();
		}
		colours[i] = col;
		((SCTexture) shapes[i].getModel().getTexture()).setValue(col.col);
	}
	
	public void update() {
		for (int i = 0; i < 4; i++) {
			StaticEntity el = this.shapes[i];
			el.rotate(0, -30 * TRDisplayManager.getFrameDeltaTime(), 0);
		}
	}
	
	public void update(TRRayCaster rc, Game game) {
		boolean flag = false;
		for (int i = 0; i < 4; i++) {
			StaticEntity el = this.shapes[i];
			el.rotate(0, -30 * TRDisplayManager.getFrameDeltaTime(), 0);
			
			if (TRMouse.isMouseButtonDown(TRMouse.LEFT)) {
				// if the cursor is hovering over this specific shape
				if (rc.penetrates(el.getPosition(), el.getModel().getRawModel().getBoundingBox().sphereRadius * el.getScale().x)) {
					game.changeSelect(el, i);
					flag = true;
				}
			}
			
		}
		// if player clicked without the cursor pointing at a shape
		/*if (TRMouse.isMouseButtonDown(TRMouse.LEFT) &! flag) {
			game.changeSelect(null, -1);
		}*/
	}
	
	
	public static Layer getRandom() {
		Layer retval = new Layer();
		
		List<Colour> possibles = Arrays.asList(Colour.values());
		Collections.shuffle(possibles);
				
		for (int i = 0; i < 4; i++) {
			retval.colours[i] = possibles.get(i);
		}
		
		return retval;
	}


	public boolean equals(Layer that) {
		return Arrays.equals(this.colours, that.colours);
	}
	
	@Override
	public String toString() {
		String ret = "";
		for (Colour col : colours) {
			ret += col.toString();
			ret += " ";
		}
		return ret;
	}

	public boolean isComplete() {
		for (Colour col : colours) {
			if (col == Colour.UNKNOWN) {
				return false;
			}
		}
		return true;
	}
	
}
