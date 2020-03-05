package engine.renderEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL42;
import org.lwjgl.openvr.Texture;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import engine.collision.BoundingBox;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TerrainLODModel;
import engine.renderEngine.textures.TextureData;
import internal.ResourceStreamClass;


public class Loader {
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();
	private static float anisoAmount = Math.min(4, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
	
	public static int createEmptyVBO(int floatcnt) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatcnt * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
	}
	
	public static void updateVBO(int vbo, float[] data, FloatBuffer buf) {
		buf.clear();
		buf.put(data);
		buf.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buf);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize,
			int instancedDataLen, int offset) {
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLen * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
	}
	
	public static RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices){
		
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		unbindVAO();
		
		return new RawModel(vaoID,indices.length, null);
	}
	
	public static TerrainLODModel loadToLODTerrainVAO(float[] positions,float[] textureCoords,float[] normals, int[] vbolod, int[] vbolodsize) {
		
		int vaoID = createVAO();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbolod[0]); // default is lod0
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		unbindVAO();
				
		return new TerrainLODModel(vaoID, vbolod, vbolodsize);
	}
	
	public static RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices, BoundingBox aabb){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length, aabb);
	}
	
	public static int loadVAOID(float[] positions, float[] textureCoords){
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	
	public static RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 2, null);
	}
	
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**When the RawModel does not require a bounding box (say, a terrain piece or even water)
	 * then null can be used instead of a vector3f, just don't access it!**/
	
	public static RawModel loadToVAO(float[] positions, int dimensions){
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();

		return new RawModel(vaoID, positions.length / dimensions, null);
		
	}
	
	private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private static void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	private static ByteBuffer fileBuffer = MemoryUtil.memAlloc(64);
	private static int fbSize = 64;
	
	public static int loadTexture(String fileName) {
				
		byte[] allBytes = null;
		
		try {
			InputStream stream = ResourceStreamClass.class.getResourceAsStream("/res/" + fileName + ".png");
			allBytes = stream.readAllBytes();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		if (allBytes.length > fbSize) {
			fileBuffer = MemoryUtil.memRealloc(fileBuffer, allBytes.length);
			fbSize = allBytes.length;
		}
		else {
			fileBuffer.clear();
		}
		
		//ByteBuffer fileBuffer = MemoryUtil.memAlloc(allBytes.length);
		fileBuffer.put(allBytes);
		fileBuffer.flip();
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer components = BufferUtils.createIntBuffer(1);
		
		ByteBuffer imageBuffer = STBImage.stbi_load_from_memory(fileBuffer, width, height, components, 0);
		

		int texID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(), height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
	
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_MAX_TEXTURE_LOD_BIAS, -0.4f);
		//if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisoAmount);
		//}
		//else {
			//System.out.println("Your driver does not support Anisotropic Filtering");
		//}
			
		STBImage.stbi_image_free(imageBuffer);

			
		textures.add(texID);
		
		return texID;
	}
	
	public static void cleanUp(){
		
		for (int vao : vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures){
			GL11.glDeleteTextures(texture);
		}
		
	}
	
	/**
	 * {"right", "left", "top", "bottom", "back", "front"}
	 */
	public static int loadCubeMap(String[] textureFiles) {
		
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile(textureFiles[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		return texID;
		
	}
	
	private static TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		
		final int numChannels = 4;
		
		try {
			@SuppressWarnings("resource")
			InputStream in = ResourceStreamClass.class.getResourceAsStream("/res/" + fileName + ".png");
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(numChannels * width * height);
			decoder.decode(buffer, width * numChannels, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	private static int bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		return vboID;
	}
	
	private static int bindIndicesBuffer(IntBuffer indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		return vboID;
	}
	
	public static int makeIndicesBuffer(IntBuffer indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		return vboID;
	}
	
	public static IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	
	
	// GL30.GL_RGBA32F for fft crap
	public static int genEmptyTexture2D(int width, int height, int glformat) {
		int texID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, glformat, width, height);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		textures.add(texID);
		return texID;
	}
	
	
	
	
}
