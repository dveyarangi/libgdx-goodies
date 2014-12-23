package eir.game.screens;

import eir.rendering.LevelRenderer;
import eir.world.unit.IUnit;

public interface IGameUI
{

	void init();

	void update(LevelRenderer renderer);

	void resize(int width, int height);

	void setUnitDescriptor(IUnit pickedUnit);

}
