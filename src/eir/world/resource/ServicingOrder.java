package eir.world.resource;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.ai.Order;
import eir.world.unit.ai.Scheduler;
import eir.world.unit.ai.TaskStage;

public class ServicingOrder extends Order <GatheringTask>
{
	
	private Resource.Type type;
	
	private CarrierDef gathererDef;

	public ServicingOrder(CarrierDef gathererDef, Resource.Type type, ISpatialObject source, ISpatialObject target)
	{
		super(new TaskStage [] { TaskStage.TRAVEL_TO_SOURCE,
								 TaskStage.LOAD,
								 TaskStage.TRAVEL_TO_TARGET,
								 TaskStage.UNLOAD},
						false,   // cycling 
						0,       // TODO: priority
						source,  // resource provider
						target   // resource consumer
					);
		
		this.type = type;
		
		this.gathererDef = gathererDef;
	}

	@Override
	public GatheringTask createTask(Unit unit, final Scheduler scheduler)
	{
		
		
		GatheringTask task = super.createTask( unit, scheduler );
		
		IServiceable gatherer = (IServiceable) unit;
		
		float capacity = (float)gatherer.getPort().getCapacity(type);
		if(capacity == 0)
			return null; // TODO: idling task?
		
		IServiceable requester = (IServiceable)this.getTarget();
		
		float requiredResource = requester.getPort().getRequiredResource( type );
		
		if(requiredResource <= 0)
			return null; // idling!
		
		float amountToGather = Math.min(Math.min(gathererDef.getResourceCapacity(), requiredResource),capacity);
		
		requester.getPort().pendResourceProvision( type, amountToGather);
		
		task.update(scheduler, this, type, amountToGather);
		
		return task;
	}

	@Override
	protected GatheringTask createEmptyTask()
	{
		GatheringTask task = new GatheringTask();
		return task;
	}
}
