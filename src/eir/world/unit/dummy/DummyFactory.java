package eir.world.unit.dummy;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitDef;
import eir.world.unit.UnitFactory;

public class DummyFactory extends UnitFactory <Dummy>
{
	public static final String NAME = "dummy".intern();

	@Override
	protected Dummy createEmpty()
	{
		return new Dummy();
	}

	@Override
	protected void init(LevelDef def, ResourceFactory factory)
	{
	}
	@Override
	protected Class<? extends UnitDef> getDefClass( String unitType ) { return UnitDef.class; }
}
