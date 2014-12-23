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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import eir.input.GameInputProcessor;
import eir.input.InputAction;
import eir.input.InputContext;
import eir.input.UIInputProcessor;
import eir.rendering.IRenderer;
import eir.resources.ResourceFactory;
import eir.world.Level;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.unit.IOverlay;
import eir.world.unit.IUnit;
import eir.world.unit.overlays.UnitSymbolOverlay;
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


	private OrthographicCamera camera;


	private static class OverlayBinding 
	{
		boolean isOn;
		IOverlay overlay;		
		public OverlayBinding(IOverlay overlay)
		{
			this(overlay, false);
		}
		public OverlayBinding(IOverlay overlay,boolean isOn)
		{
			this.overlay = overlay;
			this.isOn = isOn;
		}
		public void toggle() { isOn = !isOn; }
		
		public boolean isOn() { return isOn; }
		public void render(IRenderer renderer)
		{
			if( overlay != null && isOn)
				overlay.draw( null, renderer );
		}
	}
	
	private TIntObjectHashMap <OverlayBinding> debugOverlays = new TIntObjectHashMap <OverlayBinding> ();

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
		
		debug.setup(level, inputController);
	}
	
	public void setup( final Level level, final GameInputProcessor inputController )
	{
		UIInputProcessor uiProcessor = inputController.getInputRegistry();
		
		// coordinate grid:
		addOverlay( uiProcessor, Keys.J, new OverlayBinding( 
				new CoordinateGrid(
						level.getWidth(), level.getHeight(),
						camera)
			) );
		
		// navigation mesh
/*		addOverlay( uiProcessor, Keys.K, new OverlayBinding( 
				new NavMeshOverlay( level.getEnvironment().getGroundMesh()) 
			));*/
		
		// spatial hashmap debug
		addOverlay( uiProcessor, Keys.L, new OverlayBinding(
				new SpatialHashMapLook( (SpatialHashMap) level.getEnvironment().getIndex() )
			));
		
		addOverlay( uiProcessor, Keys.L, new OverlayBinding(null) {
				@Override
				public void toggle() {
					super.toggle();
						for(IUnit unit : debug.level.getUnits()) 
							unit.toggleOverlay( FACTION_OID );
					}
				}
			);
		
		debugRenderer.render( level.getEnvironment().getPhyisics(), camera.combined );
				
		addOverlay( uiProcessor, Keys.L, new OverlayBinding(new IOverlay() {

				@Override
				public void draw(Object unit, IRenderer renderer)
				{
					debugRenderer.render( level.getEnvironment().getPhyisics(), camera.combined );
				}
	
				@Override
				public boolean isProjected() {  return false; }
			
				}
			));
		
	}

	private Debug( final Level level, final GameInputProcessor inputController)
	{

		this.camera = inputController.getCamera();

		debugGrid = new CoordinateGrid(
				level.getWidth(), level.getHeight(),
				camera);

		spatialGrid = new SpatialHashMapLook( (SpatialHashMap) level.getEnvironment().getIndex() );

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

		for(OverlayBinding binding : debugOverlays.valueCollection())
		{
			binding.render( renderer );
		}

	}

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
	
	private void addOverlay(final UIInputProcessor uiProcessor, int keyCode, final OverlayBinding binding )
	{
		debugOverlays.put( keyCode, binding );
		uiProcessor.registerAction( keyCode, new InputAction() {
			@Override public void execute( final InputContext context )	{ binding.toggle(); }
		});
	}

	public static final int FACTION_OID = -10;

	public static void registerOverlays( final TIntObjectHashMap<IOverlay> overlays )
	{
		overlays.put( FACTION_OID, new UnitSymbolOverlay() );
	}
}
