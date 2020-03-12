package mastermind.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engine.objStuff.OBJParser;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.renderEngine.textures.SCTexture;
import engine.scene.entities.StaticEntity;
import engine.scene.entities.TROrganizationNode;
import engine.utils.TRMouse;
import engine.utils.TRRayCaster;

public class Layer {
	
	public static RawModel rawBody = OBJParser.loadObjModelWProperTexSeams("sphere");
	
	public static final float layerSpacing = 50.0f;
	public static final float shapeSpacing = 60.0f;
	
	private Colour[] colours = {Colour.UNKNOWN, Colour.UNKNOWN, Colour.UNKNOWN, Colour.UNKNOWN};
	private StaticEntity[] shapes;
	
	public Layer(int lidx, TROrganizationNode entityNode) {
		shapes = new StaticEntity[4];
		for (int i = 0; i < 4; i++) {
			ModelTexture newtex = new SCTexture(Colour.UNKNOWN.col);
			newtex.setReflectivity(0);
			newtex.setShineDamper(10);
			
			shapes[i] = new StaticEntity(new TexturedModel(rawBody, newtex), new Vector3f(i * shapeSpacing, lidx * layerSpacing, 0), 0, 0, 0, 10f);
			if (entityNode != null)
				entityNode.attachChild(shapes[i]);
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
	
	public StaticEntity[] getShapes() {
		return this.shapes;
	}
	
	public void update() {
		for (int i = 0; i < 4; i++) {
			StaticEntity el = this.shapes[i];
			el.rotate(0, -30 * TRDisplayManager.getFrameDeltaTime(), 0);
		}
	}
	
	public void update(TRRayCaster rc, Game game) {
		for (int i = 0; i < 4; i++) {
			StaticEntity el = this.shapes[i];
			el.rotate(0, -30 * TRDisplayManager.getFrameDeltaTime(), 0);
			
			if (TRMouse.isMouseButtonDown(TRMouse.LEFT)) {
				// if the cursor is hovering over this specific shape
				if (rc.penetrates(el.getPosition(), el.getModel().getRawModel().getBoundingBox().sphereRadius * el.getScale().x)) {
					game.changeSelect(el, i);
				}
			}
			
		}
	}
	
	
	public static Layer getRandomAKACorrect(TROrganizationNode entityNode) {
		Layer retval = new Layer();
		
		List<Colour> possibles = new LinkedList<Colour>(Arrays.asList(Colour.values()));
		possibles.remove(Colour.UNKNOWN);
		Collections.shuffle(possibles);
				
		for (int i = 0; i < 4; i++) {
			retval.colours[i] = possibles.get(i);
		}
		
		retval.shapes = new StaticEntity[4];
		for (int i = 0; i < 4; i++) {
			ModelTexture newtex = new SCTexture(possibles.get(i).col);
			newtex.setReflectivity(0);
			newtex.setShineDamper(10);
			
			retval.getShapes()[i] = new StaticEntity(new TexturedModel(rawBody, newtex), new Vector3f(i * shapeSpacing, 0, 150), 0, 0, 0, 10f);
			if (entityNode != null)
				entityNode.attachChild(retval.getShapes()[i]);
		}
		
		System.out.println(retval.toString());
		
		return retval;
	}


	public boolean equals(Layer that) {
		return Arrays.equals(this.colours, that.colours);
	}
	
	public int[] computeXandO(Layer that) {
		
		// x is correct colour and position
		// o is correct colour only
		
		int x = 0;
		
		HashSet<Integer> perfectMatches = new HashSet<Integer>();
		for (int i = 0; i < 4; i++) {
			if (this.colours[i] == that.colours[i]) {
				x++;
				perfectMatches.add(i);
			}
		}
		
		int o = 0;
		
		for (int i = 0; i < 4; i++) {
			if (!perfectMatches.contains(i)) {
				for (int j = 0; j < 4; j++) {
					if (!perfectMatches.contains(j)) {
						if (this.colours[i] == that.colours[j]) {
							o++;
						}
					}
				}
			}
		}
		
		
		return new int[] {x, o};
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
	
	public void reset() {
		for (int i = 0; i < 4; i++) {
			this.setColour(i, Colour.UNKNOWN);
		}
	}
	
}
