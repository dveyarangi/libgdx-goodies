/**
 *
 */
package eir.math.curves;

import com.badlogic.gdx.math.Vector2;


/**
 * @author dveyarangi
 * @Deprecated TODO: check vector2 compat
 */
@Deprecated
public class ParametricLine implements ParametricCurve
{
	private Vector2 dir = new Vector2();
	private Vector2 start = new Vector2();

	public ParametricLine()
	{
	}

	public ParametricLine(final Vector2 start, final Vector2 end)
	{
		set(start, end);
	}

	public void set(final Vector2 start, final Vector2 end)
	{

		this.start.set(start);

		dir.set(end).sub( start );
	}

	@Override
	public Vector2 at(final Vector2 target, final float t)
	{
		return target.set( dir ).scl( t ).add( start );
	}

}
