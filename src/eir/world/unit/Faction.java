/**
 *
 */
package eir.world.unit;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import eir.resources.levels.FactionDef;
import eir.resources.levels.UnitDef;
import eir.world.Level;
import eir.world.controllers.IController;
import eir.world.environment.Anchor;
import eir.world.environment.Environment;
import eir.world.environment.sensors.ISensingFilter;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.ai.Scheduler;

/**
 * Player faction
 *
 */
public class Faction
{

	/////////////////////////////////////////////////////
	// loaded by LevelLoader
	//

	/**
	 * unit owner for hits resolving
	 */
	private FactionDef def;


	public Animation antAnimation;
	/////////////////////////////////////////////////////
	//

	public Level level;


	public Set <IUnit> units;
	public Multimap <String, IUnit> unitsByTypes;

	private IController controller;

	private Scheduler scheduler;

	private ISensingFilter enemyFilter = new ISensingFilter() {

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			if( ! ( entity instanceof IUnit ) )
				return false;

			Unit unit = (Unit) entity;

			return isEnemy( unit );
		}

	};


	public Faction()
	{

		this.units = new HashSet <IUnit> ();
		this.unitsByTypes = HashMultimap.create ();
	}

	public void init(final Level level, final FactionDef def)
	{

		this.def = def;
		this.level = level;

		this.controller = level.getController( def.getOwnerId() );
		scheduler = new Scheduler( level.getUnitsFactory() );


	}

	public int getOwnerId()	{ return def.getOwnerId(); }

	public void addUnit( final IUnit unit )
	{
		units.add( unit );
		unitsByTypes.put( unit.getType(), unit );
		controller.unitAdded( unit );
	}

	public void removeUnit( final IUnit unit )
	{
		units.remove( unit );
		unitsByTypes.remove( unit.getType(), unit );
	}

	public Collection <IUnit> getUnitsByType( final String unitType )
	{
		return unitsByTypes.get( unitType );
	}

	public void update( final float delta )
	{
	}

	/**
	 * @return
	 */
	public Animation getAntAnimation()
	{
		return antAnimation;
	}


	public Scheduler getScheduler() { return scheduler; }

	public IController getController() { return controller; }

	public Set <IUnit> getUnits() {
		return units;
	}

	public Level getLevel() { return level; }
	/**
	 * Equivalent of {@link #getLevel()#getEnvironment()}
	 * @return
	 */
	public Environment getEnvironment() { return level.getEnvironment(); }

	public Unit createUnit( final UnitDef def, final Anchor anchor )
	{
		Unit unit = level.getUnitsFactory().getUnit( level, def, anchor );
		getLevel().addUnit( unit );

		return unit;
	}

	public boolean isEnemy( final Unit unit )
	{
		return this.def.getEnemies().contains( unit.getFaction().getId() );
	}

	public ISensingFilter getEnemyFilter() { return enemyFilter; }

	public Color getColor() { return def.getColor(); }

	public int getId() { return def.getOwnerId(); }

	public FactionDef getDef() { return def;  }

}
