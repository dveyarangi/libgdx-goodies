package eir.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eir.game.screens.IGameUI;
import eir.rendering.IRenderer;
import eir.resources.levels.LevelDef;
import eir.world.Level;
import eir.world.environment.spatial.ISpatialObject;

/**
 * handles input for game
 * @author Ni
 *
 */
public class GameInputProcessor implements InputProcessor
{
	private final InputMultiplexer inputMultiplexer;
	private FreeCameraController freeController;
	private ICameraController camController;

	protected Level level;

	private int /*prevx, prevy,*/ currx, curry;
	private final Vector3 pointerPosition3 = new Vector3();
	private final Vector2 pointerPosition2 = new Vector2();

//	private boolean dragging = false;

//	private float lifeTime = 0;

	private List <IControlMode> controlModes;
	private int controlModeIdx;
	private IControlMode currControlMode;

	private ISpatialObject pickedObject;
	
	private ISpatialObject touchedObject;

	private TimeController timeController;

	private UIInputProcessor uiProcessor;
	
	private boolean debugPick = false;
	
	protected float BASE_PICK_RADIUS = 10;
	protected float pickRadius = BASE_PICK_RADIUS;
	

	protected IGameUI ui;
//	private boolean pointerChanged = true;


	public GameInputProcessor(final LevelDef level, final IGameUI ui)
	{



		inputMultiplexer = new InputMultiplexer();

		this.uiProcessor = new UIInputProcessor();
		
		this.ui = ui;

		this.timeController = new TimeController();

		uiProcessor.registerAction( Keys.PLUS, new InputAction() {
			@Override
			public void execute( final InputContext context )
			{
				timeController.setTarget( timeController.getTargetModifier() * 2f);
			}
		});
		uiProcessor.registerAction( Keys.MINUS, new InputAction() {
			@Override
			public void execute( final InputContext context )
			{
				timeController.setTarget( timeController.getTargetModifier() / 2f);
			}
		});


		inputMultiplexer.addProcessor( uiProcessor );
		inputMultiplexer.addProcessor( new GestureDetector(new GameGestureListener(camController)) );
		inputMultiplexer.addProcessor( this );

//		prevx = -1;
//		prevy = -1;

	}
	
	public void init( Level level )
	{
		this.level = level;
		
		controlModes = new ArrayList <IControlMode> ();
		createControlModes( controlModes, level);
		currControlMode = controlModes.get(0);
		controlModeIdx = 0;


		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		
		OrthographicCamera camera = new OrthographicCamera( w, h );	
		freeController = new FreeCameraController(camera, level);

		camController = freeController;

	}

	protected void createControlModes( List <IControlMode> modes, Level level)
	{
		modes.add( new DebugControlMode( level ) );
	}

	@Override
	public boolean keyDown(final int keycode)
	{
		switch(keycode)
		{

		case Input.Keys.M:
			controlModeIdx = (controlModeIdx + 1) % controlModes.size();
			currControlMode = controlModes.get(controlModeIdx);
			currControlMode.reset();

			break;

/*		case Input.Keys.SPACE:
			if(camController == freeController)
			{
				autoController.zoomTarget = camController.getCamera().zoom;
				camController = autoController;
			}
			else
			{
				camController = freeController;
			}
			break;*/
		default:

			currControlMode.keyDown( keycode );
			return false;
		}

		return false;
	}

	@Override
	public boolean keyUp(final int keycode)
	{
		switch(keycode)
		{
		default:
			currControlMode.keyUp( keycode );
			return false;
		}
//		return false;
	}

