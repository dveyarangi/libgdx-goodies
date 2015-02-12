/**
 *
 */
package eir.world.unit.dummy;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitDef;
import eir.world.unit.UnitFactory;
import eir.world.unit.ai.TaskStage;

/**
 * @author dveyarangi
 *
 */
public class AntFactory extends UnitFactory <Ant>
{
	public static final String NAME = "ant".intern();

	public AntFactory( final ResourceFactory gameFactory )
	{
		behaviors.put( TaskStage.TRAVEL_TO_SOURCE, new TravelingBehavior.TravelToSourceBehavior() );
		behaviors.put( TaskStage.TRAVEL_TO_TARGET, new TravelingBehavior.TravelToTargetBehavior() );

	}

	@Override
	protected Ant createEmpty() { return new Ant(); }

	@Override
	protected void init(LevelDef def, ResourceFactory factory)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Class<? extends UnitDef> getDefClass( String unitType ) { return UnitDef.class; }




}
