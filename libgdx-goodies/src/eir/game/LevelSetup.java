package eir.game;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eir.input.GameInputProcessor;
import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.world.Level;
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


	public LevelSetup()
	{
		// game resources registry and loader
		gameFactory = new ResourceFactory();

		
//		levelDef = new LevelGenerator().generate( levelParams, gameFactory );


	}
	
	public void init()
	{
		levelDef = loadLefelDef();
		Set <UnitFactory<? extends Unit>> factories = new HashSet < UnitFactory <? extends Unit>> ();
		registerUnitFactories(factories);
		unitsFactory = new UnitsFactory( gameFactory, factories );
	}

	protected abstract LevelDef loadLefelDef();

	protected abstract void registerUnitFactories( Set<UnitFactory<? extends Unit>> factories );

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

	public abstract GameInputProcessor getInputController(Level level);


}
