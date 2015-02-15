package eir.world.unit.behaviors;

import eir.world.unit.Unit;
import eir.world.unit.ai.Task;

public class LinearTargetedMovement <U extends Unit>implements IUnitBehavior<U>
{
	@Override
	public void update(float delta, Task task, U unit)
	{
		assert unit.target != null;
		
		float dx = - unit.cx() + unit.target.getArea().cx();
		float dy = - unit.cy() + unit.target.getArea().cy();
		float len = (float)Math.sqrt((dx*dx + dy*dy));
		
		float vx = dx / len * unit.getDef().getMaxSpeed();
		float vy = dy / len * unit.getDef().getMaxSpeed();
		
		unit.getArea().getAnchor().x += vx * delta;
		unit.getArea().getAnchor().y += vy * delta;
		
		if(len < 0.001)
		{
			task.nextStage();
			return;
		}
	}


}
