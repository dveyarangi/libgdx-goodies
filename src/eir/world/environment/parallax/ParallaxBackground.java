/**
 *
 */
package eir.world.environment.parallax;

import java.util.ArrayList;
import java.util.List;

import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import eir.input.GameInputProcessor;
import eir.rendering.ColorTheme;
import eir.rendering.IRenderer;
import eir.resources.AnimationHandle;
import eir.world.IEffect;
import eir.world.Level;
import eir.world.environment.IBackground;

/**
 * @author dveyarangi
 *
 */
public class ParallaxBackground implements IBackground
{
	public static class Layer implements Comparable <Layer>
	{
		float width, height;
		private float depth;
		IBackground back;
		private Vector2 scroll;
		private boolean tiling;

		public Layer(float width, float height,final float depth, IBackground back, final Vector2 scroll, final boolean tiling)
		{
			this.width = width;
			this.height = height;
			this.depth = depth;
			this.back = back;
			this.scroll = scroll;
			this.tiling = tiling;
		}

		@Override
		public int compareTo(final Layer o)
		{
			return Float.compare( o.depth, depth);
		}

	}
	
	private IRenderer renderer;
	
	public ParallaxBackground() {}
	public ParallaxBackground(final ArrayList <Layer> layers)
	{
		this.layers = layers;
	}

	private List<Layer> layers = new ArrayList <Layer> ();
	private GameInputProcessor processor;
	/**
	 * Camera for layers of background.
	 */
	private OrthographicCamera camera = new OrthographicCamera();

	private float time = 0;

	@Override
	public void init(Level level, GameInputProcessor inputController )
	{
		// TODO Auto-generated method stub

		this.processor = inputController;

		this.renderer = new IRenderer()
		{
			SpriteBatch batch = new SpriteBatch();
			ShapeRenderer shapeRenderer = new ShapeRenderer();

			@Override
			public ShapeRenderer getShapeRenderer() { return shapeRenderer; }

			@Override
			public SpriteBatch getSpriteBatch() { return batch; }

			@Override
			public Animation getAnimation(AnimationHandle handle) { return null;}

			@Override
			public void addEffect(IEffect effect) { }

			@Override
			public RayHandler getRayHandler() { return null; }

			@Override
			public ColorTheme getColorTheme()
			{
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		for(int lidx = 0; lidx < layers.size(); lidx ++)
			layers.get(lidx).back.init( level, processor );

	}

	@Override
	public void resize(final int width, final int height)
	{
		camera.setToOrtho( false, width, height );
	}


	@Override
	public void update(final float delta)
	{
		time += delta;
		for(int lidx = 0; lidx < layers.size(); lidx ++)
			layers.get(lidx).back.update( delta );
	}


	@Override
	public void draw(final IRenderer _renderer)
	{
		SpriteBatch batch = this.renderer.getSpriteBatch();
		for(int lidx = 0; lidx < layers.size(); lidx ++)
		{
			Layer layer = layers.get(lidx);
			camera.position.x = processor.getCamera().position.x / layer.depth;
			camera.position.y = processor.getCamera().position.y / layer.depth;
//			camera.zoom = processor.getCamera().zoom / layer.depth;
			camera.update();

			batch.setProjectionMatrix( camera.projection );
			batch.setTransformMatrix( camera.view );

			float width = layer.width;
			float height = layer.height;

			float xOffset = -width/2, yOffset = -height/2;
			if(layer.scroll != null)
			{
				xOffset = layer.scroll.x * time - width/2;
				yOffset = layer.scroll.y * time - height/2;
			}

			int minXIdx = 0, maxXIdx = 1, minYIdx = 0, maxYIdx = 1;

			if(layer.tiling)
			{
				float screenMinX = camera.position.x - camera.viewportWidth/2*camera.zoom;
				float screenMinY = camera.position.y - camera.viewportHeight/2*camera.zoom;
					// higher right screen corner in world coordinates
				float screenMaxX = camera.position.x + camera.viewportWidth/2*camera.zoom;
				float screenMaxY = camera.position.y + camera.viewportHeight/2*camera.zoom;
				minXIdx = (int) Math.ceil( (screenMinX - xOffset) / width ) -1;
				maxXIdx = (int) Math.ceil( (screenMaxX - xOffset) / width );
				minYIdx = (int) Math.ceil( (screenMinY - yOffset) / height ) -1;
				maxYIdx = (int) Math.ceil( (screenMaxY - yOffset) / height );
			}

			for(int xIdx = minXIdx; xIdx < maxXIdx; xIdx ++)
			{
				for(int yIdx = minYIdx; yIdx < maxYIdx; yIdx ++)
				{
					layer.back.draw( renderer );
//					batch.draw( layer.txr, xIdx*width + xOffset, yIdx*height + yOffset  );
				}
			}
		}
	}
	
	@Override
	public void destroy()
	{
		for(int lidx = 0; lidx < layers.size(); lidx ++)
			layers.get(lidx).back.destroy();
	}
}
