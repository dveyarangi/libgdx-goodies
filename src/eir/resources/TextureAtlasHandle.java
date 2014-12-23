package eir.resources;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class TextureAtlasHandle
{
	private String path;
	
	private static Map <String, TextureAtlasHandle> handles = new HashMap <String, TextureAtlasHandle> ();
	public TextureAtlasHandle() {}
	private TextureAtlasHandle( String path ) 
	{
		this.path = Preconditions.checkNotNull( path );
	}


	public static TextureAtlasHandle get( final String path )
	{
		TextureAtlasHandle handle = handles.get( path );
		if( handle == null )
		{
			handle = new TextureAtlasHandle( path );
			handles.put( path, handle );
		}
		
		return handle;
	}
	
	public String getPath() { return path; }

	@Override
	public int hashCode() { return path.hashCode(); }
	@Override
	public boolean equals(final Object object)
	{
		TextureAtlasHandle that = (TextureAtlasHandle) object;
		return path.equals( that.path );
	}

	@Override
	public String toString() { return getPath(); }

}
