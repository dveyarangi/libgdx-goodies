package eir.rendering;

import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.resources.AnimationHandle;
import eir.world.IEffect;

public interface IRenderer
{

	ShapeRenderer getShapeRenderer();

	SpriteBatch getSpriteBatch();

	public Animation getAnimation( final AnimationHandle handle );

	void addEffect(IEffect effect/*EffectDef effectDef/*, Vector2 position, Vector2 velocity, float angle*/);

	RayHandler getRayHandler();

}
