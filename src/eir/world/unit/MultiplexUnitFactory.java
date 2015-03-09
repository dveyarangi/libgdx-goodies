package eir.world.unit;

import eir.resources.levels.UnitDef;
import gnu.trove.map.hash.TIntObjectHashMap;

public abstract class MultiplexUnitFactory <U extends IUnit> extends UnitFactory <U>
{

	
	TIntObjectHashMap<Class<? extends UnitDef>> types = new TIntObjectHashMap<> ();


	protected void registerDef( String type, Class<? extends UnitDef> clazz )
	{
		assert ! types.containsKey( type.hashCode() );
		
		types.put( type.hashCode(), clazz);
	}
	
	@Override
	protected Class <? extends UnitDef> getDefClass( String unitType ) 
	{	
		return types.get( unitType.hashCode() );	
	}
	


}
