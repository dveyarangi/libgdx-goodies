package eir.world.resource;

import eir.world.resource.Resource.Type;


public interface IServiceable
{
	public Port getPort();

	public float getRequiredResource(Type type);

	public void pendResourceProvision(Type type, float amountToGather);
}
