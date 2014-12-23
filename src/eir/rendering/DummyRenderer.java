package eir.rendering;

import eir.resources.ResourceFactory;
import eir.world.Effect;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;

public class DummyRenderer implements IUnitRenderer
{

	@Override
	public void init(ResourceFactory factory)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Effect getBirthEffect(IUnit unit, IRenderer renderer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(Unit unit, IRenderer renderer)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Effect getDeathEffect(IUnit unit)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
