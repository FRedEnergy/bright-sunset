package ru.redenergy.bs.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Box2dTiledResolver {

    public static int TILE_SIZE = 2;

    public static void populateMap(TiledMap map, World world){
        for(MapLayer layer: map.getLayers()){
            int errors = 0;
            if(layer.getName().endsWith("_static")){
                for(MapObject object: layer.getObjects()){
                    if(object instanceof TextureMapObject)
                        return;
                    Shape shape = null;
                    if(object instanceof RectangleMapObject){
                        shape = extractRectangle((RectangleMapObject) object);
                    } else if(object instanceof PolylineMapObject){
                        shape = extractPolyline((PolylineMapObject) object);
                    } else if(object instanceof CircleMapObject){
                        shape = extractCircle((CircleMapObject) object);
                    }
                    if(shape == null){
                        errors++;
                        continue;
                    }


                    BodyDef def = new BodyDef();
                    def.type = BodyDef.BodyType.StaticBody;
                    Body body = world.createBody(def);
                    Fixture fixture = body.createFixture(shape, 0F);
                    fixture.setFriction(0);
                    shape.dispose();
                }
            }
            System.out.printf("Populated layer %s, object %d, errors %s. \n", layer.getName(), layer.getObjects().getCount(), errors);
        }
    }

    private static PolygonShape extractRectangle(RectangleMapObject rectangleObject){
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape shape = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width / 2) / TILE_SIZE, (rectangle.y + rectangle.height / 2) / TILE_SIZE);
        shape.setAsBox(rectangle.width / 4, rectangle.height / 4, size, 0F);
        return shape;
    }

    private static ChainShape extractPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / 2;
            worldVertices[i].y = vertices[i * 2 + 1] / 2;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    private static CircleShape extractCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / 2);
        circleShape.setPosition(new Vector2(circle.x / 2, circle.y / 2));
        return circleShape;
    }

}
