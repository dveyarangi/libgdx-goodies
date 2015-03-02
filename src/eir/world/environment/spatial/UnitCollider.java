/**
 *
 */
package eir.world.environment.spatial;

import java.util.ArrayList;
import java.util.List;

import eir.world.unit.IUnit;
import eir.world.unit.aspects.IDamager;

/**
 * @author dveyarangi
 *
 */
public class UnitCollider implements ISpatialSensor<ISpatialObject>
{

	private IUnit collidingAnt;

	private List <IDamager> aoeDamages = new ArrayList <IDamager> ();

	public void setAnt(final IUnit collidingAnt)
	{
		this.collidingAnt = collidingAnt;
	}

	@Override
	public boolean objectFound(final ISpatialObject object)
	{
		if(!collidingAnt.isAlive())
			return true;
		
		if(object == collidingAnt)
			return false;
		
		if(! (object instanceof IUnit))
			return false;

		if(! object.isAlive())
			return false;
		
		IUnit ant = (IUnit) object;
		boolean sameFaction = ant.getFaction().getOwnerId() == collidingAnt.getFaction().getOwnerId();
		boolean damageDealt = false;
//		System.out.println("UnitCollider: collision: " + object + " <->" + collidingAnt);
		if(object instanceof IDamager)
		{
			IDamager damager = (IDamager) object;

			if( !sameFaction || damager.dealsFriendlyDamage() )
			{
				if( damager.getDamage().getAOE() != null )
				{
					aoeDamages.add( damager );

				}
				else
				{
					collidingAnt.hit( damager.getDamage(), damager, 1 );
					damageDealt = true;
				}
			}
		}

		if(collidingAnt instanceof IDamager)
		{
			IDamager source = (IDamager)collidingAnt;

			if( !sameFaction || collidingAnt.dealsFriendlyDamage() )
			{
				if( source.getDamage().getAOE() != null )
				{
					aoeDamages.add( source );
				}
				else
				{
					ant.hit( source.getDamage(), source, 1 );

					damageDealt = true;
				}
			}
		}

		return damageDealt;
	}
	
/*	private void hit(IDamager source, )
	{
		IDamager source = (IDamager)collidingAnt;

		if( !sameFaction || collidingAnt.dealsFriendlyDamage() )
		{
			if( source.getDamage().getAOE() != null )
			{
				aoeDamages.add( source );
			}
			else
			{
				ant.hit( source.getDamage(), source, 1 );

				damageDealt = true;
			}
		}
	}*/

	@Override
	public void clear()
	{
		aoeDamages.clear();
		collidingAnt = null;
	}

	public List <IDamager> getAOEDamage() { return aoeDamages; }

}