	@Override
	public boolean keyTyped(final char keycode)
	{
		switch(keycode)
		{
		default:
			currControlMode.keyTyped( keycode );
			return false;
		}	
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
	{
		registerPointerChange( screenX, screenY, getCamera().zoom ); 
		if( button==Input.Buttons.RIGHT )
		{

			camController.setUnderUserControl(true);
//			dragging = true;

		}
		if( pickedObject != null )
		{
			if(touchedObject == null)
			{
				touchedObject = pickedObject;
			}
		}
		
		currControlMode.touch( pointerPosition2.x, pointerPosition2.y, getCamera().zoom, pickedObject, button );
		
		return true;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
	{
		registerPointerChange( screenX, screenY, getCamera().zoom ); 
//		if(button == Input.Buttons.RIGHT)
		{
//			dragging = false;
			camController.setUnderUserControl(false);
				touchedObject = null;
				currControlMode.untouch();
		}
		return true;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer)
	{
		registerPointerChange( screenX, screenY, getCamera().zoom ); 

		if(touchedObject != null)
			currControlMode.dragUnit(pointerPosition2.x, pointerPosition2.y, getCamera().zoom, pointer, pickedObject);

		return true;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY)
	{

		registerPointerChange( screenX, screenY, getCamera().zoom ); 
		return true;
	}

	@Override
	public boolean scrolled(final int amount)
	{
		camController.zoomTo( currx, curry, amount*1.2f );
		return true;
	}

	public OrthographicCamera getCamera() { return camController.getCamera(); }

	/**
	 *
	 */
	public void show()
	{
		Gdx.input.setInputProcessor( inputMultiplexer );
	}

	 /**
	  * @param delta
	  */
	 public void update(final float delta)
	 {
//		 lifeTime += delta;
		 
		 camController.update( delta );
		 

		 registerPointerChange( currx, curry, getCamera().zoom );
		 
		 timeController.update( delta );
		 
		 currControlMode.update(delta);
	 }
	 
	private void registerPointerChange(int screenX, int screenY, float zoom)
	{
		if(screenX == currx && screenY == curry)
		{
//			pointerChanged  = false;
			return;
		}
		
		currx = screenX;
		curry = screenY;
		
		 pickRadius = BASE_PICK_RADIUS * camController.getCamera().zoom;
		 pointerPosition3.x = currx;
		 pointerPosition3.y = curry;
		 camController.getCamera().unproject( pointerPosition3 );
		 pointerPosition2.x = pointerPosition3.x;

		 pointerPosition2.y = pointerPosition3.y;

		 currControlMode.setWorldPointer( pointerPosition2, camController.getCamera().zoom);

		 PickingSensor sensor = currControlMode.getPickingSensor();
		 sensor.setCursor(pointerPosition2);
		 sensor.clear();
		 level.getEnvironment().getIndex().queryAABB( sensor,
				 pointerPosition2.x,
				 pointerPosition2.y, pickRadius, pickRadius);


		 List <ISpatialObject> pickedObjects = sensor.getPickedObjects();
		 ISpatialObject newPickedObject = null;
		 if(pickedObjects != null)
		 {
			 newPickedObject = currControlMode.objectPicked( pointerPosition2.x, pointerPosition2.y, pickedObjects );
			 
		 }

		 if(newPickedObject != pickedObject)
		 {
			 currControlMode.objectUnpicked( pickedObject );
			 pickedObject = newPickedObject;
		 }		
		 
//		pointerChanged = true;
	}


	public void draw( final IRenderer renderer )
	 {
		 final ShapeRenderer shape = renderer.getShapeRenderer();
		 
		 if( debugPick )
		 {
			 shape.setColor(0, 0, 1, 0.5f);
			 shape.begin(ShapeType.Line);
			 shape.rect(pointerPosition2.x-pickRadius, pointerPosition2.y-pickRadius, 2*pickRadius, 2*pickRadius);
			 shape.end();
			 
/*			 if(pickedObject != null)
			 {
				 shape.begin(ShapeType.Line);
				 shape.rect(pickedObject.getArea().getMinX(), pickedObject.getArea().getMinX(),
						 pickedObject.getArea().getHalfWidth()*2, 2*pickedObject.getArea().getHalfHeight());
				 shape.end();
			 }*/
		 }
		 
		 
		 
/*		 batch.begin();
		 TextureRegion crossHairregion = crosshair.getKeyFrame( lifeTime, true );
		 batch.draw( crossHairregion,
				 pointerPosition2.x-crossHairregion.getRegionWidth()/2, pointerPosition2.y-crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(),
				 5f/crossHairregion.getRegionWidth(),
				 5f/crossHairregion.getRegionWidth(), 0);
		 batch.end();*/

/*		 shape.setColor( 0, 1, 0, 0.1f );
		 shape.begin( ShapeType.Line );
		 shape.line( level.getControlledUnit().getBody().getAnchor().x, level.getControlledUnit().getBody().getAnchor().y,
				 pointerPosition2.x, pointerPosition2.y );
		 shape.end();*/

		 currControlMode.render( renderer );
	 }

	 /**
	  * @param width
	  * @param height
	  */
	 public void resize(final int width, final int height)
	 {
//		 autoController.resize(width, height);
		 freeController.resize(width, height);
	 }

	 public Vector2 getCrosshairPosition()
	 {
		 return pointerPosition2;
	 }
	 
	 public UIInputProcessor getInputRegistry() { return uiProcessor; }

	public float getTimeModifier() { return timeController.getModifier() ; }

}
