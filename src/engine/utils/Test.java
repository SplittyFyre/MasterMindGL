package engine.utils;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import engine.renderEngine.TRDisplayManager;

public class Test {
	
	public static void main(String[] args) {
		
		TRDisplayManager.createDisplay(1600, 900, 60, "hello", null);
		
		float timer = 0;
		
		while (!TRDisplayManager.windowShouldClose()) {
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0, 1, 0, 1);
			
			System.out.println(TRDisplayManager.Mouse.getDX() + " " + TRDisplayManager.Mouse.getDY());
			
			
			TRDisplayManager.updateDisplay();
		}
		
		TRDisplayManager.closeDisplay();
		
	}
	
	public static float binom(float p, int k, int n) {
		
		int choose = fac(n) / (fac(k) * fac(n - k));
		
		float var = (float) Math.pow((1 - p), (n - k));
		
		return (float) (choose * Math.pow(p, k) * var);
		
	}
	
	public static double binomd(float p, int k, int n) {
		
		int choose = fac(n) / (fac(k) * fac(n - k));
		
		double var = Math.pow((1 - p), (n - k));
		
		return choose * Math.pow(p, k) * var;
		
	}
	
	public static int fac(int i) {
		
		int ret = 1;
		
		for (; i > 0; i--) {
			ret *= i;
		}
		
		return ret;
		
	}

}
