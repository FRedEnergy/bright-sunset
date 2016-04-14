package ru.redenergy.bs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.redenergy.bs.BrightSunsetGame;

public class FinishScreen implements Screen {

    private Stage stage;
    private final int score;

    public FinishScreen(int score) {
        this.score = score;
    }

    @Override
    public void show() {
        stage = new Stage();

        Table mainTable = new Table();
        mainTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.center();
        mainTable.add(new Label("You've scored " + score + " points!", new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
        mainTable.row();
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        TextButton toMain = new TextButton("Main Menu", style);
        TextButton restart = new TextButton("Restart", style);
        toMain.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                BrightSunsetGame.instance.setScreen(new MainMenuScreen());
                return true;
            }
        });
        restart.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                BrightSunsetGame.instance.setScreen(new GameScreen());
                return true;
            }
        });
        mainTable.add(toMain);
        mainTable.row();
        mainTable.add(restart);
        stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
