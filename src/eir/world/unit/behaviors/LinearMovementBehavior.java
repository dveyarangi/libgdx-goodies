package eir.world.unit.behaviors;

import yarangi.math.Angles;

import com.badlogic.gdx.math.MathUtils;

import eir.world.unit.Unit;
import eir.world.unit.ai.Task;

public class LinearMovementBehavior <U extends Unit> implements IUnitBehavior <U>
{
	private boolean toTarget;
	
	public LinearMovementBehavior(boolean toTarget)
	{
		this.toTarget = toTarget;
	}

	@Override
	public void update(float delta, Task task, U unit)
	{
//		System.out.println(unit.angle);
		
		float unitAngle = unit.angle*Angles.TO_RAD;
		
		float dx = MathUtils.cos( unitAngle ) * unit.getMaxSpeed();
		float dy = MathUtils.sin( unitAngle ) * unit.getMaxSpeed();
		
		unit.getVelocity().set( dx, dy );
		
		unit.getArea().getAnchor().add( dx * delta, dy * delta );
		
		
	}

}
