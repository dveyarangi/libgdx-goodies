package eir.world.unit.weapon;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitDef;
import eir.world.unit.UnitFactory;

public class CannonFactory extends UnitFactory <Cannon>
{
	public static final String NAME = "cannon".intern();

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
	protected Class <? extends UnitDef> getDefClass( String unitType ) {	return CannonDef.class;	}

	@Override
	protected void init(LevelDef def, ResourceFactory factory)
	{
	}

}
