package eir.world.unit.aspects;

import eir.world.environment.spatial.AABB;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.damage.Damage;


public interface IDamager
{

	public Damage getDamage();

	public String getType();

	public Faction getFaction();

	public Unit getSource();

	public boolean dealsFriendlyDamage();

	public AABB getArea();

	public float getSize();
}
