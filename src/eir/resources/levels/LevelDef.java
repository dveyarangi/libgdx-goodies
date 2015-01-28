package eir.resources.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eir.resources.AnimationHandle;
import eir.world.environment.IBackground;

public class LevelDef
{
	private String name;

	private int width, halfWidth;
	private int height, halfHeight;

	private LevelInitialSettings initialSettings;

	private IBackground background;

	private List <AsteroidDef> asteroids;

	private List <FactionDef> factions = new ArrayList <FactionDef> ();

	private List <IUnitDef> units = new ArrayList <IUnitDef> ();

	private Map <String, AnimationHandle> animations;

	public float getWidth() { return width;	}
	public void setWidth( final int width ) { this.width = width; halfWidth = width/2;}
	public float getHeight() { return height; }
	public void setHeight( final int height ) { this.height = height; halfHeight = height/2;}

	public IBackground getBackgroundDef() { return background; }
	public void setBackgroundDef( final IBackground background ) { this.background = background; }
	public List <FactionDef> getFactionDefs() { return factions; }
	public void setFactionDefs( final List<FactionDef> factions ) { this.factions = factions; }

	public List <AsteroidDef> getAsteroidDefs() { return asteroids; }
	public void setAsteroidDefs( final List<AsteroidDef> asteroids ) { this.asteroids = asteroids; }

	public void setUnitDefs( final List<IUnitDef> units ) { this.units = units; }
	public List <IUnitDef> getUnitDefs() { return units; }

	public Map <String, AnimationHandle> getAnimations() { return animations; }

	public String getName() { return name; }
	public LevelInitialSettings getInitialSettings() { return initialSettings; }

	public void setInitialSettings(final LevelInitialSettings settings) { this.initialSettings = settings; }
	public float getHalfWidth()  { return halfWidth; }
	public float getHalfHeight()  { return halfHeight; }

}
