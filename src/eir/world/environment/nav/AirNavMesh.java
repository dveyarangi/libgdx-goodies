package eir.world.environment.nav;

import com.badlogic.gdx.math.Vector2;

public class AirNavMesh extends NavMesh
{

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public Route getShortestRoute(final NavNode from, final NavNode to) {
		return null;
	}

	@Override
	protected NavNode createNavNode( final NavNodeDescriptor descriptor, final Vector2 point, final int nodeIdx )
	{
		return new AirNavNode( descriptor, point, nodeIdx );
	}

}
