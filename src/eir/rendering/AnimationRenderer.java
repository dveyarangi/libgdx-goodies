package eir.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;
import eir.resources.ResourceFactory;
import eir.world.Effect;
import eir.world.IEffect;
import eir.world.unit.Unit;

public class AnimationRenderer  implements IUnitRenderer <Unit>
{

	private AnimationHandle animationHandle;
	private Animation animation;
	private int size;
	private float timeModifier;

	public AnimationRenderer(AnimationHandle animationHandle, int size, float timeModifier)
	{
		this.animationHandle = animationHandle;
		
		this.size = size;
		
		this.timeModifier = timeModifier;
	}



	@Override
	public void init( ResourceFactory factory )
	{
		this.animation = factory.getAnimation( animationHandle );
	}
	@Override
	public IEffect getBirthEffect(Unit unit, IRenderer renderer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(Unit unit, IRenderer renderer)
	{
		if(animation == null)
			return;
		final SpriteBatch batch = renderer.getSpriteBatch();
		Vector2 position = unit.getBody().getAnchor();

		TextureRegion region = animation.getKeyFrame( timeModifier * unit.getLifetime(), true );

		
		batch.draw( region,
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2,
				region.getRegionWidth(), region.getRegionHeight(),
				size*unit.size()/region.getRegionWidth(),
				size*unit.size()/region.getRegionWidth(), unit.getAngle());
	
	}

	@Override
	public Effect getDeathEffect(Unit unit)
	{
		// TODO Auto-generated method stub
		return null;
	}


}
