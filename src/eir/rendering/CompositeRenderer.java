package eir.rendering;

import eir.resources.ResourceFactory;
import eir.world.Effect;
import eir.world.IEffect;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;
import gnu.trove.map.hash.TIntObjectHashMap;

public class CompositeRenderer implements IUnitRenderer
{
	
	private TIntObjectHashMap<IUnitRenderer> renderers;
	private int state = 0;

	public CompositeRenderer(Object [] states)
	{
		renderers = new TIntObjectHashMap <IUnitRenderer> ();
		
		for(int idx = 0; idx < states.length; idx += 2)
		{
			renderers.put((Integer)states[idx], (IUnitRenderer)states[idx+1]);
		}
	}

	public void setState(int state)
	{
		this.state = state;
	}

	@Override
	public void init( ResourceFactory factory )
	{
		for(Object renderer : renderers.values())
			((IUnitRenderer)renderer).init(factory);
	}
	@Override
	public IEffect getBirthEffect(IUnit unit, IRenderer renderer)
	{
		return renderers.get( state ).getBirthEffect(unit, renderer);
	}

	@Override
	public void render(Unit unit, IRenderer renderer)
	{
		renderers.get( state ).render( unit, renderer );
	}

	@Override
	public Effect getDeathEffect(IUnit unit)
	{
		return renderers.get( state ).getDeathEffect(unit);
	}


}
