package eir.world.unit.ai;

import com.badlogic.gdx.utils.Pool;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.UnitBehavior;



/**
 * task tell an ant what it is supposed to do.
 * @author Ni
 *
 */
public class Task
{
	protected Scheduler scheduler;
	protected Order order;
	protected TaskStage stage;
	
	/**
	 * When task is given according to an order, 
	 * order source and target can change.
	 */
	protected ISpatialObject source, target;

	public static enum Status { ONGOING, COMPLETED, CANCELED };

	protected Status status;

	protected int stageIdx = 0;

	/**
	 * Pool of AABB objects
	 */
	private static final Pool<Task> pool =
			new Pool<Task>()
			{
				@Override
				protected Task newObject()
				{
					return new Task();
				}
			};

	protected static Task create(final Scheduler scheduler, final Order order)
	{
		return pool
				.obtain()
				.update( scheduler, order );
	}

	public static void free(final Task task)
	{
		pool.free( task );
	}

	protected Task()
	{

	}

	protected Task update(final Scheduler scheduler, final Order order)
	{

		this.scheduler = scheduler;
		this.order = order;
		
		this.source = order.getSource();
		this.target = order.getTarget();

		this.stageIdx = 0;
		this.stage = order.getStages()[stageIdx ++];

		this.status = Status.ONGOING;

		return this;
	}

	void free()
	{
		Task.free( this );
	}

	public TaskStage nextStage()
	{
		if(stageIdx >= order.getStages().length)
		{
			if(!order.cycleTask())
			{
				this.setCompleted();
				return null;
			}

			stageIdx = 0;
		}

		return order.getStages() [stageIdx ++];
	}

	public void cancel()
	{
		scheduler.cancelTask( this );

	}

	public Order getOrder()
	{
		return order;
	}

	public void setCanceled()
	{
		status = Status.CANCELED;
		this.target = this.source = null;
	}

	public void setCompleted()
	{
		status = Status.COMPLETED;
		this.source = this.target = null;
	}

	public boolean isFinished()
	{
		return status == Status.CANCELED || status == Status.COMPLETED;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit> UnitBehavior <U> getBehavior(final Unit unit)
	{
		return scheduler.getUnitFactory().<U>getBehavior( unit.getType(), stage );
	}

	public ISpatialObject getSource() {
		if(!source.isAlive())
			source = null;
		return source; }
	public ISpatialObject getTarget() {
		if(!source.isAlive())
			source = null;

		return target; 
	}

}
