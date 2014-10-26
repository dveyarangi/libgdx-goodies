/**
 *
 */
package eir.debug;

import java.util.HashMap;
import java.util.Map;

import yarangi.java.InvokationMapper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import eir.input.GameInputProcessor;
import eir.input.InputAction;
import eir.input.InputContext;
import eir.input.UIInputProcessor;
import eir.rendering.IRenderer;
import eir.resources.ResourceFactory;
import eir.world.Level;

import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.unit.IOverlay;
import eir.world.unit.Unit;
import eir.world.unit.overlays.UnitSymbolOverlay;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author dveyarangi
 *
 */
public class Debug
{

	public static final String TAG = "debug";

	public static Debug debug;

	public Level level;

	private CoordinateGrid debugGrid;

	private SpatialHashMapLook spatialGrid;

	private BitmapFont font;

	private OrthographicCamera camera;

	public boolean drawCoordinateGrid = false;
	public boolean drawNavMesh = false;
	public boolean drawSpatialHashmap = false;
	public boolean drawFactions = false;
	public boolean drawBox2D = false;
	public boolean drawHexes = false;

	private static Map <String, Long> timings = new HashMap <String, Long> ();

	private int frameCount = 0;
	/**
	 * measures delta time average
	 */
	private static final int SAMPLES = 60;

	public static final BitmapFont FONT = ResourceFactory.loadFont("skins//fonts//default", 0.05f);

	private float [] deltas = new float [SAMPLES];
	private float deltaPeak = 0;
	private boolean isFirstBatch = true;

	private FPSLogger fpsLogger = new FPSLogger();

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	private static InvokationMapper mapper = new InvokationMapper();

	public static void init( final Level level, final GameInputProcessor inputController )
	{
		debug = new Debug(  level, inputController  );
	}

	private Debug( final Level level, final GameInputProcessor inputController)
	{
		this.level = level;

		this.camera = inputController.getCamera();

		debugGrid = new CoordinateGrid(
				level.getWidth(), level.getHeight(),
				camera);

		spatialGrid = new SpatialHashMapLook( (SpatialHashMap) level.getEnvironment().getIndex() );

		font = ResourceFactory.loadFont("skins//fonts//default", 0.05f);

		debugRenderer.setDrawBodies(true);
		debugRenderer.setDrawAABBs(false);
		debugRenderer.setDrawVelocities(true);
	}

	public void update(final float delta)
	{
		int sampleIdx = frameCount ++ % SAMPLES;
		deltas[sampleIdx] = delta;
		if(sampleIdx >= SAMPLES-1)
		{
			float maxDelta = Float.MIN_VALUE;
			for(int idx = 0; idx < SAMPLES; idx ++)
			{
				float sample = deltas[idx];

				if(isFirstBatch)
				{
					continue;
				}

				if(sample > maxDelta)
				{
					maxDelta = sample;
				}

				if(sample > deltaPeak)
				{
					deltaPeak = sample;
					//					log("New delta maximum: " + deltaPeak);
				}
			}
			//			log("Average delta time: " + deltaSum / SAMPLES);
			isFirstBatch = false;
		}
		deltaPeak -= 0.00001f;
		if(!isFirstBatch && delta > deltaPeak * 0.5)
		{
			//			log("Delta peak: " + delta);
			//			deltaPeak *= 2;
		}
	}

	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(final IRenderer renderer)
	{
		GL20 gl = Gdx.gl;
		gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		// a libgdx helper class that logs the current FPS each second
		//fpsLogger.log();

		if(drawCoordinateGrid)
		{
			debugGrid.draw( renderer );
		}

		if(drawSpatialHashmap)
		{
			spatialGrid.draw( renderer );
		}

		if(drawNavMesh)
		{
			drawNavMesh( level.getEnvironment().getGroundMesh(), renderer );
//			drawNavMesh( level.getEnvironment().getAirMesh(), batch, shape );
		}

		if(drawFactions)
		{

		}

		if(drawBox2D)
		{
			debugRenderer.render(level.getEnvironment().getPhyisics(), camera.combined);
		}

	}

