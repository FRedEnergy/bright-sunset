package ru.redenergy.bs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.redenergy.bs.BrightSunsetGame;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Skin skin;

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage = new Stage();

        Table mainTable = new Window("", skin);

        mainTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.center();
        mainTable.add(new Label("Welcome!", skin.get("title", Label.LabelStyle.class))).setActorY(mainTable.getHeight() / 4);
        mainTable.row().padTop(50F);
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        TextButton button = new TextButton("New Game", style);
        button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                BrightSunsetGame.instance.setScreen(new GameScreen());
                return true;
            }
        });
        mainTable.add(button).width(100F);
        mainTable.row().pad(5F);
        mainTable.add(new TextButton("Multiplayer", style)).width(100F);
        mainTable.row().pad(5F);

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
        stage.dispose();
    }
}