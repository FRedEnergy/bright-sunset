package ru.redenergy.bs.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BloodParticle extends Particle{

    private static final Random rng = new Random();

    private long liveTime = 0;
    private long expireTime = rng.nextInt((int) TimeUnit.SECONDS.toMillis(5));

    public BloodParticle(float x, float y) {
        super(x, y);
        setShape(ShapeRenderer.ShapeType.Filled);
        setColor(Color.RED);
        setSize(0, 0, 2, 2);
    }

    @Override
    public void update() {
        super.update();
        liveTime++;
        if(liveTime >= expireTime)
            setDead(true);
    }
}
