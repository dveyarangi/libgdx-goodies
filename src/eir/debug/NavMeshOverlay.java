package eir.debug;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.resources.ResourceFactory;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.unit.overlays.IOverlay;
import gnu.trove.iterator.TIntObjectIterator;

public class NavMeshOverlay implements IOverlay <NavMesh>
{

	private BitmapFont font;
	
	private NavMesh navMesh;
	
	public NavMeshOverlay(NavMesh<SurfaceNavNode> navMesh)
	{

		font = ResourceFactory.loadFont("skins//fonts//default", 0.05f);

		this.navMesh = navMesh;
	}

	@Override
	public void draw( NavMesh nm, IRenderer renderer)
	{
			
			if(navMesh.getNodesNum() == 0)
				return;
			ShapeRenderer shape = renderer.getShapeRenderer();
			SpriteBatch batch = renderer.getSpriteBatch();
			NavNode srcNode;

			
			shape.begin(ShapeType.Filled);
			for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
			{
				shape.setColor( 0, 1, 0, 1f );
				srcNode = navMesh.getNode( fidx );

				shape.circle( srcNode.getPoint().x, srcNode.getPoint().y, 1 );
			}
			shape.end();

			batch.begin();
			for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
			{
				srcNode = navMesh.getNode( fidx );
				font.draw( batch, String.valueOf( fidx ), srcNode.getPoint().x+1, srcNode.getPoint().y+1 );
			}
			batch.end();

			shape.begin(ShapeType.Line);
			shape.setColor( 0, 1, 0, 0.5f );

			TIntObjectIterator<NavEdge> i = navMesh.getEdgesIterator();
			
			i.advance();
			while( i.hasNext() )
			{
				NavEdge e = i.value();
				Vector2 p1 = e.getNode1().getPoint();
				Vector2 p2 = e.getNode2().getPoint();
				shape.line(p1.x, p1.y, p2.x, p2.y);
				i.advance();
			}
			//		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
			//		{
			//			srcNode = navMesh.getNode( fidx );
			//
			//			shape.setColor( 0, 1, 0, 0.5f );
			//			for(NavNode tarNode : srcNode.getNeighbors())
			//			{
			//				shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
			//			}
			//		}
			shape.end();
	}

	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
