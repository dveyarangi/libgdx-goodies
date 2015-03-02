package eir.world.unit.ai;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.resource.CarrierDef;
import eir.world.resource.IServiceable;
import eir.world.resource.Resource;
import eir.world.unit.Unit;

public class ServicingOrder extends Order <GatheringTask>
{
	
	private Resource.Type type;
	
	private CarrierDef gathererDef;

	public ServicingOrder(CarrierDef gathererDef, Resource.Type type, ISpatialObject source, ISpatialObject target)
	{
		super(new TaskStage [] { TaskStage.TRAVEL_TO_SOURCE,
								 TaskStage.LOAD,
								 TaskStage.TRAVEL_TO_TARGET,
								 TaskStage.UNLOAD,
								 TaskStage.IDLE, },
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
		
		float capacity = gatherer.getPort().getCapacity(type);
		if(capacity == 0)
			return null; // TODO: idling task?
		
		IServiceable requester = (IServiceable)this.getTarget();
		
		float requiredResource = requester.getPort().getRequiredResource( type );
		
		if(requiredResource <= 0)
			return null; // idling!
		
		if(requiredResource < gathererDef.getResourceCapacity())
			return null;
		
		float amountToGather = Math.min(gathererDef.getResourceCapacity(), requiredResource);
		
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
