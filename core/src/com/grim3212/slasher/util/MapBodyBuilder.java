package com.grim3212.slasher.util;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MapBodyBuilder {

	// The pixels per tile. If your tiles are 16x16, this is set to 16f
	private static float ppt = 0F;

	public static void createTileCollision(Map map, World world, TiledMapTileLayer layer) {
		BodyDef bDef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fDef = new FixtureDef();
		fDef.density = 2f;
		float scaledRatio = 1 / 16F;

		bDef.type = BodyType.StaticBody;
		Body tileBody = world.createBody(bDef);
		tileBody.setUserData("collision");
		int cellcounter = 0;
		TiledMapTileLayer.Cell cell, nextCell, prevCell;
		int firstCellIndexX = 0, firstCellIndexY = 0;

		for (int j = 0; j < layer.getHeight(); j++) {
			for (int i = 0; i < layer.getWidth(); i++) {
				cell = layer.getCell(i, j);
				prevCell = layer.getCell(i - 1, j);
				nextCell = layer.getCell(i + 1, j);

				if (cell != null) {
					cellcounter++;

					if (prevCell == null) {
						firstCellIndexX = i;
						firstCellIndexY = j;
					}

					if (nextCell == null) {
						float width = layer.getTileWidth() * cellcounter * scaledRatio;
						float height = layer.getTileHeight() * scaledRatio;

						shape.setAsBox(width / 2, height / 2, new Vector2((firstCellIndexX * layer.getTileWidth() * scaledRatio) + (width / 2), (firstCellIndexY * layer.getTileHeight() * scaledRatio) + (height / 2)), 0);
						fDef.shape = shape;
						tileBody.createFixture(fDef);
						cellcounter = 0;
					}
				}
			}
		}
	}

	public static Array<Body> buildShapes(Map map, float pixels, World world, MapLayer layer) {
		ppt = pixels;

		createTileCollision(map, world, (TiledMapTileLayer) map.getLayers().get("foreground"));

		MapObjects objects = layer.getObjects();

		Array<Body> bodies = new Array<Body>();

		for (MapObject object : objects) {

			if (object instanceof TextureMapObject) {
				continue;
			}

			Shape shape;

			if (object instanceof RectangleMapObject) {
				shape = getRectangle((RectangleMapObject) object);
			} else if (object instanceof PolygonMapObject) {
				shape = getPolygon((PolygonMapObject) object);
			} else if (object instanceof PolylineMapObject) {
				shape = getPolyline((PolylineMapObject) object);
			} else if (object instanceof CircleMapObject) {
				shape = getCircle((CircleMapObject) object);
			} else {
				continue;
			}

			BodyDef bd = new BodyDef();
			bd.type = BodyType.StaticBody;
			Body body = world.createBody(bd);
			body.createFixture(shape, 1);

			bodies.add(body);

			shape.dispose();
		}
		return bodies;
	}

	private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt, (rectangle.y + rectangle.height * 0.5f) / ppt);
		polygon.setAsBox(rectangle.width * 0.5f / ppt, rectangle.height * 0.5f / ppt, size, 0.0f);
		return polygon;
	}

	private static CircleShape getCircle(CircleMapObject circleObject) {
		Circle circle = circleObject.getCircle();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.radius / ppt);
		circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
		return circleShape;
	}

	private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
			System.out.println(vertices[i]);
			worldVertices[i] = vertices[i] / ppt;
		}

		polygon.set(worldVertices);
		return polygon;
	}

	private static ChainShape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = vertices[i * 2] / ppt;
			worldVertices[i].y = vertices[i * 2 + 1] / ppt;
		}

		ChainShape chain = new ChainShape();
		chain.createChain(worldVertices);
		return chain;
	}

}