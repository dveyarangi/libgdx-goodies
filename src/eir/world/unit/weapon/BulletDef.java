package eir.world.unit.weapon;

import eir.rendering.IUnitRenderer;
import eir.resources.levels.UnitDef;

public class BulletDef extends UnitDef
{

	public BulletDef( final int factionId, final int size, IUnitRenderer renderer, float maxSpeed)
	{
		super( BulletFactory.NAME, factionId, size, renderer, false, maxSpeed );
	}

}
