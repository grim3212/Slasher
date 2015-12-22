package com.grim3212.slasher.groups;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.grim3212.slasher.entity.EntityManager;
import com.grim3212.slasher.entity.player.Player;
import com.grim3212.slasher.input.KeyManager;
import com.grim3212.slasher.util.Constants;
import com.grim3212.slasher.util.GameUtil;
import com.grim3212.slasher.util.MapBodyBuilder;
import com.grim3212.slasher.world.WorldManager;

public class GameGroup extends Group {

	// Rendering stuff for tiled map
	private OrthogonalTiledMapRenderer renderer;
	public OrthographicCamera cam;
	private TiledMap map;

	// Box2d debug
	private Box2DDebugRenderer debugRenderer;

	// The player
	private Player player;

	// temp Matrix4 for storing the batch matrix and resetting batch after
	// drawing GameRoot
	private final Matrix4 tmpMatrix4 = new Matrix4();

	private int mapWidth = 0;
	private int mapHeight = 0;

	public GameGroup() {
		player = new Player(Constants.PLAYER_ID);
		EntityManager.registerEntity(player);

		map = new TmxMapLoader().load("testmap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, GameUtil.unitScale);
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 23F, 17.2F);
		cam.update();

		debugRenderer = new Box2DDebugRenderer();

		MapProperties prop = map.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);

		MapBodyBuilder.buildShapes(map, 16f, WorldManager.getWorld(), map.getLayers().get("Objects"));

		this.addActor(player);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		KeyManager.handleInput();

		updateCamera();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// End to render tiledmap and debug renderer
		batch.end();
		renderer.setView(cam);
		renderer.render();
		debugRenderer.render(WorldManager.getWorld(), cam.combined);
		batch.begin();

		tmpMatrix4.set(batch.getProjectionMatrix());
		batch.setProjectionMatrix(cam.combined);
		super.draw(batch, parentAlpha);
		batch.setProjectionMatrix(tmpMatrix4);
	}

	private void updateCamera() {
		cam.position.x = player.getPosition().x;
		if (cam.position.x < 11.5F) {
			cam.position.x = 11.5F;
		} else if (cam.position.x > mapWidth - 11.5F) {
			cam.position.x = mapWidth - 11.5F;
		}

		cam.position.y = player.getPosition().y;
		if (cam.position.y > 91.4F) {
			cam.position.y = 91.4F;
		} else if (cam.position.y < mapHeight - 91.4F) {
			cam.position.y = mapHeight - 91.4F;
		}
		cam.update();
	}
}
