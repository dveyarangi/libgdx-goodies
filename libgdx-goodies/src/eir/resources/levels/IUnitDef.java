package eir.resources.levels;

import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;
import eir.resources.TextureHandle;

public interface IUnitDef
{

	TextureHandle getUnitSprite();

	AnimationHandle getDeathAnimation();

	float getSize();

	UnitAnchorDef getAnchorDef();

	int getFactionId();

	String getType();

	public boolean isPickable();
	public float getMaxSpeed();

	Vector2 getPosition();

}
