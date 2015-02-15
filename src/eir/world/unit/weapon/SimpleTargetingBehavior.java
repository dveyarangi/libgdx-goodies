package eir.world.unit.weapon;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.ai.Task;
import eir.world.unit.behaviors.IUnitBehavior;

public class SimpleTargetingBehavior implements IUnitBehavior<Cannon>
{
	Vector2 tDir = new Vector2();

	@Override
	public void update( final float delta, final Task task, final Cannon unit )
	{

		ISpatialObject target = unit.getTarget();
//		System.out.println("Cannon " + unit + " is targeting " + target);

		if(target == null)
			return;

		tDir.set( target.getArea().getAnchor() );
		Vector2 targetDir = tDir.sub( unit.getArea().getAnchor() ).nor();


//		unit.getWeapon().angle = target.getArea().getAnchor().tmp().sub( unit.getArea().getAnchor() ).angle();
	}

}
