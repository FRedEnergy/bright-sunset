package ru.redenergy.bs.entity;

import com.badlogic.gdx.physics.box2d.*;

public abstract class Entity {

    protected World world;
    protected Body body;

    public Entity(World world, float x, float y){
        initBodyAtCords(world, x, y);
    }

    protected void initBodyAtCords(World world, float x, float y){
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(6F);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.225f;
        fixtureDef.density = 0.85f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }
}
