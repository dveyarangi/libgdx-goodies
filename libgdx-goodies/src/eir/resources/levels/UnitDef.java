package eir.resources.levels;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.IUnitRenderer;



public class UnitDef implements IUnitDef
{

	private String type;

	private float size;

	private int faction;
	
	protected Vector2 position;

	private UnitAnchorDef anchor;

	private IUnitRenderer renderer;

	private boolean isPickable;
	
	private float maxSpeed;

	private UnitDef() {}

	public UnitDef(final String type, final int faction, final float size,
			final IUnitRenderer renderer,
			final boolean isPickable,
			final float maxSpeed)
	{
		this.type = type;
		this.size = size;
		this.faction = faction;

		this.renderer = renderer;

		this.isPickable = isPickable;
		
		this.maxSpeed = maxSpeed;
	}

	@Override
	public String getType() { return type; }

	@Override
	public int getFactionId() { return faction; }

	@Override
	public float getSize() { return size; }

	@Override
	public IUnitRenderer getUnitRenderer() { return renderer; }

	@Override
	public UnitAnchorDef getAnchorDef() { return anchor; }


	@Override
	public boolean isPickable() { return isPickable; }
	
	@Override
	public float getMaxSpeed() { return maxSpeed; }

	@Override
	public Vector2 getPosition() { return position; }

}
