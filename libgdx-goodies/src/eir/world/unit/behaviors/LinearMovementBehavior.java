package eir.world.unit.behaviors;

import yarangi.math.Angles;
import eir.world.unit.Unit;
import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;

public class LinearMovementBehavior <U extends Unit> implements UnitBehavior <U>
{

	
	public LinearMovementBehavior()
	{

	}

	@Override
	public void update(float delta, Task task, U unit)
	{
//		System.out.println(unit.angle);
		
		float unitAngle = (float) (unit.angle*Angles.TO_RAD);
		
		float dx = Angles.COS( unitAngle ) * unit.getMaxSpeed();
		float dy = Angles.SIN( unitAngle ) * unit.getMaxSpeed();
		
		unit.getVelocity().set( dx, dy );
		
		unit.getArea().getAnchor().add( dx * delta, dy * delta );
	}

}
