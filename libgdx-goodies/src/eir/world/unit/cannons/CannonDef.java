package eir.world.unit.cannons;

import eir.rendering.DummyRenderer;
import eir.resources.levels.UnitDef;
import eir.world.unit.weapon.WeaponDef;

public class CannonDef extends UnitDef
{
	private WeaponDef weaponDef;

	public CannonDef(final int faction, final float size,
		final WeaponDef weaponDef, final boolean isPickable)
	{
		super( CannonFactory.NAME, faction, size, new DummyRenderer(), isPickable, 0 );

		this.weaponDef = weaponDef;
	}

	public WeaponDef getWeaponDef() { return weaponDef; }
}
