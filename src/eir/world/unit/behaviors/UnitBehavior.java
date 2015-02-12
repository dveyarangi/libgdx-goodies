package eir.world.unit.behaviors;

import eir.world.unit.ai.Task;

/**
 * not sure what this one should be doing
 * @author Ni
 *
 */
public interface UnitBehavior <U>
{
	public void update( float delta, Task task, U unit );
}
