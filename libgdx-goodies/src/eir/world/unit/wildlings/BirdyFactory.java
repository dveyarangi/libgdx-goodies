/**
 *
 */
package eir.world.unit.wildlings;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.world.unit.UnitsFactory.UnitFactory;
import eir.world.unit.ai.TaskStage;
import eir.world.unit.behaviors.IPulseDef;
import eir.world.unit.behaviors.PulsingTargetedBehavior;

/**
 * @author dveyarangi
 *
 */
public class BirdyFactory extends UnitFactory <Birdy>
{
	public static final String NAME = "birdy".intern();

	@Override protected String getName() { return NAME; }

	public BirdyFactory()
	{
		behaviors.put( TaskStage.GUARD, new PulsingTargetedBehavior( new IPulseDef(0.5f, 10, 3)) );
		behaviors.put( TaskStage.ATTACK, new PulsingTargetedBehavior( new IPulseDef(0.3f, 20, 3)) );
	}

	@Override
	protected Birdy createEmpty() { return new Birdy(); }

	@Override
	protected void init(LevelDef def, ResourceFactory factory)
	{
		// TODO Auto-generated method stub
		
	}

}