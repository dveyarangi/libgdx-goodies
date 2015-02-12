package eir.world.unit.behaviors;

import eir.world.environment.nav.Route;
import eir.world.unit.IUnit;

public interface IRoutedUnit extends IUnit
{
	public Route getRoute();
}
