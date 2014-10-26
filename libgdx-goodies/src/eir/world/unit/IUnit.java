package eir.world.unit;

import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;

public interface IUnit
{
	/**
	 * Unit collision box and anchor point
	 * @return
	 */
	public AABB getArea();

	/**
	 * Unit orientation
	 * @return
	 */
	public float getAngle();


	public Hull getHull();

	public ISpatialObject getTarget();

	public float getSize();

	public Faction getFaction();
}
