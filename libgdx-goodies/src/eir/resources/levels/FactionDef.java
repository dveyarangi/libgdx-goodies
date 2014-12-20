package eir.resources.levels;

import gnu.trove.impl.hash.TIntHash;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

public class FactionDef
{
	private int ownerId;

	private List <UnitDef> units;

	private Color color;
	private Color secondaryColor;

	TIntHashSet enemiesSet = new TIntHashSet();

	public FactionDef(final int factionId, final Color color, final Color secondaryColor, TIntHashSet enemiesSet)
	{
		this.ownerId = factionId;
		this.color = color;
		this.secondaryColor = secondaryColor;
		this.units = new ArrayList <UnitDef> ();
		this.enemiesSet = enemiesSet;
	}

	public int getOwnerId() { return ownerId; }

	public List <UnitDef> getUnitDefs() { return units; }

	public Color getColor() { return color; }
	public Color getSecondaryColor() { return secondaryColor; }

	public TIntHash getEnemies() { return enemiesSet; }
}
