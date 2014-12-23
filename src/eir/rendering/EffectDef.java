package eir.rendering;

import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;

public class EffectDef
{
	private AnimationHandle animation;

	private Vector2 position;

	private Vector2 velocity;

	private float angle;

	private float size;


	private float timeModifier;
	
	public EffectDef(AnimationHandle animation, float size, float timeModifier )
	{
		this.animation = animation;
		this.size = size;
		this.timeModifier = timeModifier;
	}

	public AnimationHandle getAnimation() { return animation; }

	public float getSize() { return size; }

	public float getTimeModifier() { return timeModifier; }

}
