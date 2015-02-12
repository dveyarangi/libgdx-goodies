package eir.world.resource;

import eir.resources.levels.UnitDef;

public class CarrierDef extends UnitDef
{
	private float resourceCapacity;
	

	public CarrierDef(String type, int faction, float size,
			boolean isPickable, float resourceCapacity, float maxSpeed)
	{
		super(type, faction, size, isPickable, maxSpeed);
		
		this.resourceCapacity = resourceCapacity;
	}

	public float getResourceCapacity() { return resourceCapacity; }
}
