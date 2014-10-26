package eir.game;

import java.util.Set;

import eir.resources.levels.LevelDef;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory.UnitFactory;

public abstract class JSONLevelSetup extends LevelSetup
{
	
	private final String levelId;
	public JSONLevelSetup(String levelId)
	{
		super();
		this.levelId = levelId;
	}
	

	@Override
	protected LevelDef loadLefelDef()
	{
		
		// loading level definitions from file:
		// this call also registers resource handles at the factory for future loading
		LevelDef levelDef = gameFactory.readLevelDefs( levelId, unitsFactory );
		return null;
	}

	@Override
	protected abstract void registerUnitFactories(Set<UnitFactory<? extends Unit>> factories);

}
