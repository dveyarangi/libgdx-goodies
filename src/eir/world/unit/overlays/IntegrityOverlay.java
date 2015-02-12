package eir.world.unit.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.rendering.IRenderer;
import eir.world.environment.spatial.AABB;
import eir.world.unit.IUnit;
import eir.world.unit.damage.Hull;

/**
 *
 * This is rendering method for unit health overlay
 * @author Fima
 *
 * @param <U>
 */
public class IntegrityOverlay <U extends IUnit> implements IOverlay <U>
{

	@Override
	public void draw( final U unit, final IRenderer renderer )
	{
		Hull hull = unit.getHull();
		if(hull == null)
			//			Debug.log( "Unit has no hull: " + unit );
			return;

		float maxHP = hull.getMaxHitPoints();
		float currHP = hull.getHitPoints();

		float percentage = currHP / maxHP;

		AABB area = unit.getArea();

		float barYOffset = 0.1f;
		float barHeight = unit.size()/10;

		float width = area.rx()*2;
		float filledWidth = width * percentage;

		ShapeRenderer shape = renderer.getShapeRenderer();
		Gdx.gl.glEnable( GL20.GL_BLEND);
		shape.setColor( 1f - percentage, 0, percentage, 1f - percentage );
		shape.begin( ShapeType.Line );

		shape.rect( area.getMinX(), area.getMaxY()- barYOffset, width, barHeight-barYOffset );

		shape.end();

		shape.begin( ShapeType.Filled );
		shape.rect( area.getMinX(), area.getMaxY()- barYOffset, filledWidth, barHeight-barYOffset );
		shape.end();
		Gdx.gl.glDisable( GL20.GL_BLEND);
	}

	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
