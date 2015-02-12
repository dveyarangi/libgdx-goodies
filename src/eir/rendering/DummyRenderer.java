package eir.rendering;

import eir.resources.ResourceFactory;
import eir.world.Effect;
import eir.world.IEffect;
import eir.world.unit.IUnit;

public class DummyRenderer implements IUnitRenderer <IUnit>
{

	@Override
	public void init(ResourceFactory factory)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public IEffect getBirthEffect(IUnit unit, IRenderer renderer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(IUnit unit, IRenderer renderer)
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
