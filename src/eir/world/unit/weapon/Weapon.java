/**
 *
 */
package eir.world.unit.weapon;

import yarangi.math.Angles;

import com.badlogic.gdx.math.Vector2;

import eir.world.Level;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.resource.Port;
import eir.world.resource.Resource;
import eir.world.resource.Resource.Type;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

/**
 * @author dveyarangi
 *
 */
public class Weapon extends Unit
{

	//////////////////////////////////
	// properties


	////////////////////////////////
	// state
	protected Vector2 weaponDir;

//	protected float angle;

	protected float timeToReload;
	protected int bulletsInMagazine;

	protected Vector2 targetOrientation;

	protected Vector2 relativePosition;

	private static final float PLANK_CONST = 0.0001f;
	
	private static final float MAX_ENERGY = 1000;

	private boolean isOriented = false;

	private Level level;

	WeaponDef weaponDef;
	
	private Port port;
	
	private float requestedResource;
	private float pendingResourceSupply;

	
	

	/**
	 *
	 */
	public Weapon ()
	{
		super();

		weaponDir = new Vector2(1,0);


		// TODO: set position of weapon relative to host
		this.relativePosition = new Vector2();
	}

	@Override
	public void reset( final Level level )
	{
		super.reset( level );

		this.level = level;
		
		this.port = new Port();
		port.setCapacity(Type.ENERGY, MAX_ENERGY, MAX_ENERGY);
		
		
		weaponDef = (WeaponDef)def;

		weaponDef.init( level.getResourceFactory() );

		targetOrientation = weaponDir.cpy();
	}

	public Bullet fire( final ISpatialObject target )
	{

		WeaponDef def = getDef();
		
		Resource energyStock = port.get(Resource.Type.ENERGY);
		if(energyStock.getAmount() < def.getShotEnergyConsumption())
			return null;

		Bullet bullet = createBullet( target, getArea().getAnchor(), weaponDir, level.getUnitsFactory() );
		if(bullet != null)
		{
			level.addUnit( bullet );
			port.use(Resource.Type.ENERGY, def.getShotEnergyConsumption());

		}
		return null;
	}

	// TODO: temp vector
	Vector2 tDirection = new Vector2();

	protected Bullet createBullet(final ISpatialObject target, final Vector2 weaponPos, final Vector2 fireDir, final UnitsFactory unitFactory)
	{
		float angle = weaponDef.createAngle( this, fireDir );

		tDirection.set( 0, 1 );
		tDirection.setAngle( angle );

		float speed = weaponDef.createSpeed();
		Bullet bullet = unitFactory.getUnit( level, weaponDef.getBulletDef(), weaponPos.x, weaponPos.y, angle);


		bullet.weapon = this;
		bullet.getVelocity().set( tDirection ).scl( speed );
		bullet.target = target;
		bullet.angle = angle;


		bullet.lifelen = weaponDef.getBulletLifeDuration();

		bulletsInMagazine --;
		if(bulletsInMagazine > 0)
		{
			timeToReload = weaponDef.getReloadingTime();
		} else
		{
			timeToReload = weaponDef.getMagazineReloadTime();
		}

		return bullet;
	}

	@Override
	public void update(final float delta)
	{
		WeaponDef def = getDef();
		
		timeToReload -= delta;
		
		Resource energyStock = port.get(Resource.Type.ENERGY);
		float requestedResource = port.getCapacity(Resource.Type.ENERGY) - energyStock.getAmount();
		
		float diffAngle =
				(float)( Math.acos( targetOrientation.dot( weaponDir ) )* Angles.TO_DEG);
		if(Math.abs( diffAngle ) < Math.abs( delta * weaponDef.getAngularSpeed() ))
		{
			weaponDir.set( targetOrientation );
		}
		else
		{
			// TODO: create spinor utility?
			int dir = weaponDir.crs( targetOrientation ) > 0 ? 1 : -1;

			float angleDelta = dir * delta * weaponDef.getAngularSpeed();

			float dx = (float) Math.cos(angleDelta * Angles.TO_RAD);
			float dy = (float) Math.sin(angleDelta * Angles.TO_RAD);

			float x = weaponDir.x * dx - weaponDir.y * dy;
			float y = weaponDir.y * dx + weaponDir.x * dy;

			weaponDir.set( x, y ).nor();
		}

//		weaponDir.lerp( targetOrientation, delta * weaponDef.getAngularSpeed() );

//		weaponDir.nor();

		diffAngle =
				(float) (Math.acos( targetOrientation.dot( weaponDir ) )* Angles.TO_DEG);

		float absDistance = Math.abs( diffAngle );

		isOriented = absDistance < weaponDef.getMaxFireAngle();

		this.angle = weaponDir.angle();

	}


	public Level getLevel() { return level; }

	public Vector2 getDirection() { return weaponDir; }

	public boolean isOriented() { return isOriented; }
	public void reload() { bulletsInMagazine = weaponDef.getBurstSize(); }

	public Vector2 getTargetOrientation() { return targetOrientation; }

	public float getTimeToReload() { return timeToReload; }

	public int getBulletsInMagazine() {	return bulletsInMagazine; }

	@Override
	public WeaponDef getDef() { return weaponDef; }

	@Override
	public float getMaxSpeed() { return 0; }

	public Port getPort() { return port; }

	public float getRequiredResource(Type type) {
		float delta = requestedResource - pendingResourceSupply;
		if(delta < 0)
			return 0;
		return delta;
	}

	public void pendResourceProvision(Type type, float amount) {
		pendingResourceSupply += amount;
	}
	public void provisionResource(Type type, float amount) {
		pendingResourceSupply -= amount;
		if(pendingResourceSupply < 0)
			pendingResourceSupply = 0;
	}


}