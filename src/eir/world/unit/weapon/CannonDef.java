package eir.world.unit.weapon;

import eir.resources.levels.UnitDef;

public class CannonDef extends UnitDef
{
	private WeaponDef weaponDef;

	public CannonDef(final int faction, final float size,
		final WeaponDef weaponDef, final boolean isPickable)
	{
		super( CannonFactory.NAME, faction, size, isPickable, 0 );

		this.weaponDef = weaponDef;
	}

	public WeaponDef getWeaponDef() { return weaponDef; }
}
