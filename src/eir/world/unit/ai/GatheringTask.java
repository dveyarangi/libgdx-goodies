package eir.world.unit.ai;

import eir.world.resource.IServiceable;
import eir.world.resource.Port;
import eir.world.resource.Resource;


/**
 * This class represents a resource transaction order.
 * 
 * @author dveyarangi
 */
public class GatheringTask extends Task
{

	/**
	 * Amount of resource to be moved according to this order.
	 */
	private float orderedAmount;
	
	/**
	 * Type of resource to be moved according to this order.
	 */
	private Resource.Type orderedType;
	
	/**
	 * Amount of resource exported from provider by this order at current order state.
	 * Increases during export procedure
	 */
	public float exportedAmount;
	
	/**
	 * Amount of resource imported to requester by this order at current order state.
	 * Increases during import procedure
	 */
	public float importedAmount;
	
	public IServiceable provider, requester;

	public GatheringTask()
	{

	}

	public Task update(final Scheduler scheduler, final Order order, Resource.Type type, float amount)
	{
		super.update(scheduler, order);
		
		this.provider = (IServiceable)order.getSource();
		this.requester = (IServiceable)order.getTarget();
		exportedAmount = 0;
		importedAmount = 0;
		
		orderedAmount = amount;
		orderedType = type;
		
		return this;
	}

	public boolean sealExport(IServiceable carrier, float time)
	{
		Port exPort = provider.getPort();
		Port imPort = carrier.getPort();
		
		float importerCapacity = imPort.getCapacity( orderedType );
		Resource importStock = imPort.get( orderedType );
		float importerSpace = importerCapacity - importStock.getAmount();
		
		Resource exportStock = exPort.get( orderedType );
		
		float transferedAmount = Math.min( // keep in mind that transfere rate is from importer
										Math.min( time*imPort.getTransferRate(), orderedAmount),
										Math.min( importerSpace, exportStock.getAmount() ));
		
		Resource exportedResource = exportStock.consume( transferedAmount, true );
		if(transferedAmount == 0)
			return true; // TODO: this may cause half /loaded/unloaded gatherers to leave loading/unloading
		if(exportedResource == null)
			return true;
		
//		importStock.supply( transferedAmount );
		imPort.provisionResource( exportedResource );

		exportedAmount += transferedAmount; 
		if(exportedAmount+0.0000001 < orderedAmount)
			return false;

		return true;
	}

	public double getOrderedAmount()
	{
		return orderedAmount;
	}

	public boolean sealImport(IServiceable carrier, float time)
	{
		Port exPort = carrier.getPort();
		Port imPort = requester.getPort();
		
		float importerCapacity = imPort.getCapacity( orderedType );
		Resource importStock = imPort.get( orderedType );
		float importerSpace = importerCapacity - importStock.getAmount();
		
		Resource exportStock = exPort.get( orderedType );
		float transferedAmount = Math.min( // keep in mind that transfer rate is from exporter
										Math.min( time*exPort.getTransferRate(), orderedAmount),
										Math.min( importerSpace, exportStock.getAmount() ));
		
		Resource exportedResource = exportStock.consume( transferedAmount, true );
		if(transferedAmount == 0)
			return true; // TODO: this may cause half /loaded/unloaded gatherers to leave loading/unloading
		if(exportedResource == null)
			return true;
		
		imPort.provisionResource( exportedResource );
		
		importedAmount += transferedAmount; 
		if(importedAmount+0.001 < orderedAmount)
			return false;

		return true;
	}
	
	@Override
	public boolean isFinished()
	{
		return super.isFinished() || !getSource().isAlive() || !getTarget().isAlive();
	}

}
