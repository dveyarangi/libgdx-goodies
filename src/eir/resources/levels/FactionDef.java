package eir.resources.levels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

public class FactionDef
{
	private int ownerId;

	private List <UnitDef> units;

	private Color color;
	private Color secondaryColor;

	int [] enemyFactions;

	public FactionDef(final int factionId, final Color color, final Color secondaryColor, int [] enemyFactions)
	{
		this.ownerId = factionId;
		this.color = color;
		this.secondaryColor = secondaryColor;
		this.units = new ArrayList <UnitDef> ();
		this.enemyFactions = enemyFactions;
	}

	public int getOwnerId() { return ownerId; }

	public List <UnitDef> getUnitDefs() { return units; }

	public Color getColor() { return color; }
	public Color getSecondaryColor() { return secondaryColor; }
	
	public boolean isEnemy(int factionId)
	{
		for(int idx = 0; idx < enemyFactions.length; idx ++)
			if(enemyFactions[idx] == factionId)
				return true;
		
		return false;
	}
}
