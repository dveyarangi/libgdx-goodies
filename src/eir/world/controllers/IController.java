package eir.world.controllers;

import eir.rendering.IRenderer;
import eir.world.unit.Faction;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;
import eir.world.unit.aspects.IDamager;

public interface IController {

	public void unitAdded(IUnit unit);

	void init(Faction faction);

	void yellUnitHit(Unit unit, float damage, IDamager hitSource);

	void update( float delta );
	
	public void draw(IRenderer renderer);
}
