package ru.redenergy.bs.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Enemy extends EntityLiving {


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
    }
}
