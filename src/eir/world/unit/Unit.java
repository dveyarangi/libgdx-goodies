/**
 *
 */
package eir.world.unit;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.rendering.IUnitRenderer;
import eir.rendering.LevelRenderer;
import eir.resources.levels.IUnitDef;
import eir.world.IEffect;
import eir.world.Level;
import eir.world.environment.Anchor;
import eir.world.environment.Environment;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.aspects.IDamager;
import eir.world.unit.damage.Damage;
import eir.world.unit.damage.Hull;
import eir.world.unit.weapon.Weapon;
import gnu.trove.list.array.TIntArrayList;

/**
 * A generic game unit.
 *
 * TODO: becomes bulky, consider separating rendering and overlays
 * 
 * To integrate a unit, one should create {@link UnitFactory} and 
 *
 * @author dveyarangi
 */
public abstract class Unit implements ISpatialObject, IUnit
{
	///////////////////////////////////////////////
	// Unit initial and static definitions


	/**
	 * Some generic unit definitions;
	 */
	protected IUnitDef def;

	/**
	 * Unit faction
	 */
	protected Faction faction;

	/**
	 * Unit's anchor node, may be null for freely moving units.
	 */
	public Anchor anchor;

	/**
	 * Units anchored to this one
	 */
	public List <IUnit> anchoredUnits;
	///////////////////////////////////////////

	/**
	 * Some id
	 */
	private int id;

	private int hashcode;

	/**
	 * Unit location and collision box
	 */
	private AABB body;

	/**
	 * Unit orientation
	 * TODO: encapsulate in body?
	 */
	public float angle;

	/**
	 * Specifies if this unit is alive
	 * This flags to unit processing loop to withdraw unit from update cycle
	 */
	private boolean isAlive;

	/**
	 * Unit hull contains hit points and damage resistances
	 * TODO: create from defs
	 */
	protected Hull hull;

	/**
	 * Time since unit creation
	 */
	protected float lifetime;

	/**
	 * Max time length
	 * TODO: mainly used for bullets now; consider replacing with travel distance
	 */
	public float lifelen;

	/**
	 * Unit attack or movement target, if it has one
	 */
	public ISpatialObject target;

	/**
	 * TODO: this is for birdies only, consider removal
	 */
	public float timeToPulse = 0;

	/**
	 * Unit physical velocity, actual for freely moving units
	 */
	protected final Vector2 velocity;
	
	private final Vector2 force;

	/**
	 * List of overlays that are toggled on for this unit.
	 */
	private TIntArrayList overlays;

	/**
	 * List of overlays that activate on mouse hovering
	 */
	private TIntArrayList hoverOverlays;
	
	/**
	 * States whether this unit is currently has cursor over it
	 */
	Vector2 hoverVector;
	
	private IUnitRenderer renderer;

	public Unit( )
	{
		velocity = new Vector2();
		
		force = new Vector2();

		this.body = AABB.createPoint( 0, 0 );

		this.overlays = new TIntArrayList ();
		this.hoverOverlays = new TIntArrayList ();

		this.hashcode = hashCode();
		
		this.hoverVector = new Vector2(Float.NaN, Float.NaN);
		
		this.anchoredUnits = new ArrayList <IUnit> ();
	}



	/**
	 * Initializes this unit using the provided defs and position and assigning it to the faction.
	 * @param factory
	 * @param def
	 * @param renderer 
	 * @param anchor
	 * @param faction
	 */
	public void init(final Level level, final IUnitDef def, final float x, final float y, final float angle)
	{
		this.def = def;

		//substitute unit faction
		this.faction = level.getFaction( def.getFactionId() );

		// update unit position
		this.body.update( x, y,size()/2, size()/2, false);

		this.angle = angle;
		
		this.renderer = level.getResourceFactory().getRenderer( def );

		registerOverlays();
		
		// reset unit variables:
		reset( level );
	}

	/**
	 * Initializes this unit using the provided defs and anchor and assigning it to the faction.
	 * @param factory
	 * @param level
	 * @param def
	 * @param renderer 
	 * @param anchor
	 * @param faction
	 */
	public void init(final Level level, final IUnitDef def, final Anchor anchor )
	{
		this.anchor = anchor;
		
		// inform parent about anchored unit added:
		anchor.getParent().addAnchoredUnit( this );
		
		init(level, def, anchor.getPoint().x, anchor.getPoint().y, anchor.getAngle());
	}
	
	/**
	 * Resets unit according to {@link #def} properties.
	 * Called when unit is created or retrieved from pool.
	 *
	 * Override this method to reset subclass's custom properties
	 * @param level
	 */
	protected void reset( final Level level )
	{
		this.isAlive = true;
		this.id = Environment.createObjectId();

		this.lifetime = 0;
		this.lifelen = Float.NaN;

		this.timeToPulse = 0;

		this.target = null;

		this.velocity.set( 0,0 );
		this.force.set( 0,0 );
		
		this.hoverVector.set(Float.NaN, Float.NaN);
	}
	
