package eir.world.unit.cannons;

import eir.resources.levels.UnitDef;
import eir.world.unit.UnitsFactory.UnitFactory;

public class CannonFactory extends UnitFactory <Cannon>
{
	public static final String NAME = "cannon".intern();

	@Override protected String getName() { return NAME; }

	public CannonFactory( )
	{

//		behaviors.put( TaskStage.ATTACK, new LinearTargetingBehavior() );
	}

	@Override
	protected Cannon createEmpty()
	{
		return new Cannon();
	}

	@Override
	protected Class <? extends UnitDef> getDefClass() {	return CannonDef.class;	}
}
