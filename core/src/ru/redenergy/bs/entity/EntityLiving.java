package ru.redenergy.bs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class EntityLiving extends Entity {

    protected float health;
    protected float maxHealth;

    private Texture widgets = new Texture(Gdx.files.internal("widgets.png"));
    private TextureRegion healthBarBg = new TextureRegion(widgets, 0, 0, 256, 35);
    private TextureRegion healthBar = new TextureRegion(widgets, 0, 35, 256, 25);

    public EntityLiving(World world, float x, float y) {
        super(world, x, y);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
        if(health <= 0)
            setDead(true);
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        float healthbarwidth = 15.6F;
        float displaywidth = healthbarwidth / maxHealth * health;
        batch.draw(healthBarBg, body.getPosition().x - 8, body.getPosition().y - 15, 16, 3);
        batch.draw(healthBar, body.getPosition().x - 7.5F, body.getPosition().y - 14.6F, displaywidth, 2);
    }
}
