package mastermind.game;

import java.util.List;
import java.util.prefs.BackingStoreException;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import engine.fontMeshCreator.GUIText;
import engine.renderEngine.Loader;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.guis.GUIStruct;
import engine.renderEngine.guis.GUITexture;
import engine.renderEngine.guis.IButton;
import engine.renderEngine.guis.SFAbstractButton;
import engine.renderEngine.textures.SCTexture;
import engine.scene.TRScene;
import engine.scene.entities.StaticEntity;
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
	
	private boolean won = false, lost = false;
	
	private int activeLayer = 0;
	
	private float t = 0;
	
	private TRRayCaster rc;
	
	private List<GUITexture> guis;
	
	public Game(TRScene scene, Matrix4f pmat, List<GUITexture> guis) {
		this.scene = scene;
		layers = new Layer[10];
		for (int i = 0; i < 10; i++) {
			layers[i] = new Layer(i, scene);
		}
		this.correctLayer = Layer.getRandom();
		
		System.out.println(correctLayer.toString());
		
		rc = new TRRayCaster(scene.getCamera(), pmat);
		this.guis = guis;
		initGUIS();
	}
	
	public void update() {
		rc.update();
		updateGUIS();
		
		for (int i = 0; i < 10; i++) {
			Layer layer = layers[i];
			if (i == activeLayer) {
				layer.update(rc, this);
			}
			else {
				layer.update();
			}
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
		if (this.correctLayer.equals(this.layers[activeLayer])) {
			// win
		}
		else {
			
			// process more
			
			if (this.activeLayer == 9) { // this was their final chance
				// lose
			}
			else {
				activeLayer++;
			}
		}
	}
	
	private void win() {
		this.won = true;
	}
	private void lose() {
		this.lost = true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private GUIStruct panel;
	private GUITexture background;
	private SFAbstractButton submit;
	
	private GUIText message;
	
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
		
		
		
		submit = new SFAbstractButton(panel, "submit", new Vector2f(-0.1f, -0.05f), TRMath.sqr4) {
			
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
				
				
			}
		};
		
		message = new GUIText("u shouldnt see this", 1.7f, TRUtils.trFont, TRMath.coordtext(0.7f, -0.05f - 0.8f), 0.5f, false);		
		message.setColour(1, 1, 1);
	}
	
	private void updateGUIS() {
		
		// not worth it to use a switch case
		if (this.won) {
			panel.hide(guis);
			background.show(guis);
			
		}
		else if (this.lost) {
			
		}
		else {
			if (this.selected != null) {
				panel.show(guis);
				panel.update();
				message.show();

				// if the active layer isnt complete, hide submit button
				if (!layers[activeLayer].isComplete()) {
					submit.hide(guis);
				}
			}
			else {
				panel.hide(guis);
			}
		}
	}

}
