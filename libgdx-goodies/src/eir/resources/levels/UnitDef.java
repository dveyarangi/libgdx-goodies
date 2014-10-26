package eir.resources.levels;

import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;
import eir.resources.TextureHandle;



public class UnitDef implements IUnitDef
{

	private String type;

	private float size;

	private int faction;
	
	protected Vector2 position;

	private UnitAnchorDef anchor;

	private TextureHandle spriteTexture;

	private AnimationHandle deathAnimation;

	private boolean isPickable;
	
	private float maxSpeed;

	private UnitDef() {}

	public UnitDef(final String type, final int faction, final float size,
			final TextureHandle unitSprite,
			final AnimationHandle deathAnimation,
			final boolean isPickable,
			final float maxSpeed)
	{
		this.type = type;
		this.size = size;
		this.faction = faction;

		this.spriteTexture = unitSprite;
		this.deathAnimation = deathAnimation;

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
	public AnimationHandle getDeathAnimation() { return deathAnimation; }

	@Override
	public UnitAnchorDef getAnchorDef() { return anchor; }

	@Override
	public TextureHandle getUnitSprite() { return spriteTexture; }

	@Override
	public boolean isPickable() { return isPickable; }
	
	@Override
	public float getMaxSpeed() { return maxSpeed; }

	@Override
	public Vector2 getPosition() { return position; }

}
