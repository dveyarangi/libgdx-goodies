package eir.world.environment.nav;

import com.badlogic.gdx.math.Vector2;

import eir.resources.PolygonalModel;
import eir.world.environment.Anchor;
import eir.world.environment.Asteroid;

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
		int navIdx = getDescriptor().getIndex();
		Asteroid asteroid = (Asteroid)getDescriptor().getObject();

	}

	@Override
	public float getAngle() { return angle; }

	@Override
	public Object getParent() {	return getDescriptor().getObject();	}

}
