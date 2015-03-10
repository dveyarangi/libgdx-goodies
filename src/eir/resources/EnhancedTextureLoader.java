package eir.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import eir.rendering.ITextureGenerator;

public class EnhancedTextureLoader extends TextureLoader
{
	private Pixmap generatedPixels;
	
	private Map <String, ITextureGenerator> generators = new HashMap <String, ITextureGenerator> ();
	
	private Array <AssetDescriptor> dependencies = new Array <AssetDescriptor> ();
	

	public EnhancedTextureLoader(final FileHandleResolver resolver)
	{
		super( resolver );
	}
	
	public void registerGenerator(String name, ITextureGenerator generator)
	{
		FileHandle handle = Gdx.files.internal( name );
		if(handle.exists())
			throw new IllegalArgumentException("Generator name masks resource name " + name );
		if(generators.containsKey( name ))
			throw new IllegalArgumentException("Generator name "+ name + " already registered.");
		
		generators.put( name, generator );
		
		
	}
	@Override
	public void loadAsync (final AssetManager manager, final String fileName, final FileHandle file, final TextureParameter parameter)
	{
		Gdx.app.log("texture", "Loading texture " + fileName);
		if(file.exists())
		{
			super.loadAsync( manager, fileName, file, parameter );
			return;
		}
		ITextureGenerator generator = generators.get( fileName );
		if(generator == null)
			throw new RuntimeException("No texture generator for " + fileName);
		
		dependencies.clear();
		for(String texture : generator.getTextures())
		{
			dependencies.add(new AssetDescriptor<>( texture, Texture.class));
		}
		
		dependencies.addAll(generator.getDependencies());
		// generating texture buffer:
/*		Mask mandala = Mandala.generateMandala( fileName );

		// preparing background:
		String backgroundFile = "anima\\transparent_" + mandala.size() + "x" + mandala.size() + ".png";
		generatedPixels = new Pixmap( Gdx.files.internal( backgroundFile ) );

		// copying buffer to background:
		for(int mx = 0; mx < mandala.size(); mx ++) for(int my = 0; my < mandala.size(); my ++)
		{
			int source = mandala.at( mx, my );
			int b = source&0xFF;
			int g = source>>8&0xFF;
			int r = source>>16&0xFF;
			int a = source>>24&0xFF;
			generatedPixels.drawPixel( mx, my, ColorUtil.toRGBA8888( a, r, g, b )  );
		}*/
	}
	
	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, TextureParameter parameter) {
		return dependencies;
	}


	@Override
	public Texture loadSync (final AssetManager manager, final String fileName, final FileHandle file, final TextureParameter parameter)
	{
		FileHandle handle = Gdx.files.internal( fileName );
		if(handle.exists())
		{
			return super.loadSync( manager, fileName, file, parameter );
		}
		
		Texture texture = generators.get( fileName ).generate( manager );
		
		return texture;
		
	}
}
