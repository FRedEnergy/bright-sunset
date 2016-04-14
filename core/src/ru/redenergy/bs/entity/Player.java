package ru.redenergy.bs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import ru.redenergy.bs.item.weapon.PistolWeapon;
import ru.redenergy.bs.item.weapon.Weapon;

public class Player extends EntityLiving{

    private static Sprite charTexture = new Sprite(new Texture(Gdx.files.internal("char/pistol/idle/idle_pistol_0.png")));;

    private Weapon weapon;

    public Player(World world, int x, int y) {
        super(world, x, y);
        setMaxHealth(20F);
        setHealth(10F);
        setWeapon(new PistolWeapon());
    }

    public Bullet shoot(){
        if(weapon == null) return null;
        float speed = 6000F;
        float cos = (float) Math.cos(body.getAngle());
        float sin = (float) Math.sin(body.getAngle());
        float gunOffsetX = 10F;
        float gunOffsetY = -7F;
        float x = body.getPosition().x + (gunOffsetX * cos) - (gunOffsetY * sin);
        float y = body.getPosition().y + (gunOffsetX * sin) + (gunOffsetY * cos);
        Bullet bullet = weapon.createBullet(world, x, y);
        bullet.getBody().setTransform(bullet.getBody().getPosition(), body.getAngle());
        float impulseX = speed * cos;
        float impulseY = speed * sin;
        bullet.getBody().setLinearVelocity(impulseX, impulseY);
        return bullet;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
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
