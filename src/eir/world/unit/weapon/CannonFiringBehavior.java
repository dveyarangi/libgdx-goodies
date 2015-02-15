package eir.world.unit.weapon;

import java.util.List;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.ai.Task;
import eir.world.unit.behaviors.IUnitBehavior;

public class CannonFiringBehavior implements IUnitBehavior <Cannon>
{
	
	public CannonFiringBehavior()
	{
	}

	@Override
	public void update(float delta, Task task, Cannon unit)
	{
		List <ISpatialObject> units = unit.getSensor().sense( unit.getFaction().getEnemyFilter() );
		Weapon weapon = unit.getWeapon();
		ISpatialObject target = unit.getTarget();
		WeaponDef def = weapon.getDef();

		if(weapon.getBulletsInMagazine() == 0)
		{
			weapon.reload();
		}

//		if(target == null || !target.isAlive() )
//		{
//		}


		if(target == null || !target.isAlive())
			target = unit.getTargetProvider( ).pickTarget( units );
		weapon.target = target;
		Vector2 targetDirection = def.getTargetingModule().getShootingDirection( target, unit );
		if(targetDirection != null)
		{
			weapon.getTargetOrientation().set( targetDirection ).nor();
		}
		else
		{	// no target, some meaningless behavior:
			if(RandomUtil.oneOf( 200 ))
			{
				float wanderAngle = RandomUtil.getRandomFloat(360 );
				weapon.getTargetOrientation().setAngle( wanderAngle );
			}
		}

		weapon.update( delta );

		if(! weapon.isOriented() )
			return;

		if( target != null)
		{

			if(weapon.getTimeToReload() > 0)
				return;
			weapon.fire( target );

			unit.target = null;
		}
	}

}
