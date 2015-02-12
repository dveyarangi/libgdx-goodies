package eir.world.controllers;

import eir.rendering.IRenderer;
import eir.world.Level;
import gnu.trove.map.hash.TIntObjectHashMap;


public abstract class ControllerFactory
{
	
/*	public static IController createController(final String type)
  {
		String className = "eir.world.controllers." + type;
		Class<?> ctrlClass;
		try {
			ctrlClass = Class.forName( className );
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Faction controller of type " + className + " not found.");
		}

		IController ctrl;
		try
		{
			ctrl = (IController) ctrlClass.newInstance();
		}
		catch( InstantiationException e ) {
			throw new IllegalArgumentException("Faction controller of type " + className + " not found.");
		}
		catch( IllegalAccessException e ) {
			throw new IllegalArgumentException("Faction controller of type " + className + " not found.");
		}

		return ctrl;
	}
	*/
	
	TIntObjectHashMap <IController> controllers  = new TIntObjectHashMap <IController> ();
	
	public ControllerFactory()
	{
		appendControllers( controllers );
	}
	
	public IController getController( int faction )
	{
		assert controllers.contains( faction ):"No controller for faction " + faction;;
			
		return controllers.get( faction );
	}

	public void init(Level level)
	{
		
		for(int faction : controllers.keys())
		{
			IController ctrl = controllers.get( faction );
			ctrl.init( level.getFaction( faction ) );
		}
	}
	
	protected abstract void appendControllers( TIntObjectHashMap <IController> controllers);

	public void update(float delta)
	{
		for(Object ctrl : controllers.values())
			((IController)ctrl).update( delta );
	}

	public void draw(IRenderer levelRenderer)
	{
		for(Object ctrl : controllers.values())
			((IController)ctrl).draw( levelRenderer );
	}

}
