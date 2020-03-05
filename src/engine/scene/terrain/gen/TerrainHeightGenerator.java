package engine.scene.terrain.gen;

public abstract interface TerrainHeightGenerator {
	
	public abstract float generateHeight(int x, int z);

}
