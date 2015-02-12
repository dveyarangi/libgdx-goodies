/**
 *
 */
package eir.resources;

import eir.debug.Debug;
import eir.rendering.ITextureGenerator;
import eir.rendering.IUnitRenderer;
import eir.resources.levels.IUnitDef;
import eir.resources.levels.LevelDef;
import eir.world.environment.Asteroid;
import eir.world.unit.UnitsFactory;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;


/**
 * TODO: this class becomes crumbled, split to rendering and in-game stuff (them probably should go to level)
 *
 * @author dveyarangi
 */
public class ResourceFactory
{

	public static final String TAG = ResourceFactory.class.getSimpleName();

	private final AssetManager manager;
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Set <AnimationHandle> animationHandles = new HashSet <AnimationHandle> ();

	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <AnimationHandle, Animation> animationCache = new HashMap <AnimationHandle, Animation> ();
	
	
	TIntObjectHashMap<IUnitRenderer> renderers = new TIntObjectHashMap<IUnitRenderer> ();

	
	private EnhancedTextureLoader textureLoader;
	
	public ResourceFactory()
	{
		FileHandleResolver resolver = new InternalFileHandleResolver();
		this.manager = new AssetManager( resolver );
		
		manager.setLoader( ShaderProgram.class, new ShaderLoader( resolver ) );
		
		manager.setLoader( PolygonShape.class, new PolygonLoader( resolver )  );
		
		textureLoader = new EnhancedTextureLoader( resolver );
		manager.setLoader( Texture.class, textureLoader  );

		// TODO: redundant
		registerSharedResources();

	}
	public void dispose()
	{
		manager.dispose();
		animationCache.clear();
	}

	/**
	 * @param string
	 * @return
	 */
	public LevelDef readLevelDefs(final String levelName, final UnitsFactory unitsFactory)
	{
		LevelLoader loader = new LevelLoader("data/levels/");

		LevelDef levelDef = loader.readLevel( levelName, this, unitsFactory );

		return levelDef;
	}

	public final static TextureHandle ROCKET_TXR = TextureHandle.get( "anima//bullets//rocket01.png" );
	public final static TextureHandle FIREBALL_TXR = TextureHandle.get( "anima//bullets//fireball.png" );
	public final static TextureHandle STRIPE_TXR = TextureHandle.get( "anima//bullets//stripe.png" );

	public final static TextureHandle CANNON_HYBRID_TXR = TextureHandle.get( "anima//cannons//cannon_hybrid_01.png" );
	public final static TextureHandle CANNON_FAN_TXR = TextureHandle.get( "anima//cannons//fan_canon_01.png" );

	public final static TextureHandle SPAWNER_TXR = TextureHandle.get( "anima//structures//spawner01.png" );
	public final static TextureHandle BIRDY_TXR = TextureHandle.get( "anima//gears//birdy_02.png" );

	public static final TextureHandle CROSSHAIR_TXR = TextureHandle.get( "ui//crosshair.png" );


	private void registerSharedResources()
	{

/*		registerAnimation( CROSSHAIR_ANIM);
		registerAnimation( SMOKE_ANIM );
		registerAnimation( EXPLOSION_02_ANIM );
		registerAnimation( EXPLOSION_03_ANIM );
		registerAnimation( EXPLOSION_04_ANIM );
		registerAnimation( EXPLOSION_05_ANIM );*/

//		registerTexture( CROSSHAIR_TXR );
/*		registerTexture( FIREBALL_TXR );
		registerTexture( ROCKET_TXR );
		registerTexture( CANNON_HYBRID_TXR );
		registerTexture( CANNON_FAN_TXR );
		registerTexture( STRIPE_TXR );

		registerTexture( SPAWNER_TXR );
		registerTexture( BIRDY_TXR );*/

	}

	public float loadResources()
	{
		if(manager.update())
		{
			loadAnimations();
			
			loadRenderers();

			return 1;
		}
		

		return manager.getProgress();
	}
	
	private void loadRenderers()
	{
		for(Object renderer : renderers.values())
			((IUnitRenderer)renderer).init( this );
	}
	
	public TextureHandle registerTextureGenerator( final String name, ITextureGenerator generator )
	{
		textureLoader.registerGenerator(name, generator);
		manager.load( name, Texture.class );
		
		return TextureHandle.get( name );
	}


	public PolygonalModelHandle registerModelHandle( final PolygonalModelHandle model )
	{
		manager.load( model.getPath(), PolygonShape.class );

		return model;
	}

/*	public Body loadBody(final String modelId, final Asteroid asteroid)
	{
		String modelFile = createBodyPath(modelId);
		// 0. Create a loader for the file saved from the editor.
		BodyLoader loader = new BodyLoader(Gdx.files.internal( modelFile));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.position.set(asteroid.getPosition());
		bd.angle = (float)(asteroid.getAngle()/(2f*Math.PI));

		bd.type = BodyType.StaticBody;

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;

		// 3. Create a Body, as usual.
		Body body = context.environment.getPhyisics().createBody(bd);

		body.setUserData( MazeType.ASTEROID );
		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(body, "Name", fd, asteroid.getSize());

		return body;
	}*/

	public PolygonalModel getPolygonalModel( final Asteroid asteroid, final PolygonalModelHandle model )
	{
		PolygonShape shape = manager.get( model.getPath(), PolygonShape.class );

		return new PolygonalModel(
				shape.getVertices(),
				shape.getOrigin(),
				asteroid.getPosition(),
				asteroid.getSize(),
				asteroid.getAngle()
				);
	}

