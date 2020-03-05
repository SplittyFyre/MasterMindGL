package engine.scene.terrain.gen;

public class FractionalBrownianMotionGenerator implements TerrainHeightGenerator {
	
	private static final int MAX_OCTAVES = 10;
	
	private NoiseGenerator basis;
	private float fractalIncrement, lacunarity, octaves;
	private float freq;
	private float[] exparr = new float[MAX_OCTAVES];
	
	public FractionalBrownianMotionGenerator(float fractalIncrement, float lacunarity, float octaves, int seed, float amplitude) {
		this.fractalIncrement = fractalIncrement;
		this.lacunarity = lacunarity;
		this.octaves = octaves;
		this.basis = new NoiseGenerator(seed, amplitude);
		
		freq = 1f;
		for (int i = 0; i < MAX_OCTAVES; i++) {
			exparr[i] = (float) Math.pow(freq, -fractalIncrement);
			freq *= lacunarity;
		}
		
	}
	
	@Override
	public float generateHeight(int x, int z) {
		
		float value = 0f;
		
		float ex = x, zed = z;
		
		int i;
		for (i = 0; i < octaves; i++) {
			value += this.basis.generateHeight((int) ex, (int) zed) * exparr[i];
			ex*= lacunarity; zed *= lacunarity;
		}
		
		float remainder = octaves - (int) octaves;
		if (remainder != 0.0f) {
			value += (remainder * this.basis.generateHeight((int) ex, (int) zed) * exparr[i]);
		}
		
		return value;
	}

}
