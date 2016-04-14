package ru.redenergy.bs.item.weapon;

import com.badlogic.gdx.physics.box2d.World;
import ru.redenergy.bs.entity.Bullet;
import ru.redenergy.bs.item.Item;

public abstract class Weapon extends Item {
    protected float damage;

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Bullet createBullet(World world, float x, float y){
        return new Bullet(world, x, y, damage);
    }
}
