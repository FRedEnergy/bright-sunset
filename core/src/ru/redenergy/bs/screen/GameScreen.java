package ru.redenergy.bs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import ru.redenergy.bs.entity.Player;

public class GameScreen implements Screen{

    private OrthographicCamera camera;
    private World world;
    private Player player;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void show() {
        Box2D.init();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, 20, 20);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float maxVel = 100F;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            float desiredVel = maxVel;
            Vector2 vel = player.getBody().getLinearVelocity();
            float change = desiredVel - vel.x;
            float impulse = player.getBody().getMass() * change / (1F / 60F);
            player.getBody().applyForce(new Vector2(impulse, 0F), player.getBody().getWorldCenter(), true);//.applyLinearImpulse(1F, 0.0F, 20F, 20F, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            float desiredVel = -maxVel;
            Vector2 vel = player.getBody().getLinearVelocity();
            float change = desiredVel - vel.x;
            float impulse = player.getBody().getMass() * change / (1F / 60F);
            player.getBody().applyForce(new Vector2(impulse, 0F), player.getBody().getWorldCenter(), true);//.applyLinearImpulse(1F, 0.0F, 20F, 20F, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            float desiredVel = maxVel;
            Vector2 vel = player.getBody().getLinearVelocity();
            float change = desiredVel - vel.y;
            float impulse = player.getBody().getMass() * change / (1F / 60F);
            player.getBody().applyForce(new Vector2(0F, impulse), player.getBody().getWorldCenter(), true);//.applyLinearImpulse(1F, 0.0F, 20F, 20F, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            float desiredVel = -maxVel;
            Vector2 vel = player.getBody().getLinearVelocity();
            float change = desiredVel - vel.y;
            float impulse = player.getBody().getMass() * change / (1F / 60F);
            player.getBody().applyForce(new Vector2(0F, impulse), player.getBody().getWorldCenter(), true);//.applyLinearImpulse(1F, 0.0F, 20F, 20F, true);
        }

        camera.update();
        world.step(1F / 60F, 6, 2);
        debugRenderer.render(world, camera.combined);
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
