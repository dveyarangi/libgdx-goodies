package eir.world.unit.weapon;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.world.unit.UnitsFactory.UnitFactory;

public class BulletFactory extends UnitFactory <Bullet>
{
	
	public static final String NAME = "bullet".intern();

	@Override
	protected String getName() { return NAME; }

	@Override
	protected Bullet createEmpty() { return new Bullet(); }

	@Override
	protected void init(LevelDef def, ResourceFactory factory)
	{
	}

}
