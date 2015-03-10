package eir.rendering;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class MaskingBlender implements ITextureGenerator
{
	
	private String maskTxr;
	private String colorTxr;
	private Color color;

	
	public static final String MASKING_SHADER = "masking-shader";
	
	private static final Array<AssetDescriptor> dependencies = new Array(new AssetDescriptor[] { 
			new AssetDescriptor(MASKING_SHADER, ShaderProgram.class) });

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
	
		
		Texture maskTexture = assman.get( maskTxr, Texture.class);
		
//		ShaderProgram.pedantic = false;
		
		ShaderProgram maskingShader = assman.get(MASKING_SHADER);
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
		
		        maskingShader.setUniformf("v_maskingColor", color.r, color.g, color.b, -1);
		
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
	@Override
	public Array<AssetDescriptor> getDependencies() {	return dependencies; }

}