	private void drawNavMesh(final NavMesh navMesh, final IRenderer renderer)
	{
		
		if(navMesh.getNodesNum() == 0)
			return;
		ShapeRenderer shape = renderer.getShapeRenderer();
		SpriteBatch batch = renderer.getSpriteBatch();
		NavNode srcNode;

		
		shape.begin(ShapeType.Filled);
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			shape.setColor( 0, 1, 0, 1f );
			srcNode = navMesh.getNode( fidx );

			shape.circle( srcNode.getPoint().x, srcNode.getPoint().y, 1 );
		}
		shape.end();

		batch.begin();
		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		{
			srcNode = navMesh.getNode( fidx );
			font.draw( batch, String.valueOf( fidx ), srcNode.getPoint().x+1, srcNode.getPoint().y+1 );
		}
		batch.end();

		shape.begin(ShapeType.Line);
		shape.setColor( 0, 1, 0, 0.5f );

		TIntObjectIterator<NavEdge> i = navMesh.getEdgesIterator();
		
		i.advance();
		while( i.hasNext() )
		{
			NavEdge e = i.value();
			Vector2 p1 = e.getNode1().getPoint();
			Vector2 p2 = e.getNode2().getPoint();
			shape.line(p1.x, p1.y, p2.x, p2.y);
			i.advance();
		}
		//		for(int fidx = 0; fidx < navMesh.getNodesNum(); fidx ++)
		//		{
		//			srcNode = navMesh.getNode( fidx );
		//
		//			shape.setColor( 0, 1, 0, 0.5f );
		//			for(NavNode tarNode : srcNode.getNeighbors())
		//			{
		//				shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
		//			}
		//		}
		shape.end();
	}

	public static void toggleNavMesh() { debug.drawNavMesh =! debug.drawNavMesh; }
	public static void toggleCoordinateGrid() { debug.drawCoordinateGrid =! debug.drawCoordinateGrid; }
	public static void toggleSpatialGrid() { debug.drawSpatialHashmap =! debug.drawSpatialHashmap; }
	public static void toggleFactions()
	{
		debug.drawFactions =! debug.drawFactions;

		for(Unit unit : debug.level.getUnits())
		{
			unit.toggleOverlay( FACTION_OID );
		}

	}
	public static void toggleBox2D() { debug.drawBox2D =! debug.drawBox2D; }


	public static void startTiming(final String processName)
	{
		log("Starting timer for " + processName);
		timings.put( processName, System.currentTimeMillis() );
	}
	public static void stopTiming(final String processName)
	{
		long duration = System.currentTimeMillis() - timings.get( processName );
		log(processName + " finished in " + duration + "ms");
	}

	public static void log(final String message)
	{
		String [] path = new Exception().getStackTrace()[1].getClassName().split( "\\." );

		Gdx.app.log( path[path.length-2] + "#" + path[path.length-1], ">> " + message);
	}

	public static void registerDebugActions( final UIInputProcessor uiProcessor )
	{
		uiProcessor.registerAction( Keys.J, new InputAction() {
			@Override
			public void execute( final InputContext context )	{ toggleCoordinateGrid(); }
		});
		uiProcessor.registerAction( Keys.K, new InputAction() {
			@Override
			public void execute( final InputContext context )	{ Debug.toggleNavMesh(); }
		});
		uiProcessor.registerAction( Keys.L, new InputAction() {
			@Override
			public void execute( final InputContext context )	{ Debug.toggleSpatialGrid(); }
		});
		uiProcessor.registerAction( Keys.SEMICOLON, new InputAction() {
			@Override
			public void execute( final InputContext context )	{ Debug.toggleFactions(); }
		});
		uiProcessor.registerAction( Keys.APOSTROPHE, new InputAction() {
			@Override
			public void execute( final InputContext context )	{ Debug.toggleBox2D(); }
		});
	}
	public static final int FACTION_OID = -10;

	public static void registerOverlays( final TIntObjectHashMap<IOverlay> overlays )
	{
		overlays.put( FACTION_OID, new UnitSymbolOverlay() );
	}
}
