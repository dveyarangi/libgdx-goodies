package eir.world.controllers;

import eir.world.unit.Faction;
import eir.world.unit.IDamager;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;

public interface IController {

	public void unitAdded(IUnit unit);

	void init(Faction faction);

	void yellUnitHit(Unit unit, float damage, IDamager hitSource);

	void update( float delta );
}
