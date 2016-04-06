package ru.redenergy.bs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import ru.redenergy.bs.BrightSunsetGame;
import ru.redenergy.bs.entity.Bullet;
import ru.redenergy.bs.entity.Entity;
import ru.redenergy.bs.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen{

    private Stage stage;
    private OrthographicCamera camera;
    private World world;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private Touchpad touchpad;
    private Button shootButton;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private List<Entity> entities = new ArrayList<Entity>();
    private SpriteBatch batch;

    @Override
    public void show() {
        entities.clear();
        Box2D.init();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() * BrightSunsetGame.VIEWPORT_SCALE, Gdx.graphics.getHeight() * BrightSunsetGame.VIEWPORT_SCALE);
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, 20, 20);
        entities.add(player);
        debugRenderer = new Box2DDebugRenderer();
        setupControllingActors();
        setupMap();
    }
    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.x = player.getBody().getPosition().x;
        camera.position.y = player.getBody().getPosition().y;
        updatePlayerPos();
        updatePlayerRotation();
        camera.zoom = 0.25F;
        mapRenderer.setView(camera);
        mapRenderer.render();
        stage.draw();
        camera.update();
        world.step(1F / 60F, 6, 2);
        debugRenderer.render(world, camera.combined);
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        for(Entity entity: entities)
            entity.render(batch, camera);
        batch.end();
    }

    private void shoot(){
        float speed = 4000F;
        float cos = (float) Math.cos(player.getBody().getAngle());
        float sin = (float) Math.sin(player.getBody().getAngle());
        Bullet bullet = new Bullet(world, player.getBody().getPosition().x + (10 * cos), player.getBody().getPosition().y + (10 * sin));
        bullet.getBody().setTransform(bullet.getBody().getPosition(), player.getBody().getAngle());
        float impulseX = speed * cos;
        float impulseY = speed * sin;
        bullet.getBody().setLinearVelocity(impulseX, impulseY);
    }

    private void updatePlayerRotation(){
        if(!touchpad.isTouched()) return;
        double degree = Math.atan2(touchpad.getKnobPercentY(), touchpad.getKnobPercentX());
        player.getBody().setTransform(player.getBody().getPosition(), (float) degree);
    }


    private void updatePlayerPos(){
        float maxVel = 75F;
        Vector2 vel = player.getBody().getLinearVelocity();
        float desiredVelX = maxVel * touchpad.getKnobPercentX();
        float desiredVelY = maxVel * touchpad.getKnobPercentY();
        float changeX = desiredVelX - vel.x;
        float changeY = desiredVelY - vel.y;
        float impulseX = player.getBody().getMass() * changeX / (1F / 60F);
        float impulseY = player.getBody().getMass() * changeY / (1F/  60F);
        player.getBody().applyForce(new Vector2(impulseX, impulseY), player.getBody().getWorldCenter(), true);
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

    private void setupMap(){
        map = new TmxMapLoader().load("test-map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 2F);
    }

    private void setupControllingActors(){
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        shootButton = new TextButton("Shoot", style);
        shootButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shoot();
                return true;
            }
        });
        shootButton.setBounds(Gdx.graphics.getWidth() - 70, 70, 50, 50);
        stage = new Stage();
        stage.addActor(touchpad);
        stage.addActor(shootButton);
        Gdx.input.setInputProcessor(stage);
    }
}