	public void addAnchoredUnit(IUnit unit)
	{
		anchoredUnits.add( unit );
	}

	public void removeAnchoredUnit(IUnit unit)
	{
		this.anchoredUnits.remove( unit );
	}

	protected void registerOverlays()
	{
		this.overlays.clear();
		this.hoverOverlays.clear();

		addHoverOverlay( LevelRenderer.INTEGRITY_OID);
		toggleOverlay( LevelRenderer.INTEGRITY_OID);
	}
	
	@Override
	public void dispose()
	{
		if(anchor != null)
			anchor.getParent().anchoredUnits.remove( this );
	}

	public AABB getBody() { return body; }

	@Override
	public Faction getFaction() { return faction; }

	@Override
	public String getType() { return def.getType(); }


	@Override
	public AABB getArea() { return body; }

	@Override
	public float getAngle() { return angle; }

	@Override
	public int getId() { return id; }

	public String getName()  { return this.getClass().getSimpleName(); }

	@Override
	public String toString() { 	return getName() + " (" + getId() + ")"; }

	@Override
	public void update(final float delta)
	{

		// this is sad:
		if(!Float.isNaN(delta))
		lifetime += delta;

		if( lifetime > lifelen )
		{
			setDead();
			return;
		}

	}

	@Override
	public void draw( final IRenderer levelRenderer )
	{
		renderer.render( this , levelRenderer );
	}



	@Override
	public boolean isAlive() { return isAlive; }

	@Override
	public void setDead() { 
		this.isAlive = false;

		if(anchor != null)
			anchor.getParent().removeAnchoredUnit( this );
		
		for(IUnit unit : anchoredUnits)
		{
			((Unit)unit)._setDead();
		}	
	}
	
	public void _setDead() {
		this.isAlive = false;
		
	}



	/**
	 * @param damageReduction
	 * @param damage
	 */
	@Override
	public float hit(final Damage source, final IDamager damager, final float damageCoef)
	{
		float damage = damage( damager.getDamage(), damageCoef );
		faction.getController().yellUnitHit( this, damage, damager );

		return damage;
	}

	protected float damage( final Damage source, final float damageCoef )
	{
		if(hull == null) // TODO: stub, in future hulless units should not respond to damage
		{
			// setDead();
			return 0;
		}

		float damage = hull.hit( source, damageCoef );

		if(hull.isBreached())
		{
			setDead(); // TODO: consider decision by unit controller
		}

		return damage;
	}

	@Override @Deprecated
	public float getSize() { return this.def.getSize(); }
	@Override
	public float size() { return this.def.getSize(); }
	public float radius() { return this.def.getSize()/2; }



	@Override
	public Anchor getAnchor() { return anchor; }

	@Override
	public boolean dealsFriendlyDamage() { return false; }

	@Override
	public ISpatialObject getTarget() { return target; }

	public  float getMaxSpeed() { return def.getMaxSpeed(); }
	public float getLifetime() { return lifetime; }
	@Override
	public Vector2 getVelocity() { return velocity; }
	public Vector2 getForce() { return force; }

	@Override
	public void toggleOverlay(final int oid)
	{
		if(overlays.contains( oid ))
		{
			overlays.remove( oid );
		} else
		{
			overlays.add( oid );
		}
	}

	protected void addHoverOverlay( final int oid ) { hoverOverlays.add( oid ); }
	@Override
	public Hull getHull() { return hull; }

	@Override
	public TIntArrayList getActiveOverlays() { return overlays; }
	@Override
	public TIntArrayList getHoverOverlays() { return hoverOverlays; }

	public float cx() { return body.getAnchor().x; }
	public float cy() { return body.getAnchor().y; }

	public Weapon getWeapon() { return null; }

	@Override
	public void setIsHovered( float x, float y ) 
	{ 
		this.hoverVector.set(x,y); 
		for(int idx = 0; idx < anchoredUnits.size(); idx ++)
			anchoredUnits.get(idx).setIsHovered(x, y);
	}
	
	@Override
	public boolean isHovered() { return !Float.isNaN(hoverVector.x); }
	public Vector2 hoverVector() { return hoverVector; }

	@Override public int hashCode() {	return hashcode; }

	@Override
	public <E extends IUnitDef> E getDef() { return (E)def; }
	@Override
	public <E extends IUnitRenderer> E getRenderer() { return (E) renderer; }

	
	@Override
	public void setFaction(Faction newFaction)
	{
		this.faction = newFaction;
	}

	@Override
	public IEffect createDeathEffect()
	{
		IEffect effect =  renderer.getDeathEffect( this );
		if(effect == null)
			return null;
		effect.reset( this );
		
		return effect;
	}
	
	@Override
	public int z() { return 0; }
	@Override
	public boolean needsSpatialUpdate() { return false; }
	@Override
	public boolean isCollidable() { return false; }
	
	protected Level getLevel() { return getFaction().getLevel(); }
}
