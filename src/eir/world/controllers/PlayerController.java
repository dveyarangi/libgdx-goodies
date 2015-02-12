package eir.world.controllers;

import eir.rendering.IRenderer;
import eir.world.unit.Faction;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;
import eir.world.unit.ai.AttackingOrder;
import eir.world.unit.aspects.IDamager;
import eir.world.unit.weapon.CannonFactory;

public class PlayerController implements IController {

	@Override
	public void init(final Faction faction) 
	{
		faction.getScheduler().addOrder( CannonFactory.NAME, new AttackingOrder( 0 ) );

	}

	@Override
	public void yellUnitHit(final Unit unit, float damage, final IDamager hitSource) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unitAdded( final IUnit unit )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(IRenderer renderer)
	{
		// TODO Auto-generated method stub
		
	}

}
