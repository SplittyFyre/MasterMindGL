package mastermind;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import engine.audio.AudioEngine;
import engine.fontRendering.TextMaster;
import engine.postProcessing.Fbo;
import engine.postProcessing.PostProcessing;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.TRProjectionCtx;
import engine.renderEngine.TRRenderEngine;
import engine.renderEngine.guis.GUITexture;
import engine.renderEngine.guis.render.GUIRenderer;
import engine.scene.TRScene;
import engine.scene.entities.Light;
import engine.scene.entities.camera.RogueCamera;
import engine.scene.entities.camera.TRCamera;
import engine.scene.particles.ParticleWatcher;
import engine.utils.TRMath;
import engine.utils.TRMouse;
import mastermind.game.Game;
import mastermind.game.Layer;
import mastermind.game.MasterMindCamera;

public class MasterMindMain {
	
	public static void main(String[] args) {
		
		TRDisplayManager.createDisplay(3200 - 1600, 1800 - 900, 120, "Master Mind GL", null);
		
		
		TRRenderEngine engine = new TRRenderEngine(
				TRRenderEngine.RENDER_ENTITIES_BIT | 
				TRRenderEngine.RENDER_TERRAIN_BIT |
				TRRenderEngine.RENDER_SKYBOX_BIT | 
				TRRenderEngine.RENDER_DUDVWATER_BIT,
				new TRProjectionCtx(7.41f, 30000, TRCamera.STD_FOV));
		
		PostProcessing.init();
		TextMaster.init();
		AudioEngine.init();

		GUIRenderer guiRenderer = new GUIRenderer();
		
		
		Fbo fbo = new Fbo(TRDisplayManager.getWidth(), TRDisplayManager.getHeight());
		Fbo output = new Fbo(TRDisplayManager.getWidth(), TRDisplayManager.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo output2 = new Fbo(TRDisplayManager.getWidth(), TRDisplayManager.getHeight(), Fbo.DEPTH_TEXTURE);
		
		TRScene scene = new TRScene();
		
		TRCamera camera = new MasterMindCamera();
		scene.setCamera(camera);
		
		List<GUITexture> guis = new ArrayList<GUITexture>();
		
		
		float lightIntensity = 2f;
		Light light = new Light(new Vector3f(1000, 200, 1000), new Vector3f(lightIntensity, lightIntensity, lightIntensity));
		scene.addLight(light);
		
		Game game = new Game(scene, engine.getActiveProjection().getMatrix(), guis);
		
		while (!TRDisplayManager.windowShouldClose()) {
			
			camera.update();

			game.update();
			
			ParticleWatcher.update();
			engine.renderScene(scene, fbo);
			fbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, output);
			fbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, output2);
			PostProcessing.doPostProcessing(output.getColourTexture(), output2.getColourTexture());
			
			
			guiRenderer.render(guis);
			TextMaster.drawText();
			
			TRDisplayManager.updateDisplay();
		}
		
		engine.cleanUp();
		guiRenderer.cleanUp();
		PostProcessing.cleanUp();
		AudioEngine.cleanUp();
		TextMaster.cleanUp();
		TRDisplayManager.closeDisplay();
		
	}

}
