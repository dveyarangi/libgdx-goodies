package eir.world.unit.weapon;

import eir.resources.levels.UnitDef;

public class CannonDef extends UnitDef
{
	private WeaponDef weaponDef;

	public CannonDef(String name, final int faction, final float size,
		final WeaponDef weaponDef, final boolean isPickable, int movementSpeed)
	{
		super( name, faction, size, isPickable, movementSpeed );

		this.weaponDef = weaponDef;
	}

	public WeaponDef getWeaponDef() { return weaponDef; }
}
