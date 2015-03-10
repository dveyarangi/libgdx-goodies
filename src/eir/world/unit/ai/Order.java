package eir.world.unit.ai;

import com.badlogic.gdx.utils.Pool;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;

/**
 * general order given by user or ai controller
 * @author Ni
 *
 */
public abstract class Order <T extends Task>
{
	/**
	 * Pool of AABB objects
	 */
	private final Pool<T> pool =
			new Pool<T>()
			{
				@Override
				protected T newObject()
				{
					return createEmptyTask();
				}
			};

	private boolean isActive = true;

	private String unitType;

	private ISpatialObject source;
	private ISpatialObject target;

	private float priority;

	private TaskStage [] stages;

	private boolean cycleStages;

	public Order(final TaskStage [] stages, final boolean cycleStages, final float priority, final ISpatialObject source, final ISpatialObject target)
	{
		this.stages = stages;
		this.cycleStages = cycleStages;

		this.priority = priority;

		this.source = source;
		this.target = target;

	}
	protected abstract T createEmptyTask();

	public T createTask(Unit unit, final Scheduler scheduler)
	{
		T task = pool.obtain();
		
		task.update(scheduler, this);
		
		return task;
	}

	public void free(final T task)
	{
		pool.free( task );
	}


	/**
	 * @return
	 */
	public ISpatialObject getSource() { return source;	}
	public ISpatialObject getTarget() { return target;	}

	public void setActive(final boolean isActive) { this.isActive = isActive; }

	public float getPriority() { return priority; }
	public String getUnitType() { return unitType; }

	public boolean isActive() { return isActive; }

	public TaskStage[] getStages() { return stages; }

	public boolean cycleTask() { return cycleStages; }

}
