package eir.world;

import eir.rendering.IRenderer;
import eir.world.unit.IUnit;

public interface IEffect
{

	public abstract void reset(IUnit unit);

	public abstract void update(float delta);

	public abstract boolean isAlive();

	public abstract void draw(IRenderer renderer);

	public abstract void free();

}