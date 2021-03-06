package eir.world.unit.ai;

import eir.world.Level;
import eir.world.unit.Unit;
import eir.world.unit.aspects.IDamager;
import eir.world.unit.behaviors.IUnitBehavior;
import eir.world.unit.damage.Damage;

public abstract class TaskedUnit extends Unit
{

	private Task task;

	protected TaskedUnit()
	{
		super();
	}

	@Override
	protected void reset( final Level level )
	{
		super.reset( level );
		if(task != null)
			throw new IllegalStateException("Cannot reset tasked unit: task not cleared.");
	}

	@Override
	public void update( final float delta )
	{
		super.update( delta );

		if(task == null || task.isFinished())
		{
			// requesting a new task:
			task = faction.getScheduler().gettaTask( this, task );
		}

		// performing task:
		if(task == null)
			return;
		
		IUnitBehavior behavior = task.getBehavior( this );
		
		if( behavior == null)
			return;

		behavior.update( delta, task, this );


	}

	public Task getTask() { return task; }

	@Override
	public float hit(final Damage source, final IDamager damager, final float damageCoef)
	{
		float damage = super.hit( source, damager, damageCoef );

		if( !this.isAlive() && task != null  )
		{
			task.cancel();
			task = null;
		}

		return damage;
	}

	@Override
	public void dispose(  )
	{	

		if(task != null)
		{
			task.cancel();
		}

		task = null;
		super.dispose(  );

	}

	public void cancelTask()
	{
		task.cancel();
		task = null;
	}
}
