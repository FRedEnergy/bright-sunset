package ru.redenergy.bs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Entity{

    private Sprite charTexture;
    public Player(World world, int x, int y) {
        super(world, x, y);
        charTexture = new Sprite(new Texture(Gdx.files.internal("char/pistol/idle/idle_pistol_0.png")));
    }

    public Bullet shoot(){
        float speed = 4000F;
        float cos = (float) Math.cos(body.getAngle());
        float sin = (float) Math.sin(body.getAngle());
        Bullet bullet = new Bullet(world, body.getPosition().x + (10 * cos), body.getPosition().y + (10 * sin));
        bullet.getBody().setTransform(bullet.getBody().getPosition(), body.getAngle());
        float impulseX = speed * cos;
        float impulseY = speed * sin;
        bullet.getBody().setLinearVelocity(impulseX, impulseY);
        return bullet;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        charTexture.setScale(0.25F);
        charTexture.setPosition(body.getPosition().x - charTexture.getWidth() / 2, body.getPosition().y - charTexture.getHeight() / 2);
        charTexture.setRotation((float) Math.toDegrees(this.body.getAngle()));
        charTexture.draw(batch);
    }
}
