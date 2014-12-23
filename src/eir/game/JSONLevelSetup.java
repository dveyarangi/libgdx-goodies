package eir.game;

import eir.resources.levels.LevelDef;

public abstract class JSONLevelSetup extends LevelSetup
{
	
	private final String levelId;
	public JSONLevelSetup(String levelId)
	{
		super();
		this.levelId = levelId;
	}
	

	@Override
	protected LevelDef loadLevelDef()
	{
		
		// loading level definitions from file:
		// this call also registers resource handles at the factory for future loading
		LevelDef levelDef = gameFactory.readLevelDefs( levelId, unitsFactory );
		return levelDef;
	}

}
