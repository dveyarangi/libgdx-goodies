/**
 *
 */
package eir.world.unit.ant;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.world.unit.UnitsFactory.UnitFactory;
import eir.world.unit.ai.TaskStage;

/**
 * @author dveyarangi
 *
 */
public class AntFactory extends UnitFactory <Ant>
{
	public static final String NAME = "ant".intern();

	@Override protected String getName() { return NAME; }

	public AntFactory( final ResourceFactory gameFactory )
	{
		behaviors.put( TaskStage.TRAVEL_TO_SOURCE, new TravelingBehavior.TravelToSourceBehavior() );
		behaviors.put( TaskStage.TRAVEL_TO_TARGET, new TravelingBehavior.TravelToTargetBehavior() );
		behaviors.put( TaskStage.MINING, new MiningBehavior() );

	}

	@Override
	protected Ant createEmpty() { return new Ant(); }

	@Override
	protected void init(LevelDef def, ResourceFactory factory)
	{
		// TODO Auto-generated method stub
		
	}



}
