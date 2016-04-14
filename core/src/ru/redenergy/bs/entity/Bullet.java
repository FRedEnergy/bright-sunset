package ru.redenergy.bs.entity;


import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends EntityProjectile {
    protected float damage;
    public Bullet(World world, float x, float y, float damage) {
        super(world, x, y);
        this.damage = damage;
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        this.setDead(true);
        if(entity instanceof EntityLiving){
            ((EntityLiving) entity).setHealth(((EntityLiving) entity).getHealth() - damage);
        }
    }

    @Override
    protected void initBodyAtCords(World world, float x, float y) {
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        bodyDef.bullet = true;
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5F);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
