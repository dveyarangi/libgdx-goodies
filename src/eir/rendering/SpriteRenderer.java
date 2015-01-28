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
import eir.world.unit.IUnit;
import eir.world.unit.Unit;

public class SpriteRenderer implements IUnitRenderer
{
	private TextureHandle texture;
	private float size;
	private Sprite sprite;
	
	private TextureHandle maskingTexture;
	private Sprite maskingSprite;
	
	private AnimationHandle deathEffect;
	private float deathSize;
	private Animation deathAnimation;
	
	public SpriteRenderer(String textureName, float size, String maskingTextureName, AnimationHandle deathEffect, float deathSize)
	{
		this( textureName, size );
		
		this.maskingTexture = TextureHandle.get(maskingTextureName);
		
		this.deathEffect = deathEffect;
		this.deathSize = deathSize;
	}
	public SpriteRenderer(String textureName, float size, AnimationHandle deathEffect, float deathSize)
	{
		this( textureName, size );
		
		this.deathEffect = deathEffect;
		this.deathSize = deathSize;
	}

	public SpriteRenderer(String textureName, float size)
	{
		this.texture = TextureHandle.get( textureName );
		
		this.size = size;
	}


	@Override
	public void init( ResourceFactory factory )
	{
		sprite = factory.createSprite( texture );
/*		if(maskingTexture != null)
		{
			maskingSprite = factory.createSprite( maskingTexture );
			
			FrameBuffer frameBuffer
		}*/
		if(deathEffect != null)
		{
			this.deathAnimation = factory.getAnimation( deathEffect );
		}
	}
	@Override
	public IEffect getBirthEffect(IUnit unit, IRenderer renderer)
	{
//		renderer.createEffect( )
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(Unit unit, IRenderer renderer)
	{
		if(sprite == null)
			return;
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
					size/sprite.getRegionWidth(),
					size/sprite.getRegionWidth(), unit.getAngle());
		}

	}

	@Override
	public Effect getDeathEffect(IUnit unit)
	{
		if(deathAnimation == null)
			return null;
		
		return Effect.getEffect( deathAnimation, deathSize, 
				unit.getArea().getAnchor(),
				new Vector2(), 
				RandomUtil.getRandomFloat(Angles.TAU), 1
				);

	}


}
