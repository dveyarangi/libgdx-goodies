/**
 *
 */
package eir.world.unit.structure;

import eir.resources.ResourceFactory;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitDef;
import eir.world.unit.UnitsFactory.UnitFactory;

/**
 * @author dveyarangi
 *
 */
public class SpawnerFactory extends UnitFactory <Spawner>
{
	public static final String NAME = "spawner".intern();

	@Override protected String getName() { return NAME; }

	@Override
	protected Spawner createEmpty() { return new Spawner(); }

	protected Class<Spawner> getUnitClass() { return Spawner.class; }

	@Override
	protected Class <? extends UnitDef> getDefClass() { return SpawnerDef.class; }

	@Override
	protected void init(LevelDef def, ResourceFactory factory)
	{
		// TODO Auto-generated method stub
		
	}

}