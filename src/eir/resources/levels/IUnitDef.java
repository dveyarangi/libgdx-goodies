package eir.resources.levels;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.IUnitRenderer;

public interface IUnitDef
{

	IUnitRenderer getUnitRenderer();

	float getSize();

	UnitAnchorDef getAnchorDef();

	int getFactionId();

	String getType();

	public boolean isPickable();
	public float getMaxSpeed();

	Vector2 getPosition();

}
