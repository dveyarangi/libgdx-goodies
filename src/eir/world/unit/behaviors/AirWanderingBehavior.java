/**
 * 
 */
package eir.world.unit.behaviors;

import eir.world.environment.Environment;
import eir.world.environment.nav.NavNode;
import eir.world.unit.Unit;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public class AirWanderingBehavior implements IUnitBehavior <Unit>
{
	
	public NavNode targetNode;
	public Environment env;
	
	public AirWanderingBehavior(Environment env)
	{
		this.env = env;
		
		
	}

	@Override
	public void update(float delta, Task task, Unit unit)
	{
		if(targetNode == null)
		{
			targetNode = env.getClosestAirNode(unit.getBody().getAnchor());
		}
	}

}
