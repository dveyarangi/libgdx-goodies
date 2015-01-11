package eir.world.environment;

import eir.input.GameInputProcessor;
import eir.rendering.IRenderer;
import eir.world.Level;

public interface IBackground
{

	void draw(IRenderer renderer);

	void update(float delta);
	
	void init( Level level, GameInputProcessor inputController );
	
	void destroy();

	void resize(int width, int height);
}
