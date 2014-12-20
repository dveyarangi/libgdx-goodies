package eir.rendering;

import yarangi.math.Angles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class PolygonRenderer
{

	public static void renderSegment(ShapeRenderer shape, float cx, float cy, float angle, float minradius, float maxradius, float widthAngle)
	{
		shape.begin(ShapeType.Filled);
		float da = widthAngle/100f;
		float cosp, cosn, sinp, sinn;
		for(float a = -widthAngle/2+da; a <= widthAngle/2-da; a += 2*da)
		{
			cosn = Angles.COS( a+angle-da );
			cosp = Angles.COS( a+angle+da );
			sinp = Angles.SIN( a+angle+da );
			sinn = Angles.SIN( a+angle-da );
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
