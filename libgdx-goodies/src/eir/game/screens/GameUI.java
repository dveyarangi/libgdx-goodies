package eir.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import eir.rendering.LevelRenderer;

public class GameUI
{
	
	Stage stage;
	
	Table table;
	
	public GameUI( Stage stage)
	{
		this.stage = stage;
	}
	
	protected void initUI()
	{
	    table = new Table();
	    table.setFillParent(true);
	    table.setColor(0, 0, 0, 0);
	    stage.addActor(table);

	}

	public void render(LevelRenderer renderer)
	{
		
		table.drawDebug(renderer.getShapeRenderer());
	}
}
