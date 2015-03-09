/**
 *
 */
package eir.world.environment;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.resources.ResourceFactory;
import eir.resources.levels.AsteroidDef;
import eir.world.Level;
import eir.world.environment.nav.SurfaceNavNode;

/**
 * @author dveyarangi
 *
 */
public class Asteroid
{

	private AsteroidDef def;

	/**
	 * Asteroid sprite overlay
	 */
//	private EntityRenderer <Asteroid> renderer;


	Sprite sprite;

	private float angle;

	public Asteroid( )
	{
	}

	public String getName() { return def.getName(); }

	public float getAngle()	{ return def.getAngle();	}
	public Vector2 getPosition() { return def.getPosition();	}
	public float getSize() { return def.getSize(); }

	public void init(final ResourceFactory gameFactory, final Level level, final AsteroidDef def)
	{
		this.def = def;

		sprite = gameFactory.createSprite(
				def.getTexture(),
				def.getPosition(),
				new Vector2(0,0),
				def.getSize(), def.getSize(), def.getAngle() );

	}


	/**
	 * @param batch
	 * @param shapeRenderer
	 */
	public void draw( final IRenderer renderer )
	{
///		angle += 		def.getRotation();

		renderer.getSpriteBatch().draw( sprite,
				def.getPosition().x-sprite.getRegionWidth()/2, def.getPosition().y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				getSize()/sprite.getRegionWidth(),
				getSize()/sprite.getRegionWidth(), angle);
//		sprite.draw( renderer.getSpriteBatch() );
	}

	public SurfaceNavNode getNavNode()
	{
		return null;
	}



}