	public Sprite createSprite(final TextureHandle handle, final Vector2 position, final Vector2 origin, final float width, final float height, final float degrees)
	{
		return createSprite( handle, position, origin.x, origin.y, width, height, degrees);
	}

	public Sprite createSprite( final TextureHandle handle, final Vector2 position, final float ox, final float oy, final float width, final float height, final float degrees)
	{
		Texture texture = getTexture( handle );
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture);

		Sprite sprite = new Sprite(region);
		float realOX = sprite.getWidth()/2;
		float realOY = sprite.getHeight()/2;
		sprite.setOrigin(realOX, realOY);

		float scaleX = width/region.getRegionWidth();
		float scaleY = height/region.getRegionHeight();
		sprite.setScale( scaleX, scaleY );

		sprite.setRotation( degrees );

		sprite.setPosition( position.x-realOX, position.y-realOY );

		return sprite;
	}

	public Texture getTexture( final TextureHandle handle )
	{
		Texture texture = manager.get( handle.getPath(), Texture.class );
		return texture;
	}

	public Sprite createSprite( final String texture )
	{
		return createSprite( TextureHandle.get( texture ) );
	}
	public Sprite createSprite( final TextureHandle handle )
	{
		TextureRegion region = new TextureRegion( getTexture( handle ) );

		Sprite sprite = new Sprite(region);
		//		float realOX = sprite.getWidth()/2;
		//		float realOY = sprite.getHeight()/2;
		//		sprite.setOrigin(realOX, realOY);

		return sprite;
	}

	//	public static Web createWeb( Asteroid source, Asteroid target )
	//	{
	//		Sprite sourceSprite = createSprite("web_srouce_01.png");
	//		Sprite targetSprite = createSprite("web_target_01.png");
	//		Sprite threadSprite = createSprite("web_thread_01.png");
	//
	//		sourceSprite.setPosition(source.getX(), source.getY());
	//		threadSprite.setPosition(source.getX()-target.getX(), source.getY()-target.getY());
	//		targetSprite.setPosition(target.getX(), target.getY());
	//
	//		return new Web(sourceSprite, threadSprite, targetSprite);
	//	}
	//

	public TextureHandle registerTexture( final TextureHandle handle )
	{
		manager.load( handle.getPath(), Texture.class );
		return handle;
	}
	public TextureHandle registerTexture( String texturePath )
	{
		if(! Gdx.files.internal( texturePath ).exists() )
		{
			throw new RuntimeException("Texture not found: " + texturePath );
		}
		
		return registerTexture ( TextureHandle.get(texturePath));
		
	}

	static NumberFormat ANIMA_NUMBERING = new DecimalFormat( "0000" );

	public AnimationHandle registerAnimation(  final AnimationHandle animationHandle )
	{
		TextureAtlasHandle atlasHandle = animationHandle.getAtlas();

		manager.load( atlasHandle.getPath(), TextureAtlas.class );

		if(!animationHandles.contains( animationHandle ))
		{
			animationHandles.add( animationHandle );
		}

		return animationHandle;

	}

	private void loadAnimations()
	{
		for(AnimationHandle handle : animationHandles)
		{
			animationCache.put( handle, createAnimation( handle ) );
		}

		animationHandles.clear();
	}
	
	public void loadShader(String name, String vertexShaderFilename, String fragShaderFilename )
	{
		manager.load( name, ShaderProgram.class, new ShaderParameters(vertexShaderFilename, fragShaderFilename) );
	}


	private Animation createAnimation(final AnimationHandle handle)
	{
		TextureAtlas atlas = manager.get( handle.getAtlas().getPath(), TextureAtlas.class );

		int size = atlas.getRegions().size;
		TextureRegion[] frames = new TextureRegion[size];

		for(int fidx = 0; fidx < size; fidx ++)
		{
			frames[fidx] = atlas.findRegion( handle.getRegionName() + "." + ANIMA_NUMBERING.format(fidx) );
			if(frames[fidx] == null)
				throw new IllegalArgumentException( "Region array " + handle.getRegionName() + " was not found in atlas " + handle );
		}

		Debug.log("Loaded animation [" + handle + "].");

		Animation animation = new Animation( 0.05f, frames );
		return animation;
	}

	public Animation getAnimation( final AnimationHandle handle )
	{
		Animation animation = animationCache.get( handle );
		if( animation == null)
			throw new IllegalArgumentException("No animation registered for handle " + handle );

		return animation;
	}
	/**
	 * TODO: cache!
	 * @param string
	 * @return
	 */
	public static BitmapFont loadFont(final String fontName, final float scale)
	{
		BitmapFont font =  new BitmapFont(
				Gdx.files.internal(fontName + ".fnt"),
				Gdx.files.internal(fontName + ".png"),
				false // wat is flip?
				);

		font.setScale( scale );

		return font;
	}

	public TextureHandle getCrosshairTexture()
	{
		return CROSSHAIR_TXR;
	}
	public ShaderProgram getShader( String shaderName )
	{
		return manager.get( shaderName );
	}
	public void registerRenderer(String unitType, IUnitRenderer unitRend)
	{
		assert ! renderers.contains( unitType.hashCode() );
		renderers.put(unitType.hashCode(), unitRend);
	}

	public IUnitRenderer getRenderer(IUnitDef def)
	{
		assert renderers.contains( def.getType().hashCode() ) : "No renderer for unit type " + def.getType();
		return renderers.get( def.getType().hashCode() );
	}
}
