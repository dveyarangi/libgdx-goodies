package eir.world.unit.structure;

import eir.rendering.SpriteRenderer;
import eir.resources.AnimationHandle;
import eir.resources.TextureHandle;
import eir.resources.levels.UnitDef;

public class SpawnerDef extends UnitDef
{
	private UnitDef spawnedUnit;
	private int maxUnits;
	private float spawnInterval;

	public SpawnerDef(final String type, final int factionId, final float size, final TextureHandle unitSprite,
			final AnimationHandle deathAnimation, final boolean isPickable,
			final UnitDef spawnedUnit, final int maxUnits, final float spawnInterval)
	{
		super( type, factionId, size, new SpriteRenderer(unitSprite.getPath(), size, deathAnimation, size), isPickable, 0 );

		this.spawnedUnit = spawnedUnit;
		this.maxUnits = maxUnits;
		this.spawnInterval = spawnInterval;
	}

	public UnitDef getSpawnedUnit() { return spawnedUnit; }

	public int getMaxUnits() { return maxUnits; }

	public float getSpawnInterval() { return spawnInterval;	}


}
