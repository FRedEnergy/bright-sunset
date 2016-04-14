package ru.redenergy.bs.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Entity {

    protected World world;
    protected Body body;
    protected boolean isDead;

    public Entity(World world, float x, float y){
        initBodyAtCords(world, x, y);
        body.setUserData(this);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera){

    }

    public void update(){
        if(isDead){
            world.destroyBody(body);
            body.setUserData(null);
            body = null;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void onCollideWith(Entity entity){

    }

    protected void initBodyAtCords(World world, float x, float y){
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(9);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.0f;
        fixtureDef.density = 0.0f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }
}
