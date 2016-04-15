package ru.redenergy.bs.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class Particle {

    protected boolean isDead = false;
    protected float xPos, yPos;
    protected float size;
    protected Color color;
    protected ShapeRenderer.ShapeType shape;
    protected float x, y, width, height;

    private static ShapeRenderer renderer = new ShapeRenderer();

    public Particle(float x, float y) {
        this.xPos = x;
        this.yPos = y;
    }

    public void update(){}

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ShapeRenderer.ShapeType getShape() {
        return shape;
    }

    public void setShape(ShapeRenderer.ShapeType shape) {
        this.shape = shape;
    }

    public void setSize(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        renderer.setProjectionMatrix(camera.combined);
        renderer.setColor(color);
        renderer.rect(xPos, yPos, width, height);
    }

    public static ShapeRenderer getRenderer() {
        return renderer;
    }
}