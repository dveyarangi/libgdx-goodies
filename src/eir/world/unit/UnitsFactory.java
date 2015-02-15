/**
 *
 */
package eir.world.unit;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.utils.IdentityMap;

import eir.debug.Debug;
import eir.resources.ResourceFactory;
import eir.resources.levels.IUnitDef;
import eir.resources.levels.LevelDef;
import eir.world.Level;
import eir.world.environment.Anchor;
import eir.world.unit.ai.TaskStage;
import eir.world.unit.behaviors.IUnitBehavior;

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
	public void free(Level level, final IUnit unit)
	{
		unit.dispose();

		UnitFactory <IUnit> factory = (UnitFactory <IUnit>) factories.get( unit.getType() );
		factory.pool.free( unit );
	}

	/**
	 * @param unitType
	 * @return
	 */
	public Class<?> getUnitDefClass(final String unitType)
	{
		return factories.get( unitType ).getDefClass( unitType );
	}


	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit> IUnitBehavior<U> getBehavior(final String unitType, final TaskStage stage)
	{
		IUnitBehavior<U> behavior = (IUnitBehavior<U>) factories.get( unitType ).behaviors.get(stage);
		if(behavior == null)
		{
			Debug.log( "No behaviour for unit type " + unitType + " stage " + stage );
		}
		return behavior;
	}


}