package eir.resources.levels;

import eir.game.LevelSetup;
import eir.world.Level;

/**
 * Builds level from level definitions
 * @author Fima
 *
 */
public class LevelBuilder
{
	private LevelSetup setup;
	public LevelBuilder( LevelSetup setup)
	{
		this.setup = setup;
	}

	public Level build(final LevelDef levelDef)
	{
		Level level = new Level( setup );

		level.init( levelDef );

		return level;

	}
}
