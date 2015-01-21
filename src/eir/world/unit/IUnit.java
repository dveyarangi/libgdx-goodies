package eir.world.unit;

import eir.rendering.IRenderer;
import eir.rendering.IUnitRenderer;
import eir.resources.levels.IUnitDef;
import eir.world.Effect;
import eir.world.environment.Anchor;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import gnu.trove.list.array.TIntArrayList;

public interface IUnit extends ISpatialObject
{
	/**
	 * Unit collision box and anchor point
	 * @return
	 */
	@Override
	public AABB getArea();

	/**
	 * Unit orientation
	 * @return
	 */
	public float getAngle();


	public Hull getHull();

	public ISpatialObject getTarget();

	@Deprecated
	public float getSize();
	public float size();
	public Faction getFaction();

	public String getType();

	public void setFaction(Faction newFaction);

	public Anchor getAnchor();

	public boolean dealsFriendlyDamage();

	public float hit(final Damage source, final IDamager damager, final float damageCoef);

	public void dispose();

	public void update(float delta);

	public void draw(IRenderer levelRenderer);

	public <E extends IUnitRenderer> E getRenderer();

	public TIntArrayList getActiveOverlays();

	public boolean isHovered();

	public TIntArrayList getHoverOverlays();

	public void setIsHovered(float x, float y);

	public void toggleOverlay(int factionOid);

	public Effect createDeathEffect();

	public void setDead();

	public int z();

	public <E extends IUnitDef> E getDef();

	public boolean needsSpatialUpdate();
	public boolean isCollidable();

}
