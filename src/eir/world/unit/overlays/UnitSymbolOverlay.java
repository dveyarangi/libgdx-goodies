package eir.world.unit.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.rendering.IRenderer;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.IOverlay;
import eir.world.unit.IUnit;

/**
 *
 * This is rendering method for unit health overlay
 * @author Fima
 *
 * @param <U>
 */
public class UnitSymbolOverlay <U extends IUnit> implements IOverlay <U>
{

	@Override
	public void draw( final U unit, final IRenderer renderer )
	{
		ShapeRenderer shape = renderer.getShapeRenderer();


		AABB body = unit.getArea();
		ISpatialObject target = unit.getTarget();

		shape.begin(ShapeType.Filled);
		Color color = unit.getFaction().getColor();
		shape.setColor(color.r,color.g,color.b,0.5f);
		shape.circle(body.getAnchor().x, body.getAnchor().y, unit.getSize() / 2);
		shape.end();

		if( target != null)
		{
			shape.begin(ShapeType.Line);
			shape.line( body.getAnchor().x, body.getAnchor().y,
					target.getArea().getAnchor().x, target.getArea().getAnchor().y );
			shape.end();
			shape.begin(ShapeType.Line);
			shape.circle( target.getArea().cx(),
					      target.getArea().cy(),
					      target.getArea().rx());
			shape.end();

		}
	}

	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
