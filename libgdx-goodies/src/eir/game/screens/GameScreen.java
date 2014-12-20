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
import eir.world.Level;

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
	
	private IGameUI ui;
	private LevelRenderer renderer;
	private Level level;
	
	private Table table;

	public GameScreen(final Game game, final LevelSetup levelSetup)
	{
		super( game );

		gameFactory = levelSetup.getGameFactory();

		// creating level from level definitions:
		level = levelSetup.createLevel();


	    ui = levelSetup.getUI( stage ); 
	    
	    inputController = levelSetup.getInputController( level, ui );
		
		
		Gdx.input.setInputProcessor( stage );

		level.getBackground().init( gameFactory, inputController );

		Debug.init( level, inputController );

		this.renderer = new LevelRenderer( gameFactory, inputController, level );
		
		if( ui != null)
			ui.init();
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

		renderer.render( delta );
		
		super.render( delta );
		
		if( ui != null)
			ui.update( renderer );
	}

	@Override
	public void resize(final int width, final int height)
	{
		super.resize( width, height );
		inputController.resize( width, height );

		level.getBackground().resize( width, height );
		
		if( ui != null)
			ui.resize( width, height);
		
		
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
