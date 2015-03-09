package eir.world.unit.ai;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.behaviors.IUnitBehavior;



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


	public static enum Status { ONGOING, COMPLETED, CANCELED };

	protected Status status;

	protected int stageIdx = 0;


	public Task()
	{

	}

	protected Task update(final Scheduler scheduler, final Order order)
	{

		this.scheduler = scheduler;
		this.order = order;

		this.stageIdx = 0;
		this.stage = order.getStages()[stageIdx];

		this.status = Status.ONGOING;

		return this;
	}

	void free()
	{
		order.free( this );
	}

	public TaskStage nextStage()
	{
		stageIdx ++;
		if(stageIdx >= order.getStages().length)
		{
			if(!order.cycleTask())
			{
				this.setCompleted();
				return null;
			}

			stageIdx = 0;
		}
		this.stage = order.getStages()[stageIdx];

		return stage;
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
	}

	public void setCompleted()
	{
		status = Status.COMPLETED;
	}

	public boolean isFinished()
	{
		return status == Status.CANCELED || status == Status.COMPLETED;
	}

	/**
	 * @return
	 */
	public <U extends Unit> IUnitBehavior <U> getBehavior(final Unit unit)
	{
		return scheduler.getBehavior( unit.getType(), stage );
	}

	public ISpatialObject getSource() { return getOrder().getSource(); }
	public ISpatialObject getTarget() { return getOrder().getTarget(); }

	public TaskStage getStage() { return stage; }

}
