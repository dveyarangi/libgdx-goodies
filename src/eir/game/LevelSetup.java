package eir.game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import eir.game.screens.IGameUI;
import eir.input.GameInputProcessor;
import eir.resources.ResourceFactory;
import eir.resources.levels.LevelBuilder;
import eir.resources.levels.LevelDef;
import eir.world.Level;
import eir.world.controllers.ControllerFactory;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.UnitsFactory.UnitFactory;

public abstract class LevelSetup
{
	protected ResourceFactory gameFactory;

	protected UnitsFactory unitsFactory;

	private LevelDef levelDef;

	final ExecutorService pool = Executors.newFixedThreadPool(1);
	private float progress;
	private String state;
	
	private ControllerFactory controllerFactory;


	public LevelSetup()
	{
		// game resources registry and loader
		gameFactory = new ResourceFactory();
	}
	
	public void init()
	{
		controllerFactory = createControllerFactory();
		levelDef = loadLevelDef();
		Map <String, UnitFactory<? extends Unit>> factories = new HashMap <String, UnitFactory <? extends Unit>> ();
		registerUnitFactories(factories);
		unitsFactory = new UnitsFactory( levelDef, gameFactory, factories );
	}

	protected abstract LevelDef loadLevelDef();

	/**
	 * Register unit factories
	 * @param factories
	 */
	protected abstract void registerUnitFactories( Map <String, UnitFactory<? extends Unit>> factories );
	
	public ControllerFactory getControllerFactory() { return controllerFactory; }

	public ResourceFactory getGameFactory() { return gameFactory; }
	public UnitsFactory getUnitsFactory() { return unitsFactory; }
	public LevelDef getLevelDef() { return levelDef; }


	public synchronized void advance(final float amount, final String state)
	{
		progress += amount;
		this.state = state;
	}
	public float getProgress() { return progress; }
	public String getState() { return state; }

	public abstract GameInputProcessor getInputController(Level level, IGameUI ui);
	
	public abstract ControllerFactory createControllerFactory();

	public abstract IGameUI getUI(Stage stage);

	public Level createLevel()
	{
//		gameFactory = levelSetup.getGameFactory();
//		UnitsFactory unitsFactory = levelSetup.getUnitsFactory();
//		LevelDef levelDef = levelSetup.getLevelDef();
		// creating level from level definitions:
		Level level = new LevelBuilder( this ).build( levelDef );
		
		return level;
	}
}
