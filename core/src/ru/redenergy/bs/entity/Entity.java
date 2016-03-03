package ru.redenergy.bs.entity;

import com.badlogic.gdx.physics.box2d.*;

public abstract class Entity {

    private World world;
    private Body body;

    public Entity(World world, int x, int y){
        initBodyAtCords(world, x, y);
    }

    private void initBodyAtCords(World world, int x, int y){
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
