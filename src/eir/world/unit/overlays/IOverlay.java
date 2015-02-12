package eir.world.unit.overlays;

import eir.rendering.IRenderer;

/**
 * Unit decorator
 * @author Fima
 *
 */
public interface IOverlay <U>
{

	public void draw( U unit, final IRenderer renderer );

	/***
	 * If false, overlay will be rendered in absolute screen coordinates system
	 * @return
	 */
	public boolean isProjected();
}
