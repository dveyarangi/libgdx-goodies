/**
 *
 */
package eir.world.unit.behaviors;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public class PulsingTargetedBehavior <U extends Unit> implements IUnitBehavior <U>
{

	private IPulseDef pulseDef;

	private Vector2 t = new Vector2();

	private static final float VISCOSITY = 0.1f;

	public PulsingTargetedBehavior( final IPulseDef pulseDef)
	{
		this.pulseDef = pulseDef;
	}

	@Override
	public void update(final float delta, final Task task, final U unit)
	{
		if(unit.timeToPulse <= 0)
		{

			unit.timeToPulse = pulseDef.getPulseDuration();
			unit.target = task.getTarget();

			ISpatialObject targetObject = unit.getTarget();

			Vector2 target = targetObject.getArea().getAnchor();

			t.set( target ).sub( unit.getBody().getAnchor() );

			float dist = t.len();
			t.x += RandomUtil.R( dist ) - dist/2;
			t.y += RandomUtil.R( dist ) - dist/2;

			t.scl( pulseDef.getPulseStrength() / dist);

			unit.getVelocity().add( t );


			unit.angle = unit.getVelocity().angle();
		}

		Vector2 velocityNor = t;
		float speed = unit.getVelocity().len();
		t.set( unit.getVelocity() ).scl( speed );


		if(speed > unit.getMaxSpeed())
		{
			unit.getVelocity().scl(unit.getMaxSpeed() / speed );
		}

		speed = Math.min( speed, unit.getMaxSpeed() );

		t.set( unit.getVelocity() ).scl( delta );

		unit.getArea().getAnchor().add( t );

		if(speed == 0 || speed < pulseDef.getPulseDecay() * delta)
		{
			unit.getVelocity().set(0,0);
		} else
		{
			unit.getVelocity().sub(velocityNor.scl( pulseDef.getPulseDecay() * delta ));
		}

		unit.timeToPulse -= delta;

	}

}
