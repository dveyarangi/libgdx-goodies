package eir.game.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import eir.debug.Debug;

public class MenuScreen extends AbstractScreen
{
    private Table mainTable;

    private final static float LEFT_PADDING = 40;

    public MenuScreen( final Game game )
    {
        super( game );
    }

    @Override
    public void show()
    {
        super.show();

        // retrieve the custom skin for our 2D widgets
        Skin skin = UI.getSkin();

        // create the table actor and add it to the stage
        mainTable = new Table( skin );
        mainTable.setWidth( stage.getWidth() );
        mainTable.setHeight( stage.getHeight() );
        stage.addActor( mainTable );

        Table menuTable = new Table(skin);

        // register the button "start game"
        TextButton startGameButton = new TextButton( "Start game", skin );
        startGameButton.addListener( new ClickListener() {
			@Override
	       	public void clicked (final InputEvent event, final float x, final float y) {
               // game.startGame();
             }

        } );

        menuTable.add( startGameButton ).align( Align.left ).padLeft( LEFT_PADDING ).expandX().row();



        // register the button "options"
        TextButton optionsButton = new TextButton( "Options", skin );
        optionsButton.addListener( new ClickListener() {
			@Override
	       	public void clicked (final InputEvent event, final float x, final float y) {
				  	Debug.log( "TODO: options" );
           }
        } );
        menuTable.add( optionsButton ).align( Align.left ).padLeft( LEFT_PADDING ).row();


        // register the button "high scores"
        TextButton highScoresButton = new TextButton( "High Scores", skin );
        highScoresButton.addListener( new ClickListener() {
       	@Override
		public void clicked (final InputEvent event, final float x, final float y) {
       				Debug.log( "TODO: highscores" );
           }
        } );
        menuTable.add( highScoresButton ).align( Align.left ).padLeft( LEFT_PADDING ).row();

      // register the button "exit"
        TextButton exitButton = new TextButton( "Exit", skin );
        exitButton.addListener( new ClickListener() {
	       	@Override
			public void clicked (final InputEvent event, final float x, final float y) {
	       				Debug.log( "Exiting by user orders." );
					  Gdx.app.exit();
	           }
	        } );
        menuTable.add( exitButton ).align( Align.left ).padLeft( LEFT_PADDING ).row().expandX();

        mainTable.setBackground(new NinePatchDrawable(
        		new NinePatch(skin.getAtlas().findRegion( "main_menu_512x256" ))));

        mainTable.left().add(menuTable);

//        UI.showTwoChoiceDialog( stage, "is this it, u gonna quit?", "Yes", "No", 100, 100 );
    }


}