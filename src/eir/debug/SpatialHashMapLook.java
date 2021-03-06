package eir.debug;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.rendering.IRenderer;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.unit.overlays.IOverlay;

/**
 * Renders entity index
 *
 * @author dveyarangi
 *
 */
public class SpatialHashMapLook implements IOverlay
{

	private final SpatialHashMap <ISpatialObject> map;

	public SpatialHashMapLook(final SpatialHashMap <ISpatialObject> map)
	{
		this.map = map;
	}

	public void draw(final IRenderer rend)
	{
		ShapeRenderer renderer = rend.getShapeRenderer();
		GL20 gl = Gdx.gl;
		gl.glEnable(GL20.GL_BLEND);

		int cellX, cellY;
		float cellsize = map.getCellSize();
		float halfCellSize = cellsize / 2.f;
		float minx = -map.getWidth()/2-halfCellSize;
		float maxx = map.getWidth()/2-halfCellSize;
		float miny = -map.getHeight()/2-halfCellSize;
		float maxy = map.getHeight()/2-halfCellSize;
		renderer.setColor(0f, 0f, 0.4f, 0.5f);
		renderer.begin( ShapeType.Line );
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			renderer.line( minx, y, maxx, y);
		}

		for(float x = minx; x <= maxx; x += map.getCellSize())
		{
			renderer.line( x, miny,x, maxy);

		}

		renderer.end();

		List <ISpatialObject> bucket = null;
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			cellY = map.toGridIndex( y );
			for(float x = minx; x < maxx; x += map.getCellSize())
			{

				cellX = map.toGridIndex( x );
				
				bucket = map.getBucket(cellX, cellY);

				if(bucket != null && bucket.size() != 0)
				{
					boolean isReal = false;
					ISpatialObject o = null;
					for(int idx = 0; idx < bucket.size(); idx ++)
					{
						o = bucket.get(idx);
						if(o.getArea().overlaps( AABB.createFromEdges(x, y, x+cellsize, y+cellsize) ))
						{
							isReal = true;
							break;
						}
					}
					if(isReal)
					{
						renderer.setColor(0.8f, 0.6f, 0.8f, 0.2f);
						renderer.begin( ShapeType.Filled );
						renderer.rect( x, y, cellsize, cellsize);
						renderer.end();
					}
					else
					{
						renderer.setColor(0.1f, 0.5f, 0.1f, 0.2f);
						renderer.begin( ShapeType.Filled );
						renderer.rect( x, y, cellsize, cellsize);
						renderer.end();
					}
				}
			}
		}
	}

	@Override
	public void draw(Object unit, IRenderer renderer)
	{
		draw( renderer );
	}

	@Override
	public boolean isProjected()
	{
		return true;
	}

}
