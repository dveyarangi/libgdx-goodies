package eir.rendering;

import eir.resources.ResourceFactory;
import eir.world.Effect;
import eir.world.IEffect;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;

public interface IUnitRenderer
{
	public void init( ResourceFactory factory );
	
	public IEffect getBirthEffect( IUnit unit, IRenderer renderer );
	/**
	 * Regular unit rendering procedure
	 * @param renderer
	 */
	public void render( Unit unit, IRenderer renderer );

	Effect getDeathEffect(IUnit unit);
	
	/*
	 * 
		this.deathAnimation = def.getDeathAnimation() == null ? null :
			factory.getAnimation( def.getDeathAnimation() );

	  	if(deathAnimation != null)
			return Effect.getEffect( deathAnimation, 10, getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), 1 );
		return null;

	 */
}
