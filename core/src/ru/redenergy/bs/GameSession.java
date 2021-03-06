package ru.redenergy.bs;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import ru.redenergy.bs.entity.*;
import ru.redenergy.bs.map.Box2dTiledResolver;
import ru.redenergy.bs.screen.FinishScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class GameSession {

    public World world;
    public Player player;
    public TiledMap map;
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Particle> particles = new ArrayList<Particle>();
    private Random rng = new Random();
    public int score = 0;

    public void init(){
        entities.clear();
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        setupMap();
        player = new Player(world, 512, 512);
        entities.add(player);
        setupListeners();
    }

    public void update(){
        world.step(1F / 60F, 6, 2);
        for(ListIterator iterator = entities.listIterator(); iterator.hasNext();){
            Entity entity = (Entity) iterator.next();
            if(entity.isDead()){
                if(entity == player){
                    endGame();
                    break;
                }
                if(entity instanceof Enemy) {
                    score++;
                    spawnBlood(entity.getBody().getPosition().x, entity.getBody().getPosition().y, 15);
                }
                iterator.remove();
            }
            entity.update();
        }
        for(ListIterator iterator = particles.listIterator(); iterator.hasNext();){
            Particle particle = (Particle) iterator.next();
            if(particle.isDead()){
                iterator.remove();
            }
            particle.update();
        }
        if(rng.nextInt(200) == 0){
            if(countEntities(Enemy.class) < 20) {
                int angle = rng.nextInt(360);
                float x = (float) (player.getBody().getPosition().x + (Math.cos(angle) * (rng.nextInt(100) + 100)));
                float y = (float) (player.getBody().getPosition().x + (Math.sin(angle) * (rng.nextInt(100) + 100)));
                if(validateEnemySpawnPos(x, y)) {
                    entities.add(new Enemy(world, x, y));
                }
            }
        }
    }

    private boolean validateEnemySpawnPos(float x, float y){
        return (x > 20 && x < 1000 && y > 20 && y < 1000);
    }

    private void spawnBlood(float x, float y, int amount){
        for(int i = 0; i < amount; i++)
            particles.add(new BloodParticle(x + rng.nextInt(10) - 5, y  + rng.nextInt(10) - 5));
    }

    private void endGame(){
        BrightSunsetGame.instance.setScreen(new FinishScreen(score));
    }

    public int countEntities(Class entity){
        int count = 0;
        for(Entity ent: entities)
            if(entity.isInstance(ent))
                count++;
        return count;
    }

    public void updatePlayerRotation(Touchpad touchpad){
        if(!touchpad.isTouched()) return;
        double degree = Math.atan2(touchpad.getKnobPercentY(), touchpad.getKnobPercentX());
        player.getBody().setTransform(player.getBody().getPosition(), (float) degree);
    }

    public void updatePlayerPos(Touchpad touchpad){
        float maxVel = 75F;
        Vector2 vel = player.getBody().getLinearVelocity();
        float desiredVelX = maxVel * touchpad.getKnobPercentX();
        float desiredVelY = maxVel * touchpad.getKnobPercentY();
        float changeX = desiredVelX - vel.x;
        float changeY = desiredVelY - vel.y;
        float impulseX = player.getBody().getMass() * changeX / (1F / 60F);
        float impulseY = player.getBody().getMass() * changeY / (1F/  60F);
        player.getBody().applyForce(new Vector2(impulseX, impulseY), player.getBody().getWorldCenter(), true);
    }

    private void setupMap(){
        map = new TmxMapLoader().load("main-map.tmx");
        Box2dTiledResolver.populateMap(map, world);
    }

    private void setupListeners(){
        ContactListener listener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object contacter = contact.getFixtureA().getBody().getUserData();
                Object contated = contact.getFixtureB().getBody().getUserData();
                if(contacter instanceof Entity && contated instanceof Entity){
                    ((Entity) contacter).onCollideWith((Entity) contated);
                    ((Entity) contated).onCollideWith((Entity) contacter);
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                if(contact.getFixtureA().getBody().getUserData() instanceof Bullet || contact.getFixtureB().getBody().getUserData() instanceof Bullet)
                    contact.setEnabled(false);
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        };
        world.setContactListener(listener);
        ContactFilter filter = new ContactFilter() {
            @Override
            public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
                return !(fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof EntityLiving) ||
                        !(fixtureA.getBody().getUserData() instanceof EntityLiving && fixtureB.getBody().getUserData() instanceof Player);
            }
        };
        world.setContactFilter(filter);
    }
}
