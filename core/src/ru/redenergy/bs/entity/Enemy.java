package ru.redenergy.bs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.TimeUnit;

public class Enemy extends EntityLiving {

    protected static Sprite charTexture = new Sprite(new Texture(Gdx.files.internal("zombie1.png")));
    private static final float damage = 2.75F;
    private long lastAttackTime = 0L;
    private static final long attackDelay = TimeUnit.SECONDS.toMillis(2);

    public Enemy(World world, float x, float y) {
        super(world, x, y);
        setMaxHealth(20F);
        setHealth(20F);
    }

    @Override
    public void update() {
        super.update();
        if(isDead()) return;
        Player player = null;
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for(Body entity: bodies){
            if(entity.getUserData() instanceof Player)
                player = (Player) entity.getUserData();
        }
        if(player == null)
            return;
        Vector2 pos = body.getPosition();
        Vector2 playerPos = player.getBody().getPosition();
        double angle = Math.atan2(playerPos.y - pos.y, playerPos.x - pos.x);
        body.setTransform(body.getPosition(), (float) angle);

        float maxVel = 25F;
        float moveAngle = body.getAngle();
        Vector2 vel = body.getLinearVelocity();
        float changeX = (float) ((maxVel * Math.cos(moveAngle)) - vel.x);
        float changeY = (float) ((maxVel * Math.sin(moveAngle))- vel.y);
        float impulseX = body.getMass() * changeX / (1F / 60F);
        float impulseY = body.getMass() * changeY / (1F / 60F);
        body.applyForce(new Vector2(impulseX, impulseY), body.getWorldCenter(), true);

        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if(fixture.getBody().getUserData() instanceof Player){
                    Player pl = (Player) fixture.getBody().getUserData();
                    long current = System.currentTimeMillis();
                    if((current - lastAttackTime) > attackDelay) {
                        pl.setHealth(pl.getHealth() - damage);
                        lastAttackTime = current;
                    }
                    return true;
                }
                return false;
            }
        }, body.getPosition().x - 5, body.getPosition().y - 5, body.getPosition().x + 5, body.getPosition().y + 5);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        charTexture.setScale(0.05F);
        charTexture.setPosition(body.getPosition().x - charTexture.getWidth() / 2, body.getPosition().y - charTexture.getHeight() / 2);
        charTexture.setRotation((float) Math.toDegrees(this.body.getAngle()));
        charTexture.draw(batch);
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
}
