package mastermind.game;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.fontMeshCreator.GUIText;
import engine.renderEngine.Loader;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.guis.GUIStruct;
import engine.renderEngine.guis.GUITexture;
import engine.renderEngine.guis.IButton;
import engine.renderEngine.guis.SFAbstractButton;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.renderEngine.textures.SCTexture;
import engine.scene.TRScene;
import engine.scene.entities.StaticEntity;
import engine.scene.entities.TREntity;
import engine.scene.entities.TROrganizationNode;
import engine.utils.TRKeyboard;
import engine.utils.TRMath;
import engine.utils.TRRayCaster;
import engine.utils.TRUtils;

public class Game {
		
	private Layer[] layers;
	private TRScene scene;
	private Layer correctLayer;
	
	private boolean selectChanged = false;
	private StaticEntity selected = null;
	private StaticEntity newSelect = null;
	private int selectIndex = -1;
	
	private boolean arrowFlag = true;
	
	private boolean won = false, lost = false;
	
	private int activeLayer = 0;
	
	private float t = 0;
	
	private TRRayCaster rc;
	
	private List<GUITexture> guis;
	
	private TROrganizationNode entityNode;
	private TROrganizationNode hiddenNode;
	
	public Game(TRScene scene, Matrix4f pmat, List<GUITexture> guis) {
		this.scene = scene;
		layers = new Layer[10];
		
		entityNode = new TROrganizationNode();
		hiddenNode = new TROrganizationNode();
		
		for (int i = 0; i < 10; i++) {
			layers[i] = new Layer(i, entityNode);
		}
		this.correctLayer = Layer.getRandomAKACorrect(hiddenNode);
				
		rc = new TRRayCaster(scene.getCamera(), pmat);
		this.guis = guis;
		initGUIS();
		
		scene.addEntityToRoot(entityNode);
	}
	
