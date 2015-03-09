package eir.rendering;

import yarangi.math.Angles;
import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;
import eir.resources.ResourceFactory;
import eir.resources.TextureHandle;
import eir.world.Effect;
import eir.world.IEffect;
import eir.world.unit.Unit;

public class SpriteRenderer implements IUnitRenderer <Unit>
{
	private TextureHandle texture;
	private float sizeModifier;
	private Sprite sprite;
	
	private AnimationHandle deathEffect;
	private float deathSizeModifier;
	private Animation deathAnimation;

	public SpriteRenderer(String textureName, int sizeModifier, AnimationHandle deathEffect, float deathSizeModifier)
	{
		this( textureName, sizeModifier );
		
		this.deathEffect = deathEffect;
		this.deathSizeModifier = deathSizeModifier;
	}

	public SpriteRenderer( String textureName, int sizeModifier )
	{
		this.sizeModifier = sizeModifier;
		this.texture = TextureHandle.get( textureName );
	}


	@Override
	public void init( ResourceFactory factory )
	{
		this.sprite = factory.createSprite( texture );
		
		assert sprite != null;
		if(deathEffect != null)
		{
			this.deathAnimation = factory.getAnimation( deathEffect );
		}
	}
	@Override
	public IEffect getBirthEffect(Unit unit, IRenderer renderer)
	{
//		renderer.createEffect( )
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(Unit unit, IRenderer renderer)
	{

		final SpriteBatch batch = renderer.getSpriteBatch();
		Vector2 position = unit.getBody().getAnchor();
		
/*		if(maskingSprite != null)
		{

		batch.setBlendFunction(1, 1);
		
		batch.draw( sprite,
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				size/sprite.getRegionWidth(),
				size/sprite.getRegionHeight(), unit.getAngle());
		
		batch.setBlendFunction(1, 1);
		
		batch.draw( maskingSprite,
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				size/sprite.getRegionWidth(),
				size/sprite.getRegionHeight(), unit.getAngle());
		batch.setBlendFunction(GL20.GL_FUNC_ADD, GL20.GL_FUNC_ADD);
		}
		else*/
		{
			batch.draw( sprite,
					position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
					sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
					sprite.getRegionWidth(), sprite.getRegionHeight(),
					unit.getDef().getSize()*sizeModifier/sprite.getRegionWidth(),
					unit.getDef().getSize()*sizeModifier/sprite.getRegionWidth(), unit.getAngle());
		}

	}

	@Override
	public Effect getDeathEffect(Unit unit)
	{
		if(deathAnimation == null)
			return null;
		
		return Effect.getEffect( deathAnimation, unit.size() * deathSizeModifier, 
				unit.getArea().getAnchor(),
				new Vector2(), 
				RandomUtil.getRandomFloat(Angles.TAU), 1
				);

	}


}
