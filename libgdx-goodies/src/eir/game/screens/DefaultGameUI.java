package eir.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import eir.rendering.LevelRenderer;
import eir.world.unit.IUnit;
public class DefaultGameUI implements IGameUI
{
	
	Stage stage;
	
	Table table;
	
	Dialog unitDialog;
	Label unitLabel;
	
	IUnit displayedUnit = null;
	
	Skin skin;
	
	boolean isDirty = true;
	
	
	public DefaultGameUI( Stage stage)
	{
		this.stage = stage;
		 skin = UI.getSkin();
	}
	
	{
	}
	
	@Override
	public void init()
	{
        
        
	    table = new Table();
	    table.setFillParent(true);
	    table.setColor(0, 0, 0, 1);
	    
	    table.setSize(stage.getWidth(), stage.getHeight());
	    table.setFillParent( true );
	    
	    
	    stage.addActor(table);

	    unitLabel = new Label( "Hello!", skin );
//	    unitLabel.setSize(stage.getWidth()/10, stage.getWidth()/10);
//	    unitLabel.setColor(0, 0.2f, 9.9f, 0.5f);
	    
	    unitDialog = new Dialog("Object descriptor", skin);
	    unitDialog.text(unitLabel);
         
	    unitDialog.show( stage, sequence(Actions.alpha(0), Actions.fadeIn(4f, Interpolation.fade)) );
	    unitDialog.setSize(200, 200);
	    unitDialog.setPosition(10, 10);
	       
		unitDialog.setMovable( true );
		unitDialog.setResizable( true );
	        
		unitDialog.getBackground().setMinWidth(200);
		unitDialog.getBackground().setMinHeight(200);
	        
	    //table.top().left().add( unitLabel );
	    
	//    table.invalidate();
	}
	
	@Override
	public void resize( int w, int h)
	{
		table.setSize( w, h );
		table.invalidateHierarchy();
	}

	@Override
	public void update(LevelRenderer renderer)
	{
		if(isDirty == true)
		{
			String unitText;
			if(displayedUnit != null)
			{
				unitText = displayedUnit.toString();
				
			}
			else
				unitText = "";
			
			unitLabel.setText( unitText );
			unitLabel.invalidate();
			
			
			isDirty = false;
		}
		table.drawDebug(renderer.getShapeRenderer());
	}
	
	public void setUnitDescriptor( IUnit unit ) 
	{
		if(unit != displayedUnit)
		{
			displayedUnit = unit;
			isDirty = true;
		}
	}

}
