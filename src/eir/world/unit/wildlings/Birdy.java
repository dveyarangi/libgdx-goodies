/**
 *
 */
package eir.world.unit.wildlings;

import com.badlogic.gdx.math.Vector2;

import eir.world.Level;
import eir.world.unit.Damage;
import eir.world.unit.Hull;
import eir.world.unit.IDamager;
import eir.world.unit.TaskedUnit;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Birdy extends TaskedUnit implements IDamager
{
	///////////////////////////////////////////
//	private static Sprite sprite = GameFactory.createSprite( "anima//gears//birdy.png" );
//	private static int animationId = GameFactory.registerAnimation("anima//glow//glow.atlas",	"glow");

//	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas",
//			"explosion02");

	public static float maxSpeed = 20;

	private float size = 2;

	///////////////////////////////////////////

	public Damage damage = new Damage(1,1,1,1);

	public Vector2 impactImpulse;

	@Override
	protected void reset( final Level level  )
	{
		super.reset( level );
		this.hull = new Hull(300f, 0f, new float [] {0f,0f,0f,0f});
	}


	@Override
	public void update(final float delta)
	{
		super.update( delta );
	}
	@Override
	public float hit(final Damage source, final IDamager damager, final float damageCoef)
	{
		float damage = super.hit( source, damager, damageCoef );


		Unit unit = (Unit)damager;

/*		impactImpulse = this.getArea().getAnchor().tmp()
				.sub(
						unit.getArea().getAnchor() )
				.nor()
				.mul( damage );


		getVelocity().add( impactImpulse );*/

		return damage;
	}

	@Override
	public float getSize() { return size; }

	@Override
	public Damage getDamage() {	return damage; }

	@Override
	public Unit getSource()
	{
		return this; // TODO: maybe generalize to drone and make source the spawner?
	}

}
