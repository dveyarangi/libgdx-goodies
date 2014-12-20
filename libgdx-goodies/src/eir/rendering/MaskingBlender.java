package eir.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class MaskingBlender implements ITextureGenerator
{
	
	private String maskTxr;
	private String colorTxr;
	private Color color;
	
	private String maskingVertexShader;
	private String maskingFragShader;

	public MaskingBlender(String maskTxr, String colorTxr)
	{
		this.maskTxr = maskTxr;
		this.colorTxr = colorTxr;
	}
	public MaskingBlender(String maskTxr, Color color)
	{
		this.maskTxr = maskTxr;
		this.color = color;

	}
		
	@Override
	public Texture generate( AssetManager assman )
	{
		FileHandle vertexShaderFile = Gdx.files.internal( "shaders/masking_vertex.glsl" );
		if(!vertexShaderFile.exists())
			throw new IllegalStateException("cannot find default shader");
		maskingVertexShader = vertexShaderFile.readString();
		FileHandle maskingShaderFile = Gdx.files.internal( "shaders/masking_frag.glsl" );
		if(!maskingShaderFile.exists())
			throw new IllegalStateException("cannot find masking shader");
		maskingFragShader = maskingShaderFile.readString();		
		
		Texture maskTexture = assman.get( maskTxr, Texture.class);
		
//		ShaderProgram.pedantic = false;
		
		ShaderProgram maskingShader = new ShaderProgram( maskingVertexShader, maskingFragShader );
//		ShaderProgram maskingShader = new ShaderProgram( DefaultShader.getDefaultVertexShader(), maskingShaderStr);
//		System.out.println("shader log: " + maskingShader.getLog() );
		if(!maskingShader.isCompiled())
			throw new IllegalArgumentException( "failed to compile shader");
		
		SpriteBatch batch = new SpriteBatch( 2, maskingShader );
		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, maskTexture.getWidth(), maskTexture.getHeight()))	;
		FrameBuffer fbo = new FrameBuffer( Format.RGBA8888, maskTexture.getWidth(), maskTexture.getHeight(), false );
		
		fbo.begin();
		
	//		Texture colorTexture = assman.get( colorTxr, Texture.class);
	//		colorTexture.bind( GL20.GL_TEXTURE1 );
	//		maskingShader.setUniformi("u_colorSetting", GL20.GL_TEXTURE1 );
			// mask is black opaque texture that marks alpha channel for the resulting texture
			// redering mask texture:
	
			
	        batch.begin();
		
		        maskingShader.setUniformf("v_maskingColor", color.r, color.g, color.b);
		
				batch.draw( maskTexture, 0, 0 );
			
			batch.end();
		
//		colorTexture.dispose();
		
		fbo.end();
		Texture result = fbo.getColorBufferTexture(); 
		return result;
		
	}

	@Override
	public String [] getTextures()
	{
		return new String [] { /*colorTxr,*/ maskTxr };
	}

}
