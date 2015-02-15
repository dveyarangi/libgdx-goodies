package eir.world.unit;

import java.util.HashMap;
import java.util.Map;

import eir.world.unit.ai.TaskStage;
import eir.world.unit.behaviors.IUnitBehavior;

public class BehaviorFactory <U extends Unit>
{


	private Map <TaskStage, IUnitBehavior <U>> behaviors = new HashMap <TaskStage, IUnitBehavior<U>> ();

	protected void registerBehavior(final TaskStage stage, final IUnitBehavior <U> behavior)
	{
		behaviors.put( stage, behavior );
	}

	public IUnitBehavior <U> getBehavior(final TaskStage stage)
	{
		return behaviors.get(stage);
	}
}
