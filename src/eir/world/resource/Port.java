package eir.world.resource;

import java.util.HashMap;
import java.util.Map;

import eir.world.resource.Resource.Type;

/**
 * 
 * Multibin resource stack with dynamic capacity and resource amount.
 * 
 * @author dveyarangi
 *
 */
public class Port
{
	/**
	 * @deprecated Test only, not really infinite
	 * @return
	 */
	@Deprecated
	public static Port createEndlessPort()
	{
		Port port = new Port();
		for(Resource.Type type : Resource.Type.values())
		{
			port.capacity.put( type, Float.MAX_VALUE );
			port.stock.put(type, new Resource(type, Float.MAX_VALUE));
			port.pendingProvision.put( type, 0f );
		}
		
		return port;
	}
	
	public static Port createEmptyPort()
	{
		Port port = new Port();
		for(Resource.Type type : Resource.Type.values())
		{
			port.capacity.put( type, 0f );
			port.stock.put(type, new Resource(type, 0));
			port.pendingProvision.put( type, 0f );
		}
		
		return port;
	}
	
	private final Map <Resource.Type, Float> pendingProvision = new HashMap <Resource.Type, Float> ();
	private final Map <Resource.Type, Float> capacity = new HashMap <Resource.Type, Float> ();
	private final Map <Resource.Type, Resource> stock = new HashMap <Resource.Type, Resource> ();
	
	private float transferRate = 1;
	
	public void setTransferRate(float rate) { this.transferRate = rate; }
	
//	void put(Resource resource) { stock.put( resource.type, resource ); }
	public Resource get(Resource.Type type) { return stock.get( type ); }
	
	public void setCapacity(Resource.Type type, float amount, float maxAmount) { 
		capacity.put( type, maxAmount );
		stock.put( type, new Resource(type, amount) );
	}
	public float getCapacity(Resource.Type type) { return capacity.get(type); }

	public float use(Resource.Type type, float amount)
	{
		Resource resource = get(type);
		if(resource == null)
			return 0;
		
		Resource consumedResource =  resource.consume( amount, false );
		return consumedResource == null ? 0 : consumedResource.getAmount(); 
	}
	
	public float getTransferRate() { return transferRate; }

	
	public float getSaturation(Resource.Type type) {
		return (float)(get( type ).getAmount() / getCapacity( type ));
	}
	

	public float getRequiredResource(Type type)
	{
		return pendingProvision.get( type );

	}

	public void pendResourceProvision(Type type, float amount)
	{
		pendingProvision.put( type, pendingProvision.get( type ) + amount );
	}

	public float provisionResource(Resource resource)
	{
		
		Resource typestock = stock.get(resource.type);
		float provisionedAmount = Math.min(
				capacity.get(resource.type) - typestock.getAmount(),
				resource.getAmount());
				
		
		typestock.supply( provisionedAmount );
		
		float pendingAmount = pendingProvision.get( resource.type );
		float newPendingAmount = pendingAmount - provisionedAmount;
		if(newPendingAmount < 0)
			newPendingAmount = 0;

		pendingProvision.put( resource.type, newPendingAmount );
		
		return provisionedAmount;
	}

}
