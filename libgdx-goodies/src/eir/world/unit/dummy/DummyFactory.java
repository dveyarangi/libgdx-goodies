package eir.world.unit.dummy;

import eir.world.unit.UnitsFactory.UnitFactory;

public class DummyFactory extends UnitFactory <Dummy>
{
	public static final String NAME = "dummy".intern();

	@Override protected String getName() { return NAME; }

	@Override
	protected Dummy createEmpty()
	{
		return new Dummy();
	}

}
