package eir.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.debug.Debug;
import eir.game.LevelSetup;

public class LoadingScreen  extends AbstractScreen
{
	private LevelSetup levelSetup;

	private ShapeRenderer renderer = new ShapeRenderer();

	private int minx, miny, lw, lh;

	private float culloutTime;

	private float barProgress = 0;

	public LoadingScreen(final Game game, final LevelSetup levelSetup)
	{
		super(game);

		this.levelSetup = levelSetup;
		
		levelSetup.init();
	}

	@Override
	public void render(final float delta)
	{
		
		
		super.render( delta );
//		Debug.log( levelSetup.getState() + " " + levelSetup.getPercentage());
		float progress = levelSetup.getGameFactory().loadResources();

		// smoothing a little:
		barProgress += (progress - barProgress)/3;

		renderer.begin( ShapeType.Line );
		renderer.rect( minx, miny, lw, lh );
		renderer.end();
		renderer.begin( ShapeType.Filled );
		renderer.rect( minx, miny, barProgress*lw, lh );
		renderer.end();

		if(progress == 1)
		{
			culloutTime += delta;
			if(culloutTime > 0.1)
			{
				this.dispose();
				game.setScreen( new GameScreen( game, levelSetup ) );
				Debug.log( "Finished loading level");
			}
		}
	}
    @Override
	public void resize(
            final int width,
            final int height )
    {
    	this.minx = width / 2 - width / 4;
    	int maxx = width / 2 + width / 4;
    	lw = maxx - minx;
    	this.miny = height / 2 - height / 100;
    	int maxy = height / 2 + height / 100;
    	lh = maxy - miny;
    }
}
