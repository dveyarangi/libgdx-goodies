package eir.world.unit.weapon;

import eir.rendering.IUnitRenderer;
import eir.resources.levels.UnitDef;

public class BulletDef extends UnitDef
{
	
	private boolean dieOnCollision;

	public BulletDef( final int factionId, final float size, IUnitRenderer renderer, float maxSpeed, boolean dieOnCollision)
	{
		super( BulletFactory.NAME, factionId, size, renderer, false, maxSpeed );
		
		this.dieOnCollision = dieOnCollision;
	}

	public boolean shouldDieOnCollision() { return dieOnCollision; }

}
