package eir.world.environment.spatial;

import yarangi.java.InvokationMapper;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import eir.math.Geometry;

/**
 * Represents an axis-aligned bounding box.
 *
 */
public class AABB
{
	/**
	 * center point
	 */
	private Vector2 ref = Vector2.Zero.cpy();

	/**
	 * Size
	 */
	private Vector2 dim = Vector2.Zero.cpy();


	/**
	 * marker for {@link SpatialHashMap} queries.
	 */
	private int passId;
	
	private boolean isLine = false;

//	private static InvokationMapper amap = new InvokationMapper();

	/**
	 * Pool of AABB objects
	 */
	public static final Pool<AABB> pool =
			new Pool<AABB>()
			{
				@Override
				protected AABB newObject()
				{
					return new AABB();
				}
			};


	static InvokationMapper mapper = new InvokationMapper();

	/**
	 * Returns specified AABB to the pool.
	 */
	public static void free(final AABB aabb) { pool.free( aabb ); }

	/**
	 * Returns this AABB to the pool.
	 */
	public void free() { AABB.free( this ); }

	public static AABB createSquare(final float x, final float y, final float r)
	{
		AABB aabb = pool.obtain();
		return aabb.update( x, y, r, r, false );
	}
	public static AABB createSquare(final Vector2 center, final float r)
	{
		AABB aabb = pool.obtain();
		return aabb.update(center.x, center.y, r, r, false);
	}

	public static AABB createFromEdges(final float x1, final float y1, final float x2, final float y2)
	{
		float rx = (x2 - x1) / 2.f;
		float ry = (y2 - y1) / 2.f;

		AABB aabb = pool.obtain();
		return aabb.update(x1+rx, y1+ry, rx, ry, false);
	}

	public static AABB createFromCenter(final float cx, final float cy, final float rx, final float ry)
	{
		AABB aabb = pool.obtain();
		return aabb.update(cx, cy, rx, ry, false);
	}

	public static AABB createPoint(final float x, final float y)
	{
		AABB aabb = pool.obtain();
		return aabb.update(x, y, 0, 0, false);
	}
	
	public static AABB createLine(float x1, float y1, float x2, float y2)
	{
		AABB aabb = pool.obtain();
		
		float cx = (x2-x1)/2+x1;
		float cy = (y2-y1)/2+y1;
		float rx = Math.abs(x2-x1)/2;
		float ry = Math.abs(y2-y1)/2;
		
		return aabb.update(cx, cy, rx, ry, true);
	}

	/**
	 * @param aabb
	 */
	public static AABB copy(final AABB aabb)
	{
		return pool.obtain().copyFrom( aabb );
	}

	protected AABB()
	{
//		System.out.println("AABB allocated from " + mapper.record( 4 ) );;
//		amap.record();
		this.ref = Vector2.Zero.cpy();
	}
	/**
	 * @param aabb
	 */
	public AABB copyFrom(final AABB aabb)
	{
		this.ref.set( aabb.ref );
		this.dim.set( aabb.dim );
		this.isLine = aabb.isLine;
		return this;
	}

	/**
	 * C'tor
	 * @param x box center x
	 * @param y box center y
	 * @param r half box width
	 * @param a box orientation (degrees)
	 */

	public AABB update(final float x, final float y, final float rx, final float ry, boolean isLine)
	{

		this.ref.set( x, y );
		this.dim.set( rx, ry );
		this.isLine = isLine;

		return this;
	}

	public final Vector2 getAnchor() { return ref; }

	public final Vector2 getDimensions() { return dim; }

	final public void translate(final float dx, final float dy) {
		ref.add(dx, dy);
	}

	public void move(final float x, final float y) {
		ref.set( x, y );
	}

	public void fitTo(final float radius)
	{
		if(radius < 0)
			throw new IllegalArgumentException("AABB radius must be positive.");

		if(dim.x > dim.y)
		{
			dim.x = radius;
			dim.y *= dim.y/radius;
		}
		else
		{
			dim.x *= dim.x/radius;
			dim.y = radius;

		}
	}

