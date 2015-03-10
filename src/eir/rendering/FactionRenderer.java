package eir.rendering;

import eir.resources.ResourceFactory;
import eir.world.IEffect;
import eir.world.unit.Unit;
import gnu.trove.map.hash.TIntObjectHashMap;

public class FactionRenderer implements IUnitRenderer <Unit>
{
	
	private TIntObjectHashMap<IUnitRenderer> renderers;

	public FactionRenderer(Object [] states)
	{
		renderers = new TIntObjectHashMap <IUnitRenderer> ();
		
		for(int idx = 0; idx < states.length; idx += 2)
		{
			renderers.put((Integer)states[idx], (IUnitRenderer)states[idx+1]);
		}
	}

	@Override
	public void init( ResourceFactory factory )
	{
		for(Object renderer : renderers.values())
			((IUnitRenderer)renderer).init(factory);
	}
	@Override
	public IEffect getBirthEffect(Unit unit, IRenderer renderer)
	{
		assert renderers.contains( unit.getFaction().getOwnerId() ) : "No renderer for unit " + unit + ", state " + unit.getFaction().getOwnerId();
		return renderers.get( unit.getFaction().getOwnerId() ).getBirthEffect(unit, renderer);
	}

	@Override
	public void render(Unit unit, IRenderer renderer)
	{
		assert renderers.contains( unit.getFaction().getOwnerId() ) : "No renderer for unit " + unit + ", state " + unit.getFaction().getOwnerId();
		renderers.get( unit.getFaction().getOwnerId() ).render( unit, renderer );
	}

	@Override
	public IEffect getDeathEffect(Unit unit)
	{
		return renderers.get( unit.getFaction().getOwnerId() ).getDeathEffect(unit);
	}


}
