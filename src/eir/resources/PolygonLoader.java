package eir.resources;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PolygonLoader extends AsynchronousAssetLoader<PolygonShape, AssetLoaderParameters<PolygonShape>>
{
	PolygonShape shape;

	public PolygonLoader(final FileHandleResolver resolver)
	{
		super( resolver );
	}


	@Override
	public void loadAsync( final AssetManager manager, final String fileName, final FileHandle file,
			final AssetLoaderParameters<PolygonShape> parameter)
	{

		Vector2 origin = null;
		Vector2 [] vertices = null;
		if(file.exists())
		{
			ShapeLoader.RigidBodyModel bodyModel = ShapeLoader.readShape( file.readString() ).rigidBodies.get( 0 );
			vertices = bodyModel.shapes.get( 0 ).vertices;

			origin = bodyModel.origin;

			this.shape = new PolygonShape(vertices, origin);
		}
		else
		{
			shape = PolygonShape.generateCircleModel( fileName );
		}


	}

	@Override
	public PolygonShape loadSync( final AssetManager manager, final String fileName, final FileHandle file,
			final AssetLoaderParameters<PolygonShape> parameter )
	{
		return shape;
	}


	@Override
	public Array<AssetDescriptor> getDependencies( final String fileName,
			final FileHandle file, final AssetLoaderParameters<PolygonShape> parameter )
	{
		// TODO Auto-generated method stub
		return null;
	}


}
