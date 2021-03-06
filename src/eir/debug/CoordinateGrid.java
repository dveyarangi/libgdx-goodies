package eir.debug;

import yarangi.math.FastMath;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.rendering.IRenderer;
import eir.world.unit.overlays.IOverlay;



/**
 * Displays grid debug overlay (with powers of 2 steps).
 *
 * The grid automatically adjusts to the zoom level.
 *
 * TODO: print axis values
 *
 *
 * @author dveyarangi
 *
 */
public class CoordinateGrid implements IOverlay
{
	private final OrthographicCamera camera;
	private final float halfWidth;
	private final float halfHeight;

	public CoordinateGrid(final float width, final float height, final OrthographicCamera camera)
	{
		this.camera = camera;
		this.halfHeight = height/2;
		this.halfWidth = width/2;
	}

	@Override
	public void draw( Object unit, final IRenderer renderer )
	{


		ShapeRenderer shape = renderer.getShapeRenderer();

		GL20 gl = Gdx.gl;
		gl.glEnable(GL20.GL_BLEND);

		// TODO: dont be lazy, use sqrt
		float order;
		for( order = 2048f; order > 0.000001; order /= 2f) {
			if(Math.round(  order / camera.zoom) == 0)
			{
				break;
			}
		}

		// lower left screen corner in world coordinates
		float screenMinX = camera.position.x - camera.viewportWidth/2*camera.zoom;
		float screenMinY = camera.position.y - camera.viewportHeight/2*camera.zoom;
		// higher right screen corner in world coordinates
		float screenMaxX = camera.position.x + camera.viewportWidth/2*camera.zoom;
		float screenMaxY = camera.position.y + camera.viewportHeight/2*camera.zoom;

		int steps = 8;
		shape.begin(ShapeType.Line);

		for(int i = 1; i <= steps; i ++) {

			int magnitude = (int)Math.pow(2, i);
			float step = order*magnitude*4;

			shape.setColor( 0, 0, 1f, 0.05f);


			float minx = Math.max( -halfWidth,  FastMath.toGrid( screenMinX, step ) );
			float maxx = Math.min(  halfWidth,  FastMath.toGrid( screenMaxX, step ) );
			float miny = Math.max( -halfHeight, FastMath.toGrid( screenMinY, step ) );
			float maxy = Math.min(  halfHeight, FastMath.toGrid( screenMaxY, step ) );

			for(float x = minx; x <= maxx; x += step)
			{
				shape.line( x, miny, x, maxy );
			}
			for(float y = miny; y <= maxy; y += step)
			{
				shape.line( minx, y, maxx, y );
			}
		}

		shape.end();

	}


	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}


}
