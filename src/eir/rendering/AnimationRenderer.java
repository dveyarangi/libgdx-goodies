package eir.rendering;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.ResourceFactory;
import eir.resources.TextureHandle;
import eir.world.Effect;
import eir.world.IEffect;
import eir.world.unit.IUnit;
import eir.world.unit.Unit;

public class AnimationRenderer implements IUnitRenderer
{

	private String textureName;
	private Sprite sprite;
	private int size;

	public AnimationRenderer(String textureName, int size)
	{
		this.textureName = textureName;
		
		this.size = size;
	}



	@Override
	public void init( ResourceFactory factory )
	{
		this.sprite = factory.createSprite( TextureHandle.get(textureName) );
	}
	@Override
	public IEffect getBirthEffect(IUnit unit, IRenderer renderer)
	{
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

		TextureRegion region = unit.getFaction().getAntAnimation().getKeyFrame( unit.getLifetime(), true );
		batch.draw( region,
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2,
				region.getRegionWidth(), region.getRegionHeight(),
				size/region.getRegionWidth(),
				size/region.getRegionWidth(), unit.getAngle());
	
	}

	@Override
	public Effect getDeathEffect(IUnit unit)
	{
		// TODO Auto-generated method stub
		return null;
	}


}
