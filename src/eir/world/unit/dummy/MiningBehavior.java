package eir.world.unit.dummy;

import eir.world.unit.ai.Task;
import eir.world.unit.behaviors.UnitBehavior;


/**
 * ant driver when ant is tasked to mine
 * @author Ni
 *
 */
public class MiningBehavior implements UnitBehavior <Ant>
{
	@Override
	public void update(float delta, Task task, Ant ant)
	{
		ant.angle += delta;
	}
}
