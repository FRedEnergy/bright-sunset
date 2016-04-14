package ru.redenergy.bs.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends EntityProjectile {
    private static Sprite texture = new Sprite(new Texture(Gdx.files.internal("bullet.png")));
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
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        texture.setScale(0.3F);
        texture.setPosition(body.getPosition().x - texture.getWidth() / 2, body.getPosition().y - texture.getHeight() / 2);
        texture.setRotation((float) Math.toDegrees(this.body.getAngle()));
        texture.draw(batch);
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
