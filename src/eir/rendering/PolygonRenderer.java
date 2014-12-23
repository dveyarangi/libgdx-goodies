package eir.rendering;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class PolygonRenderer
{

	public static void renderSegment(ShapeRenderer shape, float cx, float cy, float angle, float minradius, float maxradius, float widthAngle)
	{
		shape.begin(ShapeType.Filled);
		float da = widthAngle/100f;
		float cosp, cosn, sinp, sinn;
		for(float a = -widthAngle/2+da; a <= widthAngle/2-da; a += 2*da)
		{
			cosn = MathUtils.cos( a+angle-da );
			cosp = MathUtils.cos( a+angle+da );
			sinp = MathUtils.sin( a+angle+da );
			sinn = MathUtils.sin( a+angle-da );
			if(minradius == 0)
			{
				shape.triangle( cx, cy,
						cx + maxradius * cosn,
						cy + maxradius * sinn,
						cx + maxradius * cosp,
						cy + maxradius * sinp
						);

			}
			else
			{
				shape.triangle( 
						cx + minradius * cosn,
						cy + minradius * sinn,
						cx + maxradius * cosn,
						cy + maxradius * sinn,
						cx + maxradius * cosp,
						cy + maxradius * sinp
					);
				shape.triangle( 
						cx + minradius * cosn,
						cy + minradius * sinn,
						cx + maxradius * cosp,
						cy + maxradius * sinp,
						cx + minradius * cosp,
						cy + minradius * sinp
					);
			}

		}
		shape.end();
	}
}
