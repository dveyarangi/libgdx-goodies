package eir.input;

import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;

import eir.rendering.IRenderer;
import eir.resources.ResourceFactory;
import eir.world.Level;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.spatial.ISpatialObject;

/**
 * TODO: placeholder for building control mode;
 *
 * responds to nav node hovers 
 *
 * @author Fima
 *
 */
public class DebugControlMode implements IControlMode
{

	PickingSensor sensor = new PickingSensor(new IPickingFilter () {

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return true;
			//return entity instanceof Hex || entity instanceof Edge;
		}

	});

	private final Level level;

	private ISpatialObject pickedObject;

	private Sprite crosshair;

	private ResourceFactory gameFactory;

	private int unitIdx;

	DebugControlMode(final ResourceFactory gameFactory, final Level level)
	{
		this.gameFactory = gameFactory;

		this.level = level;

		this.crosshair = new Sprite(gameFactory.getTexture( gameFactory.getCrosshairTexture() ));

	}

	@Override
	public PickingSensor getPickingSensor() { return sensor; }


	@Override
	public void touchUnit( final ISpatialObject pickedObject, int button )
	{
/*		if(pickedObject instanceof Hex)
		{
			Hex hex = (Hex) pickedObject;
			
			switch(button)
			{
			case Input.Buttons.RIGHT:
				hex.setFaction(null);
			
				break;
			case Input.Buttons.LEFT:
				break;
			}
		}
		if(pickedObject instanceof Edge)
		{
			Edge edge = (Edge) pickedObject;
			
			switch(button)
			{
			case Input.Buttons.RIGHT:
				edge.setFaction(null);
//				hex.rotate( 1 );
			
				break;
			case Input.Buttons.LEFT:
				
				hexController.makeMove( edge ) ;
				break;
			}
		}*/
	}

	@Override
	public void render( final IRenderer renderer )
	{

		 if(pickedObject == null)
			 return;

/*		 if(pickedObject instanceof SurfaceNavNode)
		 {
			 SurfaceNavNode pickedNode = (SurfaceNavNode) pickedObject;
			 SpriteBatch batch = renderer.getSpriteBatch();

			 Vector2 point = pickedNode.getPoint();
			 batch.begin();
//			 TextureRegion crossHairregion = crosshair.getKeyFrame( 0, true );
			 TextureRegion crossHairregion = crosshair;
			 batch.draw( crossHairregion,
					 point.x-crossHairregion.getRegionWidth()/2f,
					 point.y-crossHairregion.getRegionHeight()/2f,

					 crossHairregion.getRegionWidth()/2f,
					 crossHairregion.getRegionHeight()/2f,

					 crossHairregion.getRegionWidth(),
					 crossHairregion.getRegionHeight(),

					 1,
					 1,

					 0f);
			 batch.end();
		 }*/
	}

	@Override
	public ISpatialObject objectPicked( final List <ISpatialObject> objects )
	{
		pickedObject = null;
		for(ISpatialObject o : objects)
		{
			if(o instanceof SurfaceNavNode)
			{
				pickedObject = o;
			}
			if(o instanceof IPickable)
			{
				IPickable pickedUnit = (IPickable) o;

				pickedUnit.setIsHovered( true );
				pickedObject = o;
				break;
			}
		}

		return pickedObject;
	}

	@Override
	public void objectUnpicked( final ISpatialObject pickedObject )
	{
//		assert pickedObject == pickedNode;
		if(pickedObject instanceof IPickable)
		{
			IPickable pickedUnit = (IPickable) pickedObject;
			pickedUnit.setIsHovered( false );
		}

		this.pickedObject = null;
	}

	@Override
	public void reset()
	{
		pickedObject = null;
	}

	@Override
	public void keyDown( final int keycode )
	{
		switch(keycode)
		{

		case Input.Keys.N:
/*			unitIdx ++;
			if(unitIdx >= defRobin.length)
			{
				unitIdx = 0;
			}*/

			break;
		}
	}

}
