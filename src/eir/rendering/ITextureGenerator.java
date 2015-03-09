package eir.rendering;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("rawtypes")
public interface ITextureGenerator
{

	String [] getTextures();

	Texture generate(AssetManager assman);

	Array<? extends AssetDescriptor> getDependencies();
	
}