	public final boolean overlaps(final float minx, final float miny, final float maxx, final float maxy, boolean isLine)
	{
		if(!this.isLine() && !isLine)
			return ( maxx >= ref.x-dim.x && maxx <= ref.x+dim.x ||
				     minx >= ref.x-dim.x && minx <= ref.x+dim.x ||
				     minx >= ref.x-dim.x && maxx <= ref.x+dim.x ||
				     minx <= ref.x-dim.x && maxx >= ref.x+dim.x
				  ) && (
				     maxy >= ref.y-dim.y && maxy <= ref.y+dim.y ||
				     miny >= ref.y-dim.y && miny <= ref.y+dim.y ||
				     miny >= ref.y-dim.y && maxy <= ref.y+dim.y ||
				     miny <= ref.y-dim.y && maxy >= ref.y+dim.y
				   );
		else
		if(this.isLine() && isLine)
		{
			return Geometry.calcIntersection( this.getMinX(),  this.getMinY(), 
											  this.getMaxX() - this.getMinX(),
											  this.getMaxY() - this.getMinY(),
											  minx,  miny, 
											  maxx - minx,
											  maxy - miny
											  );
		}
		else
		{
			float bminx, bminy, bmaxx, bmaxy;
			float lminx, lminy, lmaxx, lmaxy;
			if(this.isLine())
			{
				bminx = minx;
				bminy = miny;
				bmaxx = maxx;
				bmaxy = maxy;
				lminx = this.getMinX();
				lminy = this.getMinY();
				lmaxx = this.getMaxX();
				lmaxy = this.getMaxY();
			}
			else
			{
				lminx = minx;
				lminy = miny;
				lmaxx = maxx;
				lmaxy = maxy;
				bminx = this.getMinX();
				bminy = this.getMinY();
				bmaxx = this.getMaxX();
				bmaxy = this.getMaxY();
			}
			
			return Geometry.calcIntersection( lminx,  lminy, 
											  lmaxx - lminx,
											  lmaxy - lminy,
											  bminx, bminy, bmaxx-bminx, 0)
		        || Geometry.calcIntersection( lminx,  lminy, 
						                      lmaxx - lminx,
						                      lmaxy - lminy,
						                      bminx, bmaxy, bmaxx-bminx, 0)
			    || Geometry.calcIntersection( lminx,  lminy, 
						                      lmaxx - lminx,
						                      lmaxy - lminy,
						                      bminx, bminy, 0, bmaxy-bminy)
				|| Geometry.calcIntersection( lminx,  lminy, 
						                      lmaxx - lminx,
						                      lmaxy - lminy,
						                      bmaxx, bminy, 0, bmaxy-bminy);
		}
	}

	public boolean overlaps(final AABB area)
	{
		return overlaps( area.getMinX(), area.getMinY(), area.getMaxX(), area.getMaxY(), area.isLine );

	}


	@Override
	public boolean equals(final Object o)
	{
		return o == this;
	}

	@Override
	public String toString()
	{
		return "AABB [loc:" + ref.x + ":" + ref.y + "; r:(" + dim.x +"," +dim.y +");]";
	}


	public final float getMinX() { return ref.x - dim.x; }
	public final float getMaxX() { return ref.x + dim.x; }
	public final float getMinY() { return ref.y - dim.y; }
	public final float getMaxY() { return ref.y + dim.y; }

	public final float getCenterX() { return ref.x;	}
	public final float getCenterY() { return ref.y;	}
	public final float cx() { return getCenterX(); }
	public final float cy() { return getCenterY(); }

	public final float getHalfWidth()  { return dim.x; }
	public final float getHalfHeight() { return dim.y; }
	public final float rx() { return getHalfWidth();  }
	public final float ry() { return getHalfHeight(); }


	public final int getPassId() { return passId; }

	public final void setPassId(final int id) {	this.passId = id; }

	public boolean contains(final float x, final float y)
	{
		return x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY();
	}



	/**
	 * @param ox
	 * @param oy
	 * @param dx
	 * @param dy
	 * @return
	 */
	public boolean crosses(final float ox, final float oy, final float dx, final float dy)
	{
		throw new UnsupportedOperationException();
	}


	public boolean isLine() { return isLine; }
}
