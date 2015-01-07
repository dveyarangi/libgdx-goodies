package eir.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import eir.game.LevelSetup;
import eir.rendering.IRenderer;
import eir.resources.ResourceFactory;
import eir.resources.levels.FactionDef;
import eir.resources.levels.IUnitDef;
import eir.resources.levels.LevelDef;
import eir.resources.levels.LevelInitialSettings;
import eir.resources.levels.UnitAnchorDef;
import eir.resources.levels.UnitDef;
import eir.world.controllers.ControllerFactory;
import eir.world.controllers.IController;
import eir.world.environment.Anchor;
import eir.world.environment.Asteroid;
import eir.world.environment.Environment;
import eir.world.environment.IBackground;
import eir.world.environment.Web;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.unit.Faction;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Level
{
	////////////////////////////////////////////////////////////////
	// level physical environment:

	private LevelDef def;

	private ResourceFactory gameFactory;
	
	private ControllerFactory controllerFactory;

	private float halfWidth, halfHeight;
	
	private ILevelData data;

	/**
	 * Level spatial and navigational environment
	 */
	private Environment environment;

	////////////////////////////////////////////////////////////////
	// renderables
	/**
	 * Level background images
	 */
	private IBackground background;
	////////////////////////////////////////////////////////////////

	/**
	 * List of asteroids
	 */
	private Map <String, Asteroid> asteroids;

	/**
	 * Participating factions
	 */
	private TIntObjectHashMap<Faction> factions;

	/**
	 * Set of units in game
	 * TODO: swap to identity set?
	 */
	private final LinkedList <IUnit> units;

	/**
	 * List of webs
	 */
	private List <Web> webs;

	////////////////////////////////////////////////////////////////

	/**
	 * Units to add queue
	 */
	private final Queue <IUnit> unitsToAdd = new LinkedList <IUnit> ();

	private final Queue <IUnit> unitsToRemove = new LinkedList <IUnit> ();

	private int highestUnitZ = Integer.MIN_VALUE;

	private UnitsFactory unitsFactory;
	
	private List <Effect> pendingEffects = new ArrayList <Effect>();
	
	private int PROFILE_INTERVAL = 2;
	private float timeSinceProfiling = 0;
	
	

	public Level( LevelSetup setup )
	{
		this.unitsFactory = setup.getUnitsFactory();
		this.gameFactory = setup.getGameFactory();
		this.controllerFactory = setup.getControllerFactory();
		asteroids = new HashMap <String, Asteroid> ();
		Comparator <IUnit> comparator = new Comparator <IUnit> () {

			@Override
			public int compare(IUnit u1, IUnit u2) { return u1.z() - u2.z(); }
			
		};
		
		// TODO: (optimization) linked listify
		units = new LinkedList <IUnit > ();
		webs = new ArrayList <Web> ();
		factions = new TIntObjectHashMap<Faction> ();
		
		this.background = setup.getLevelDef().getBackgroundDef();
	}



	public Collection <Asteroid> getAsteroids() { return asteroids.values(); }
	public Asteroid getAsteroid( final String name ) { return asteroids.get( name ); }

	public List <Web> getWebs() { return webs; }

	public List <IUnit> getUnits() { return units; }

	/**
	 * @return
	 */
	public float getHeight() { return def.getHeight(); }
	/**
	 * @return
	 */
	public float getWidth() { return def.getWidth(); }

	/**
	 * @return
	 */
	public float getHalfHeight() { return halfHeight; }
	/**
	 * @return
	 */
	public float getHalfWidth() { return halfWidth; }
	
	public IUnit addUnit(Level level, UnitDef def, Anchor anchor)
	{
		IUnit unit = unitsFactory.getUnit(level, def, anchor);
		addUnit( unit );
		return unit;
	}
	public IUnit addUnit(Level level, UnitDef def,float x, float y, float a)
	{
		IUnit unit = unitsFactory.getUnit(level, def, x, y, a);
		addUnit( unit );
		return unit;
	}

	/**
	 * @param startingNode
	 */
	public IUnit addUnit(final IUnit unit)
	{
		debug("Unit added: " + unit);
		unitsToAdd.add(unit);
		unit.getFaction().addUnit( unit );

		return unit;
	}

	/**
	 * @param context
	 * @param factory
	 */
	public void init(final LevelDef def )
	{
		this.def = def;

		halfWidth = def.getWidth() / 2;
		halfHeight = def.getHeight() / 2;

		this.environment = new Environment();

		this.background = def.getBackgroundDef();

		environment.init( this );
		
		for( FactionDef factionDef : def.getFactionDefs() )
		{
			Faction faction = new Faction();
			faction.init( this, factionDef );

//			faction.getScheduler().addOrder( UnitsFactory.ANT, new RandomTravelingOrder( environment, 0 ) );
			// TODO: wrong place for this
			factions.put( factionDef.getOwnerId(), faction );
		}
		
		controllerFactory.init( this );


		Vector2 coord = new Vector2();

		for( Web web : webs )
		{
			web.init( gameFactory, this );
		}

		for(IUnitDef unitDef : def.getUnitDefs())
		{
			Unit unit;
			UnitAnchorDef anchorDef = unitDef.getAnchorDef();
			if(anchorDef != null)
			{
				Asteroid asteroid = getAsteroid( anchorDef.getAsteroidName() );
	
				SurfaceNavNode anchor = asteroid.getNavNode();
				unit = getUnitsFactory().getUnit( this, unitDef, anchor );
			}
			else
				unit = getUnitsFactory().getUnit( this, unitDef, unitDef.getPosition().x, unitDef.getPosition().y, 0 );
			

			addUnit(unit);
		}
		
		background.init( this );

//		this.playerUnit = new Spider( unitsFactory );

		//		addUnit(playerUnit);



		// nav mesh initiated after this point
		////////////////////////////////////////////////////

	}




	/**
	 * @param delta
	 */
	public void update(final float delta)
	{
		////////////////////////////////////////////////////////
		if(timeSinceProfiling > PROFILE_INTERVAL)
		{
			assert log("Level update: active units: " + units.size());
			
			timeSinceProfiling = 0;
		}
		
		timeSinceProfiling += delta;
		////////////////////////////////////////////////////////
		
		controllerFactory.update( delta );

		////////////////////////////////////////////////////////
		reassesUnits();
		
		////////////////////////////////////////////////////////
		data.beforeUpdate(delta);

		////////////////////////////////////////////////////////
		for(int fid : factions.keys())
		{
			Faction faction = factions.get( fid );
			faction.update( delta );
		}

		////////////////////////////////////////////////////////
		// TODO: (optimize) a-ha!
		Iterator <IUnit> unIt = units.descendingIterator();
		while(unIt.hasNext())
		{
			IUnit unit = unIt.next();

			if(unit.isAlive()) // unit may already be dead from previous hits
			{
				// updating position:
				unit.update(delta);
				environment.update( unit );
			}

			if(!unit.isAlive() ||
					!inWorldBounds(unit.getArea().getAnchor())) // unit may be dead from the collision
			{
				//addEffect(unit.createDeathEffect( ));
				unitsToRemove.add ( unit );
			}

		}

		////////////////////////////////////////////////////////
		environment.update( delta );
		
		background.update( delta );
		
		////////////////////////////////////////////////////////
		data.afterUpdate(delta);

	}

	public void draw(IRenderer levelRenderer)
	{
		for(Effect effect : pendingEffects)
		{
			levelRenderer.addEffect( effect );
		}
		
		pendingEffects.clear();

	}

	public boolean inWorldBounds(final Vector2 position)
	{
		return position.x < halfWidth  && position.x > -halfWidth
				&& position.y < halfHeight && position.y > -halfHeight;
	}

	/**
	 * @param sourceNode
	 * @param targetNode
	 */
/*	public void toggleWeb(final SurfaceNavNode sourceNode, final SurfaceNavNode targetNode)
	{

		if(sourceNode.getDescriptor().getObject() == targetNode.getDescriptor().getObject())
			return;

		NavEdge <SurfaceNavNode> edge = environment.getGroundMesh().getEdge( sourceNode, targetNode );

		if(edge == null)
		{
			Web web = new Web(sourceNode, targetNode,
					"models/web_thread_01.png",
					"models/web_source_01.png",
					"models/web_target_01.png"
					);

			webs.add( web );
			web.init( gameFactory, this );



		}
		else
		{
			System.out.println("removing web: " + sourceNode + " <---> " + targetNode);
			Iterator <Web> webIt = webs.iterator();
			while(webIt.hasNext())
			{
				Web web = webIt.next();
				if(web.getSource() == sourceNode && web.getTarget() == targetNode
						|| web.getTarget() == sourceNode && web.getSource() == targetNode )
				{
					webIt.remove();
				}
			}

			environment.getGroundMesh().unlinkNodes( sourceNode, targetNode );
		}


		Debug.startTiming("navmesh calculation");
		environment.getGroundMesh().update();
		Debug.stopTiming("navmesh calculation");

	}*/


	public IBackground getBackground() { return background; }

	public String getName() { return null; }

	public Environment getEnvironment() { return environment; }

	/**
	 * Add and remove pending units
	 */
	protected void reassesUnits()
	{
		while(!unitsToAdd.isEmpty())
		{
			IUnit unit = unitsToAdd.poll();
			
			if(unit.z() >= highestUnitZ )
			{
				units.add( unit );
				highestUnitZ = unit.z();
			}
			else
			{
				int currZ = highestUnitZ;
				int idx = units.size()-1;
				while(currZ > unit.z() && idx >= 0) { 
					currZ = units.get(idx).z();
					idx --; 
				}
				
				units.add( idx+1, unit  );
			}
			
			
			environment.add( unit );
		}

		while(!unitsToRemove.isEmpty())
		{
			IUnit unit = unitsToRemove.poll();

			// TODO: (optimize) a-ha!
			units.remove( unit );
			
			environment.remove( unit );
			// dat questionable construct:
			unit.getFaction().removeUnit( unit );

			unitsFactory.free( unit );
			
			Effect effect = unit.createDeathEffect();
			if(effect == null)
				assert debug("Unit " + unit + " has no death effect");
			else
				pendingEffects.add( effect );
			assert debug("Unit removed: " + unit);
		}
	}


	private static final String TAG = "level";
	private boolean debug(final String message)
	{
		Gdx.app.debug( TAG, message);
		return true;
	}
	private boolean log(final String message)
	{
		Gdx.app.log( TAG, message ) ;
		return true;
	}

	public UnitsFactory getUnitsFactory() {	return unitsFactory; }

	public Faction getFaction( final int factionId) { return factions.get( factionId ); }
	public int getFactionsNum() { return factions.size(); }

	/**
	 * Get misc parameters for game beginning
	 * @return
	 */
	public LevelInitialSettings getInitialSettings() { return def.getInitialSettings(); }



	public ResourceFactory getResourceFactory() { return gameFactory; }



	public void moveToFaction(IUnit unit, int targetFaction)
	{
		Faction prevFaction = unit.getFaction();
		prevFaction.removeUnit( unit );
		
		Faction newFaction = getFaction( targetFaction );
		newFaction.addUnit( unit );
		unit.setFaction( newFaction );
	}

	/*
	 * some generic module to append at level creation
	 */
	public <E> E getData() { return (E) data; }
	public void setData(ILevelData data) { this.data = data;  }



	public IController getController(int factionId) { return controllerFactory.getController(factionId); }






}
