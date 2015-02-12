package eir.resources.levels;

import com.badlogic.gdx.math.Vector2;

public interface IUnitDef
{

	float getSize();

	UnitAnchorDef getAnchorDef();

	int getFactionId();

	String getType();

	public boolean isPickable();
	public float getMaxSpeed();

	Vector2 getPosition();


}
