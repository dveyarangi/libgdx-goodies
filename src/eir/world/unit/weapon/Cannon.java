package eir.world.unit.weapon;

import eir.rendering.IRenderer;
import eir.rendering.LevelRenderer;
import eir.world.IEffect;
import eir.world.Level;
import eir.world.environment.Anchor;
import eir.world.environment.RelativeAnchor;
import eir.world.environment.sensors.ISensor;
import eir.world.resource.Port;
import eir.world.unit.Unit;
import eir.world.unit.ai.TaskedUnit;
import eir.world.unit.aspects.IBuilding;
import eir.world.unit.damage.Damage;
import eir.world.unit.damage.Hull;

public class Cannon extends TaskedUnit implements IBuilding
{

	public static final int SENSOR_RADIUS = 100;

	private ISensor sensor;

	private Weapon weapon;

	private Damage impactDamage = new Damage( 10, 1, 0, 0 );

	private TargetProvider targetProvider;
	private WeaponDef weaponDef;

	private Anchor weaponMount;

	private float wanderAngle;


	public Cannon( )
	{
		super();

	}

	@Override
	protected void reset( final Level level )
	{
		super.reset( level );
		
		CannonDef def = getDef();

		weaponMount = new RelativeAnchor( this );

		weaponDef = ((CannonDef)this.def).getWeaponDef();


		this.hull = new Hull(500f, 0f, new float [] {0f,0f,0f,0f});

		weapon = level.getUnitsFactory().getUnit( level, weaponDef, weaponMount );
		weapon.getDirection().setAngle( this.angle+90 );
		weapon.getTargetOrientation().setAngle( this.angle+90 );
		wanderAngle = this.angle+90;
		

		this.sensor = level.getEnvironment().createSensor( this, weaponDef.getSensorRadius() );
		
		targetProvider = def.getWeaponDef().createTargetProvider( weapon );
		
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		weapon.getArea().getAnchor().x = cx();
		weapon.getArea().getAnchor().y = cy();
	}

	@Override
	public void draw( final IRenderer renderer )
	{
//		super.draw( renderer );
		weapon.draw( renderer );
	}

	@Override
	public float getSize()
	{
		return 4;
	}

	@Override
	public Weapon getWeapon() { return weapon; }

	@Override
	public Damage getDamage() { return impactDamage; }

	@Override
	public Unit getSource() { return this; }

	@Override
	public float getMaxSpeed() { return 0; }

	@Override
	protected void registerOverlays()
	{
		super.registerOverlays();
		addHoverOverlay( LevelRenderer.WEAPON_OID);
	}

	@Override
	public void setDead()
	{
		super.setDead();
		weapon.setDead();
	}

	@Override
	public IEffect createDeathEffect()
	{
		return weapon.createDeathEffect();
		
	}
	
	@Override
	public Port getPort() { return weapon.getPort(); }

	public ISensor getSensor() { return sensor; }

	public TargetProvider getTargetProvider() { return targetProvider; }


}
