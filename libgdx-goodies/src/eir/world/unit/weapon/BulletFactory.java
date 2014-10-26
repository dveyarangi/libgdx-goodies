package eir.world.unit.weapon;

import eir.world.unit.UnitsFactory.UnitFactory;

public class BulletFactory extends UnitFactory <Bullet>
{
	
	public static final String NAME = "bullet".intern();

	@Override
	protected String getName() { return NAME; }

	@Override
	protected Bullet createEmpty() { return new Bullet(); }

}
