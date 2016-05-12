package ru.redenergy.bs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.redenergy.bs.BrightSunsetGame;

public class FinishScreen implements Screen {

    private Stage stage;
    private final int score;
    private Skin skin;

    public FinishScreen(int score) {
        this.score = score;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage = new Stage();

        Table mainTable = new Window("", skin);
        mainTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.center();
        mainTable.add(new Label("You've scored " + score + " points!", skin.get("title", Label.LabelStyle.class)));
        mainTable.row().padTop(50F);
        TextButton toMain = new TextButton("Main Menu", skin);
        TextButton restart = new TextButton("Restart", skin);
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
        mainTable.row().pad(5F);
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
