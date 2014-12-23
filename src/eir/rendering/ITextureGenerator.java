package eir.rendering;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public interface ITextureGenerator
{

	String [] getTextures();

	Texture generate(AssetManager assman);
	
}
