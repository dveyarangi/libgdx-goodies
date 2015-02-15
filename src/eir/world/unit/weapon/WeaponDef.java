package eir.world.unit.weapon;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.resources.ResourceFactory;
import eir.resources.levels.UnitDef;
import eir.world.Effect;
import eir.world.unit.Unit;
import eir.world.unit.damage.Damage;

public abstract class WeaponDef extends UnitDef
{
	
	private BulletDef bulletDef;
	private TargetingModule targetingModule;
	public WeaponDef( String type, final int faction, BulletDef bulletDef, final float size, final boolean isPickable, TargetingModule targetingModule)
	{
		super( type, faction, size, isPickable, 0 );
		
		this.bulletDef = bulletDef;
		
		this.targetingModule = targetingModule;
	}

	/**
	 * @return the burstSize
	 */
	public abstract int getBurstSize();

	/**
	 * @return the magazineReloadTime
	 */
	public abstract float getMagazineReloadTime();

	/**
	 * @return the reloadingTime
	 */
	public abstract float getReloadingTime();

	/**
	 * @return the accuracy
	 */
	public abstract float getDispersion();


	public abstract float getBulletLifeDuration();
	/**
	 * @return the bulletBehavior
	 */
	public abstract IBulletBehavior getBulletBehavior();

	/**
	 * @return the speed
	 */
	public abstract float createSpeed();

	public abstract float getAngularSpeed();

	/**
	 * Max angular offset from target orientation when weapon is allowed to fire
	 * @return
	 */
	public abstract float getMaxFireAngle();

	/**
	 * @return
	 */
	public abstract float getBulletSpeed();
	/**
	 *
	 */
	public abstract Damage getDamage();

	public abstract boolean decayOnNoTarget();

	public BulletDef getBulletDef() { return bulletDef; }

	/**
	 * @param firingDir
	 * @return
	 */
	protected abstract float createAngle( Weapon weapon, Vector2 firingDir);

	public abstract Effect createTraceEffect( Bullet bullet, IRenderer renderer );

	public abstract Effect createHitEffect( Bullet bullet, boolean b );

	public abstract float getSensorRadius();

	public abstract void init( ResourceFactory gameFactory );

	public abstract float getShotEnergyConsumption();

	public abstract boolean shouldDieOnCollision();

	public abstract TargetProvider createTargetProvider( final Unit owner );
	
	public TargetingModule getTargetingModule() { return targetingModule; }
}
