package eir.rendering;

import eir.resources.ResourceFactory;
import eir.world.IEffect;
import eir.world.unit.IUnit;

public interface IUnitRenderer <U extends IUnit>
{
	public void init( ResourceFactory factory );
	
	public IEffect getBirthEffect( U unit, IRenderer renderer );
	/**
	 * Regular unit rendering procedure
	 * @param renderer
	 */
	public void render( U unit, IRenderer renderer );

	IEffect getDeathEffect(U unit);
	
	/*
	 * 
		this.deathAnimation = def.getDeathAnimation() == null ? null :
			factory.getAnimation( def.getDeathAnimation() );

	  	if(deathAnimation != null)
			return Effect.getEffect( deathAnimation, 10, getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), 1 );
		return null;

	 */
}
