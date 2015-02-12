package eir.world.unit.weapon;

import eir.world.unit.MultiplexUnitFactory;

public abstract class WeaponFactory extends MultiplexUnitFactory <Weapon>
{
	public static final String NAME = "weapon".intern();

	public WeaponFactory( )
	{

//		behaviors.put( TaskStage.ATTACK, new LinearTargetingBehavior() );
	}

	@Override
	protected Weapon createEmpty()
	{
		return new Weapon();
	}

}
