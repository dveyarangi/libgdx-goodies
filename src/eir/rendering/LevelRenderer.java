package eir.rendering;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import yarangi.numbers.RandomUtil;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.debug.Debug;
import eir.input.GameInputProcessor;
import eir.resources.AnimationHandle;
import eir.resources.ResourceFactory;
import eir.world.IEffect;
import eir.world.Level;
import eir.world.unit.IUnit;
import eir.world.unit.overlays.IOverlay;
import eir.world.unit.overlays.IntegrityOverlay;
import eir.world.unit.overlays.WeaponOverlay;
import gnu.trove.map.hash.TIntObjectHashMap;

public class LevelRenderer implements IRenderer
{
	private GameInputProcessor inputController;

	private Level level;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private final ResourceFactory gameFactory;

	private final List <IEffect> effects;

	private TIntObjectHashMap <IOverlay> overlays;

	public static final int INTEGRITY_OID = 1;
	public static final int WEAPON_OID = 2;
	public static final int HEX_OID = 3;
	private RayHandler rayHandler;
	
	private ColorTheme colorTheme;

	public LevelRenderer( final ResourceFactory gameFactory, final GameInputProcessor inputController, final Level level )
	{
		
		this.gameFactory = gameFactory;
		this.inputController = inputController;
		this.level = level;
		
		this.colorTheme = new ColorTheme();
		colorTheme.setTheme( RandomUtil.N(360) );

		
		rayHandler = new RayHandler( level.getEnvironment().getPhyisics() );

		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();

		effects = new LinkedList <IEffect> ();

		this.overlays = new TIntObjectHashMap <IOverlay> ();

		overlays.put( INTEGRITY_OID, new IntegrityOverlay() );
		overlays.put( WEAPON_OID, new WeaponOverlay() );
//		overlays.put( HEX_OID, new HexRegularOverlay() );

		Debug.registerOverlays(overlays);
	}

	public void render(final float delta)
	{

		// setting renderers to camera view:
		// TODO: those are copying matrix arrays, maybe there is a lighter way to do this
		// TODO: at least optimize to not set if camera has not moved
		batch.setProjectionMatrix( inputController.getCamera().projection );
		batch.setTransformMatrix( inputController.getCamera().view );

		shapeRenderer.setProjectionMatrix( inputController.getCamera().projection);
		shapeRenderer.setTransformMatrix( inputController.getCamera().view );
		
		
		level.getBackground().draw( this );
		
		rayHandler.setCombinedMatrix( inputController.getCamera().combined );
		rayHandler.updateAndRender();
		

		
		
		batch.begin();
		
		level.draw( this );

		for(IUnit unit : level.getUnits())
		{
			if(unit.isAlive())
			{
				unit.draw( this );
			}
		}
		
		for(IEffect effect : effects)
		{
			effect.draw( this );
		}


		Iterator <IEffect> effectIt = effects.iterator();
		while(effectIt.hasNext())
		{
			IEffect effect = effectIt.next();
			effect.update( delta );
			if(!effect.isAlive())
			{

				effectIt.remove();
				effect.free();
			}

		}



		batch.end();

		for(IUnit unit : level.getUnits())
		{


			for(int oidx = 0; oidx < unit.getActiveOverlays().size(); oidx ++)
			{
				int oid = unit.getActiveOverlays().get( oidx );
				overlays.get( oid ).draw( unit, this );
			}
			if( unit.isHovered() )
			{
				for(int oidx = 0; oidx < unit.getHoverOverlays().size(); oidx ++)
				{
					int oid = unit.getHoverOverlays().get( oidx );
					overlays.get( oid ).draw( unit, this );
				}
			}
		}



		//////////////////////////////////////////////////////////////////
		// debug rendering
		inputController.draw( this );

		Debug.debug.draw( this );
	}


/*	@Override
	public void addEffect( final EffectDef effectDef, Vector2 position, Vector2 velocity, float angle)
	{
		Animation animation = getAnimation( effectDef.getAnimation() );

		Effect effect =	Effect.getEffect( animation, effectDef.getSize(), position, angle, effectDef.getTimeModifier());
		
		effects.add( effect );
	}*/
	@Override
	public void addEffect( final IEffect effect)
	{
		assert (effect != null);
		effects.add( effect );
	}

	@Override
	public RayHandler getRayHandler() { return rayHandler; }
	

	public void dispose()
	{

		// dispose sprite batch renderer:
		batch.dispose();

		// dispose shape renderer:
		shapeRenderer.dispose();
		
		rayHandler.dispose(); 
	}

	@Override
	public SpriteBatch getSpriteBatch() { return batch; }

	@Override
	public ShapeRenderer getShapeRenderer() { return shapeRenderer;	}

	@Override
	public Animation getAnimation( final AnimationHandle handle )
	{
		return gameFactory.getAnimation( handle );
	}

	@Override
	public ColorTheme getColorTheme() { return colorTheme; }
}
