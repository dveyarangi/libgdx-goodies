package eir.input;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.world.environment.spatial.ISpatialObject;

/**
 * Control mode determines input handling some subset of game functionality
 *
 * @author Fima
 *
 */
public interface IControlMode
{
	/**
	 * Called before the mode is activated.
	 */
	void reset();

	/**
	 * Sensor for game entities picking
	 * @return
	 */
	PickingSensor getPickingSensor();

	void touchUnit(float worldX, float worldY, float scale,
			ISpatialObject pickedObject, int button);


	/**
	 * Game entity mouse hover callback
	 * @param pickedObjects
	 */
	ISpatialObject objectPicked( float x, float y, List<ISpatialObject> pickedObjects );
	/**
	 * Game entity mouse unhover callback
	 * @param pickedObject
	 */
	void objectUnpicked( ISpatialObject pickedObject );

	/**
	 * Renderer for mode-specific UI
	 * @param renderer
	 */
	void render( IRenderer renderer );

	void keyDown( int keycode );

	void keyUp(int keycode);

	void keyTyped(char keycode);

	void untouch();

//	void dragUnit(int screenX, int screenY, int pointer,
//			ISpatialObject pickedObject);

	void setWorldPointer(Vector2 pointerPosition2, float scale);

	void dragUnit(float worldX, float worldY, float zoom, int pointer,
			ISpatialObject pickedObject);
	
	public void update( float delta );

}
