package ru.redenergy.bs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ru.redenergy.bs.BrightSunsetGame;
import ru.redenergy.bs.entity.Bullet;
import ru.redenergy.bs.entity.Player;

public class GameScreen implements Screen{

    private Stage stage;
    private OrthographicCamera camera;
    private World world;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private Touchpad touchpad;
    private Button shootButton;

    @Override
    public void show() {
        Box2D.init();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, 20, 20);
        debugRenderer = new Box2DDebugRenderer();

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
    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updatePlayerPos();
        updatePlayerRotation();
        stage.draw();
        camera.update();
        world.step(1F / 60F, 6, 2);
        debugRenderer.render(world, camera.combined);
    }

    private void shoot(){
        Bullet bullet = new Bullet(world, player.getBody().getPosition().x + 10, player.getBody().getPosition().y);
        bullet.getBody().setTransform(bullet.getBody().getPosition(), player.getBody().getAngle());
        float speed = 4000F;
        float impulseX = (float) (speed * Math.cos(player.getBody().getAngle()));
        float impulseY = (float) (speed * Math.sin(player.getBody().getAngle()));
        bullet.getBody().setLinearVelocity(impulseX, impulseY);//.applyForce(new Vector2(impulseX, impulseY), player.getBody().getWorldCenter(), true);
    }

    private void updatePlayerRotation(){
        if(!touchpad.isTouched()) return;
        double degree = Math.atan2(touchpad.getKnobPercentY(), touchpad.getKnobPercentX());
        player.getBody().setTransform(player.getBody().getPosition(), (float) degree);
    }


    private void updatePlayerPos(){
        float maxVel = 100F;
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
}
