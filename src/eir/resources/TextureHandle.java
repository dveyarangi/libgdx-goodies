package eir.resources;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class TextureHandle
{
	private String path;
	
	private static Map <String, TextureHandle> handles = new HashMap <String, TextureHandle> ();
	
	public TextureHandle() {}
	private TextureHandle( String path ) 
	{
		this.path = Preconditions.checkNotNull( path );
	}

	public static TextureHandle get( final String path )
	{
		TextureHandle handle = handles.get( path );
		if( handle == null )
		{
			handle = new TextureHandle( path );
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
		TextureHandle that = (TextureHandle) object;
		return path.equals( that.path );
	}

	@Override
	public String toString() { return getPath(); }

}
