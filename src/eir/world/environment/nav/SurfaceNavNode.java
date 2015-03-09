package eir.world.environment.nav;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.Anchor;
import eir.world.unit.Unit;

public class SurfaceNavNode extends NavNode implements Anchor
{


	/**
	 * index of the asteroid containing this nav node
	 */
	final int				aIdx;

	/**
	 * Normal angle
	 */
	float             angle;

	SurfaceNavNode( final NavNodeDescriptor descriptor, final Vector2 point, final int idx, final int aIdx )
	{
		super( descriptor, point, idx );

		this.aIdx = aIdx;

		this.angle = Float.NaN;

	}

	@Override
	public float getAngle() { return angle; }

	@Override
	public Unit getParent() {	return getDescriptor().getObject();	}

}
