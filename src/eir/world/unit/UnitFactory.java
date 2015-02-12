package eir.world.unit;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Pool;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitDef;
import eir.world.unit.ai.TaskStage;
import eir.world.unit.behaviors.UnitBehavior;

/**
 * Implementation of specific unit factory should add {@link TaskStage} behaviors by calling
 * {@link BehaviorFactory#registerBehavior(eir.world.unit.ai.TaskStage, UnitBehavior)}
 * @author Fima
 *
 * @param <U>
 */
public abstract class UnitFactory <U extends IUnit>
{
	/**
	 * Pool of units of this type
	 */
	protected Pool <U> pool = new Pool<U> () { @Override
		protected U newObject() { return createEmpty(); } };

	/**
	 * TaskStage -> TaskBehavior mapping
	 */
	protected Map <TaskStage, UnitBehavior <U>> behaviors = new HashMap <TaskStage, UnitBehavior<U>> ();
	
	protected abstract void init( LevelDef levelDef, ResourceFactory factory );
	
	protected abstract U createEmpty();

	/**
	 * Allows mapping extended unit definitions in the level file.
	 * Unit initialization procedure will rely on this type
	 * @param unitType 
	 * @return
	 */
	protected abstract Class <? extends UnitDef> getDefClass(String unitType);
}