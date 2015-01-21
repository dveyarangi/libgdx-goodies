/**
 *
 */
package eir.world.unit.weapon;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */

public class Bullet extends Unit implements IDamager
{

	//////////////////////////////////////////////////////////////////

	Weapon weapon;




	public boolean leaveTrace = false;
	public boolean decaying = false;
	
	public Vector2 lastTargetPosition;
	
	

	Bullet()
	{
		super();
		
		this.lastTargetPosition = new Vector2();
	}

	@Override
	protected void reset( final Level level )
	{
		super.reset( level );
		//this.hull = new Hull(0.001f, 0f, new float [] {0f,0f,0f,0f});
		this.leaveTrace = false;

		this.decaying = false;
	}


	/**
	 * @param delta
	 */
	@Override
	public void update(final float delta)
	{
		super.update( delta );

		if(target != null && ! this.target.isAlive() )
		{
			this.lastTargetPosition.set( target.getArea().getAnchor() );
			this.setDecaying( true );
			this.target = null;
		}

		if( weapon.getDef().decayOnNoTarget() && isDecaying() )
		{
			this.lifetime += delta * (this.lifelen - this.lifetime) * 5f;
		}

		if(this.target != null && this.getArea().getAnchor().dst2( this.target.getArea().getAnchor() )  < 1)
		{
			this.setDead();
		}

		weapon.getDef().getBulletBehavior().update( delta, this );
	}

	@Override
	public void draw( final IRenderer renderer )
	{
		super.draw( renderer );

		if(leaveTrace)
		{
//			if(RandomUtil.oneOf( 3 ))
			{
				Effect effect = weapon.getDef().createTraceEffect(this, renderer);
				if(effect != null)
				{
					renderer.addEffect( effect );
				}
			}
		}

	}

	@Override
	public ISpatialObject getTarget() { return target; }


	@Override
	public Weapon getWeapon() {	return weapon; }

	@Override
	public Damage getDamage()
	{
		return weapon.getDef().getDamage();
	}
	@Override
	public Unit getSource()
	{
		return getWeapon();
	}

	@Override
	public float getMaxSpeed() {return weapon.getDef().getBulletSpeed(); }

	public void setDecaying( final boolean decaying ) { this.decaying = decaying; }

	public boolean isDecaying() { return decaying; }

	@Override
	public Effect createDeathEffect( )
	{
/*		if( decaying )
			return null;
		else*/
		return weapon.getDef().createHitEffect( this, true );
	}
	
	@Override
	protected float damage( final Damage source, final float damageCoef )
	{
		
		setDead();
		return 0;
	}
	
	@Override
	public boolean needsSpatialUpdate() { return true; }
	@Override
	public boolean isCollidable() { return true; }

}
