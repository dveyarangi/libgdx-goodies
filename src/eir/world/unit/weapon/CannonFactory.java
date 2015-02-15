package eir.world.unit.weapon;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitDef;
import eir.world.unit.UnitFactory;
import eir.world.unit.ai.TaskStage;
import eir.world.unit.behaviors.LinearTargetedMovement;

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
		
		behaviors.put( TaskStage.ATTACK, new CannonFiringBehavior() );
		behaviors.put( TaskStage.TRAVEL_TO_TARGET, new LinearTargetedMovement<Cannon>( ) );

	}

}