	public void update() {
		rc.update();
		updateGUIS();
		
		if (this.won || this.lost) {
			if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_TAB)) {
				this.reset();
				return;
			}
		}
		
		for (int i = 0; i < 10; i++) {
			Layer layer = layers[i];
			if (i == activeLayer) {
				layer.update(rc, this);
			}
			else {
				layer.update();
			}
		}
		
		
		for (TREntity el : entityNode.getChildren()) {
			el.rotate(0, -30 * TRDisplayManager.getFrameDeltaTime(), 0);
		}
		
		
		if (selectChanged) {
			if (this.selected != null) {
				this.selected.unoffset();
			}
			this.selected = this.newSelect;
			selectChanged = false;
			t = 0;
		}
		
		if (this.selected != null) {
			
			if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_LEFT) || TRKeyboard.isKeyDown(GLFW.GLFW_KEY_Q)) {
				if (arrowFlag) {
					arrowFlag = false;
					
					int targetIndex = selectIndex - 1;
					if (targetIndex == -1) {
						targetIndex = 3;
					}
					
					this.changeSelect(layers[activeLayer].getShapes()[targetIndex], targetIndex);
				}
			}
			else if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT) || TRKeyboard.isKeyDown(GLFW.GLFW_KEY_E)) {
				if (arrowFlag) {
					arrowFlag = false;
					
					int targetIndex = selectIndex + 1;
					if (targetIndex == 4) {
						targetIndex = 0;
					}
					
					this.changeSelect(layers[activeLayer].getShapes()[targetIndex], targetIndex);
				}
			}
			else {
				arrowFlag = true;
			}
			
			t += TRDisplayManager.getFrameDeltaTime() * 3;
			this.selected.offset(10 * (float) Math.sin(t));
		}
		
	}
	
	public StaticEntity getSelected() {
		return this.selected;
	}
	
	public void changeSelect(StaticEntity obj, int index) {
		if (this.selected != obj) {
			selectChanged = true;
			this.newSelect = obj;
			this.selectIndex = index;
		}
	}
	
	private void enterColour(Colour col) {
		if (this.selected == null) {
			throw new RuntimeException("why is nothing selected?");
		}
		((SCTexture) this.selected.getModel().getTexture()).setValue(col.col);
		layers[activeLayer].setColour(selectIndex, col);
	}
	
	
	private void verifyLayer() {
		
		this.changeSelect(null, -1);
		
		Layer currentLayer = this.layers[activeLayer];
		
		if (this.correctLayer.equals(currentLayer)) {
			this.win();
		}
		else {
			
			int[] vals = currentLayer.computeXandO(correctLayer);
			int x = vals[0];
			int o = vals[1];
			
			
			// add entities to show x and o
			// x is red, o is white
			
			int j = 0;
			Vector3f lastShapePos = ((StaticEntity) currentLayer.getShapes()[3]).getInitialPosition();
			for (int i = 0; i < x; i++) {
				ModelTexture newtex = new SCTexture(Colour.RED.col);
				newtex.setReflectivity(0);
				newtex.setShineDamper(10);
				
				entityNode.attachChild(new StaticEntity(new TexturedModel(Layer.rawBody, newtex), new Vector3f(lastShapePos.x + Layer.shapeSpacing * 2 + (j * 30.f), lastShapePos.y, 0), 0, 0, 0, 5));
				j++;
			}
			for (int i = 0; i < o; i++) {
				ModelTexture newtex = new SCTexture(Colour.WHITE.col);
				newtex.setReflectivity(0);
				newtex.setShineDamper(10);
				
				entityNode.attachChild(new StaticEntity(new TexturedModel(Layer.rawBody, newtex), new Vector3f(lastShapePos.x + Layer.shapeSpacing * 2 + (j * 30.f), lastShapePos.y, 0), 0, 0, 0, 5));
				j++;
			}
			
			if (this.activeLayer == 9) { // this was their final chance
				this.lose();
			}
			else {
				activeLayer++;
			}
		}
	}
	
	private void win() {
		this.won = true;
		this.scene.addEntityToRoot(this.hiddenNode);
	}
	private void lose() {
		this.lost = true;
		this.scene.addEntityToRoot(this.hiddenNode);
	}
	
	private void reset() {
		this.won = false;
		this.lost = false;
		for (Layer layer : this.layers) {
			layer.reset();
		}
		entityNode.detachAll();
		
		this.newSelect = null;
		this.selected = null;
		this.activeLayer = 0;
		
		for (int i = 0; i < 10; i++) {
			layers[i] = new Layer(i, this.entityNode);
		}
		
		this.scene.rootNode.detachChild(this.hiddenNode);
		this.hiddenNode = new TROrganizationNode();
		
		this.correctLayer = Layer.getRandomAKACorrect(hiddenNode);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private GUIStruct panel;
	private GUITexture background;
	private SFAbstractButton submit;
	
	private GUIText message, info;
	
	private void initGUIS() {
		
		panel = new GUIStruct(new Vector2f(0.8f, -0.8f));
		background = new GUITexture(Loader.loadTexture("panel"), new Vector2f(0, -0.2f), TRMath.sqrgui(0.3f));
		panel.addChild(background);
	
		
		SFAbstractButton redbutton = new SFAbstractButton(panel, "red", new Vector2f(-0.25f, 0.25f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.RED);
			}
		};
		
		SFAbstractButton greenbutton = new SFAbstractButton(panel, "green", new Vector2f(-0.15f, 0.25f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.GREEN);
			}
		};
		
		SFAbstractButton bluebutton = new SFAbstractButton(panel, "blue", new Vector2f(-0.05f, 0.25f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.BLUE);
			}
		};
		
		SFAbstractButton yellowbutton = new SFAbstractButton(panel, "yellow", new Vector2f(0.05f, 0.25f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.YELLOW);
			}
		};
		
		SFAbstractButton brownbutton = new SFAbstractButton(panel, "brown", new Vector2f(-0.25f, 0.125f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.BROWN);
			}
		};
		
		SFAbstractButton orangebutton = new SFAbstractButton(panel, "orange", new Vector2f(-0.15f, 0.125f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.ORANGE);
			}
		};
		
		SFAbstractButton blackbutton = new SFAbstractButton(panel, "black", new Vector2f(-0.05f, 0.125f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.BLACK);
			}
		};
		
		SFAbstractButton whitebutton = new SFAbstractButton(panel, "white", new Vector2f(0.05f, 0.125f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				enterColour(Colour.WHITE);
			}
		};
		
		
		
		submit = new SFAbstractButton(panel, "checkmark", new Vector2f(-0.1f, -0.05f), TRMath.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
				
			}
			
			@Override
			public void onClick(IButton button) {
				verifyLayer();
				
			}
		};
		
		message = new GUIText("this shouldnt be here", 2f, TRUtils.trFont, TRMath.coordtext(0.525f, -0.525f), 0.5f, false);		
		message.hide();
		info = new GUIText("Press TAB to play again", 1.3f, TRUtils.trFont, TRMath.coordtext(0.525f, -0.625f), 0.5f, false);		
		info.hide();
	}
	
	private void updateGUIS() {
		
		// not worth it to use a switch case
		if (this.won) {
			panel.hide(guis);
			background.show(guis);
			message.setColour(0, 0.9f, 0.3f);
			info.setColour(0, 0.9f, 0.3f);
			message.setText("YOU WIN!!!");
			message.show();
			info.show();
		}
		else if (this.lost) {
			panel.hide(guis);
			background.show(guis);
			message.setColour(1, 0.1f, 0.1f);
			info.setColour(1, 0.1f, 0.1f);
			message.setText("YOU LOST :(");
			message.show();
			info.show();
		}
		else {
			message.hide();
			info.hide();
			if (this.selected != null) {
				panel.show(guis);
				panel.update();

				// if the active layer isnt complete, hide submit button
				if (layers[activeLayer].isComplete()) {
					if (TRKeyboard.isKeyDown(GLFW.GLFW_KEY_ENTER)) {
						verifyLayer();
					}
				}
				else {
					submit.hide(guis);
				}
			}
			else {
				panel.hide(guis);
			}
		}
	}

}
