package ru.redenergy.bs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import ru.redenergy.bs.BrightSunsetGame;
import ru.redenergy.bs.GameSession;
import ru.redenergy.bs.entity.*;
import ru.redenergy.bs.map.Box2dTiledResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen{

    private GameSession session;
    private Stage stage;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private Touchpad touchpad;
    private Button shootButton;
    private OrthogonalTiledMapRenderer mapRenderer;
    private SpriteBatch batch;
    private Label scoreLabel;
    private Skin skin;

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        session = new GameSession();
        session.init();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() * BrightSunsetGame.VIEWPORT_SCALE, Gdx.graphics.getHeight() * BrightSunsetGame.VIEWPORT_SCALE);
        debugRenderer = new Box2DDebugRenderer();
        setupControllingActors();
        mapRenderer = new OrthogonalTiledMapRenderer(session.map, 1 / 2F);
    }
    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        session.update();
        scoreLabel.setText(String.valueOf(session.score));
        camera.position.x = session.player.getBody().getPosition().x;
        camera.position.y = session.player.getBody().getPosition().y;
        session.updatePlayerPos(touchpad);
        session.updatePlayerRotation(touchpad);
        camera.zoom = 0.4F;
        mapRenderer.setView(camera);
        mapRenderer.render();
        stage.draw();
        camera.update();
        Particle.getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        for(Particle particle: session.particles)
            particle.render(batch, camera);
        Particle.getRenderer().end();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        for(Entity entity: session.entities)
            entity.render(batch, camera);
        batch.end();
    }

    private void setupControllingActors(){
        BitmapFont font = new BitmapFont();
        font.setColor(Color.YELLOW);
        font.getData().setScale(2, 2);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.YELLOW;
        scoreLabel = new Label(String.valueOf(session.score), labelStyle);
        scoreLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 80, Align.center);

        Skin textureSkin = new Skin();
        textureSkin.add("crosshair", new Texture("crosshair.png"));
        Touchpad.TouchpadStyle touchpadStyle = skin.get(Touchpad.TouchpadStyle.class);
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(25, 25, 175, 175);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        Drawable crosshair = textureSkin.getDrawable("crosshair");
        btnStyle.up = crosshair;
        btnStyle.down = crosshair;
        btnStyle.over = crosshair;
        shootButton = new Button(btnStyle);
        shootButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                session.entities.add(session.player.shoot());
                return true;
            }
        });
        shootButton.setBounds(Gdx.graphics.getWidth() - 150, 50, 128, 128);
        stage = new Stage();
        stage.addActor(scoreLabel);
        stage.addActor(touchpad);
        stage.addActor(shootButton);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
