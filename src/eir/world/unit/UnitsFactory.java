/**
 *
 */
package eir.world.unit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Pool;

import eir.debug.Debug;
import eir.resources.ResourceFactory;
import eir.resources.levels.IUnitDef;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitDef;
import eir.world.Level;
import eir.world.environment.Anchor;
import eir.world.unit.ai.TaskStage;

/**
 * Manages unit instantiation and pooling.
 *
 * @author dveyarangi
 *
 */
public class UnitsFactory
{

	private IdentityMap <String, UnitFactory<? extends Unit>> factories = new IdentityMap <String, UnitFactory <? extends Unit>> ();
	Set <UnitFactory> factoriesSet = new HashSet <UnitFactory> ();

	
	LevelDef levelDef;
	
	public UnitsFactory(LevelDef levelDef, final ResourceFactory gameFactory, Map <String, UnitFactory<? extends Unit>> factories)
	{
		
		this.levelDef = levelDef;

		for(String unitType : factories.keySet())
		{
			UnitFactory factory = factories.get( unitType );
			
			factoriesSet.add( factory);
			
			this.factories.put(unitType.intern(), factory);
			
		}

		for(UnitFactory factory : factoriesSet)
		{
			factory.init( levelDef, gameFactory );
		}
	}
	

	/**
	 * Creates unit of specified type, with position set to the anchor.
	 * @param type
	 * @param anchor
	 * @param faction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit, F extends Faction> U getUnit(final Level level, final IUnitDef def, final Anchor anchor)
	{
		UnitFactory <U> fctory = (UnitFactory <U>) factories.get( def.getType() );
		if( fctory == null )
			throw new RuntimeException("Unit factory for " + def.getType() + " is not registered.");
		U unit = fctory.pool.obtain();

		unit.init(level, def, anchor);


		return unit;
	}

	/**
	 * Creates unit of specified type, with position set at (x,y) and null anchor.
	 * @param type
	 * @param anchor
	 * @param faction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit, F extends Faction> U getUnit( final Level level, final IUnitDef def, final float x, final float y, final float angle)
	{
		UnitFactory <U> factory = (UnitFactory <U>) factories.get( def.getType() );
		if( factory == null )
			throw new RuntimeException("No factory for unit type " + def.getType() );
		U unit = factory.pool.obtain();

		unit.init( level, def, x, y, angle );

		unit.angle = angle;

		return unit;
	}


	/**
	 * Returns unit to pool.
	 *
	 * @param ant
	 */
	@SuppressWarnings("unchecked")
	public void free(final IUnit unit)
	{
		unit.dispose();

		UnitFactory <IUnit> factory = (UnitFactory <IUnit>) factories.get( unit.getType() );
		factory.pool.free( unit );
	}

	/**
	 * Implementation of specific unit factory should add {@link TaskStage} behaviors by calling
	 * {@link BehaviorFactory#registerBehavior(eir.world.unit.ai.TaskStage, UnitBehavior)}
	 * @author Fima
	 *
	 * @param <U>
	 */
	public static abstract class UnitFactory <U extends IUnit>
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
	
		protected abstract String getName();
		
		protected abstract U createEmpty();

		/**
		 * Allows mapping extended unit definitions in the level file.
		 * Unit initialization procedure will rely on this type
		 * @return
		 */
		protected Class <? extends UnitDef> getDefClass()
		{
			return UnitDef.class;
		}
	}


	/**
	 * @param unitType
	 * @return
	 */
	public Class<?> getUnitDefClass(final String unitType)
	{
		return factories.get( unitType ).getDefClass();
	}


	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit> UnitBehavior<U> getBehavior(final String unitType, final TaskStage stage)
	{
		UnitBehavior<U> behavior = (UnitBehavior<U>) factories.get( unitType ).behaviors.get(stage);
		if(behavior == null)
		{
			Debug.log( "No behaviour for unit type " + unitType + " stage " + stage );
		}
		return behavior;
	}


}