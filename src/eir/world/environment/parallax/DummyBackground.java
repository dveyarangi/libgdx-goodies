package eir.world.environment.parallax;

import com.badlogic.gdx.Gdx;

import eir.input.GameInputProcessor;
import eir.rendering.IRenderer;
import eir.world.Level;
import eir.world.environment.IBackground;

public class DummyBackground implements IBackground
{

	@Override
	public void draw(IRenderer renderer)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Level level, GameInputProcessor inputController)
	{
		Gdx.app.log(this.getClass().toString(), "Dummy background is used.");
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
		
	}

}
