package eir.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import eir.debug.Debug;
import eir.game.LevelSetup;
import eir.input.GameInputProcessor;
import eir.rendering.LevelRenderer;
import eir.resources.ResourceFactory;
import eir.resources.levels.LevelBuilder;
import eir.resources.levels.LevelDef;
import eir.world.Level;
import eir.world.unit.UnitsFactory;

/**
 * place holder screen for now. does same as application listener from sample
 * code.
 *
 * @author Ni
 *
 */
public class GameScreen extends AbstractScreen
{
	private ResourceFactory gameFactory;
	private GameInputProcessor inputController;
	
	private GameUI ui;
	private LevelRenderer renderer;
	private Level level;
	
	private Table table;

	public GameScreen(final Game game, final LevelSetup levelSetup)
	{
		super( game );

		gameFactory = levelSetup.getGameFactory();
		UnitsFactory unitsFactory = levelSetup.getUnitsFactory();
		LevelDef levelDef = levelSetup.getLevelDef();

	    ui = new GameUI( stage );
		// creating level from level definitions:
		level = new LevelBuilder( gameFactory, unitsFactory ).build( levelDef );

		inputController = levelSetup.getInputController( level );

		level.getBackground().init( gameFactory, inputController );

		Debug.init( level, inputController );

		this.renderer = new LevelRenderer( gameFactory, inputController, level );
		
		ui.initUI();
	}

	@Override
	public void render(final float delta)
	{

        // (2) draw the result

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        
		inputController.update( delta );
		
		stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();

		float modifiedTime = inputController.getTimeModifier() * delta;

		level.update( modifiedTime );
		Debug.debug.update( delta );

//		Gdx.gl.glClearColor( 0.8f, 0.8f, 1f, 1 );
//		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		level.getBackground().update( modifiedTime );

		renderer.render( modifiedTime );
		
		ui.render( renderer );
		
		super.render( delta );
	}

	@Override
	public void resize(final int width, final int height)
	{
		super.resize( width, height );
		inputController.resize( width, height );

		level.getBackground().resize( width, height );
	}

	@Override
	public void show()
	{
		super.show();
		inputController.show();

	}

	@Override
	public void hide()
	{
		super.hide();
	}

	@Override
	public void pause()
	{
		super.pause();
	}

	@Override
	public void resume()
	{
		super.resume();
	}

	@Override
	public void dispose()
	{
		super.dispose();
		stage.dispose();
		renderer.dispose();

		// dispose textures and stuff:
		gameFactory.dispose();

		
	}

}
