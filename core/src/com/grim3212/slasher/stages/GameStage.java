package com.grim3212.slasher.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.grim3212.slasher.entity.EntityManager;
import com.grim3212.slasher.entity.player.Player;
import com.grim3212.slasher.util.GameInfo;
import com.grim3212.slasher.util.MapBodyBuilder;
import com.grim3212.slasher.world.WorldManager;

public class GameStage extends Stage {

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;
	private SpriteBatch fontBatch;
	private TiledMap map;
	private BitmapFont font;
	private Player player;

	private float unitScale = 1 / 16F;

	private final float TIME_STEP = 1 / 60f;
	private float accumulator = 0f;

	private float playerWidth = unitScale * 16;
	private float playerHeight = unitScale * 32;

	private int mapWidth = 0;
	private int mapHeight = 0;

	public GameStage() {
		GameInfo.loadGameInfo();
		Gdx.graphics.setTitle(GameInfo.title);
		WorldManager.initWorld();

		debugRenderer = new Box2DDebugRenderer();

		player = new Player("Grim3212");
		EntityManager.setPlayer(player);
		fontBatch = new SpriteBatch();
		font = new BitmapFont();
		map = new TmxMapLoader().load("testmap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, unitScale);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 23F, 17.2F);
		camera.update();

		MapProperties prop = map.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);

		MapBodyBuilder.buildShapes(map, 16f, WorldManager.getWorld(), map.getLayers().get("Objects"));
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		accumulator += delta;

		handleInput();

		camera.position.x = player.getPosition().x;
		if (camera.position.x < 11.5F) {
			camera.position.x = 11.5F;
		} else if (camera.position.x > mapWidth - 11.5F) {
			camera.position.x = mapWidth - 11.5F;
		}

		camera.position.y = player.getPosition().y;
		if (camera.position.y > 91.4F) {
			camera.position.y = 91.4F;
		} else if (camera.position.y < mapHeight - 91.4F) {
			camera.position.y = mapHeight - 91.4F;
		}
		camera.update();

		while (accumulator >= delta) {
			WorldManager.getWorld().step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
	}

	@Override
	public void draw() {
		super.draw();

		renderer.setView(camera);
		renderer.render();

		fontBatch.begin();
		font.setColor(Color.BLACK);
		if (GameInfo.drawFPS)
			font.draw(fontBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()) + "\nX: " + player.getPosition().x + "\nY: " + player.getPosition().y + "\nOnGround: " + player.onGround, 5, Gdx.graphics.getHeight());
		fontBatch.end();

		Batch batch = renderer.getBatch();

		batch.begin();
		if (player.facingRight) {
			batch.draw(player.texture, player.getPosition().x - 0.5F, player.getPosition().y - 1F, playerWidth, playerHeight);
		} else {
			batch.draw(player.texture, player.getPosition().x - 0.5F, player.getPosition().y - 1F, playerWidth, playerHeight);
		}
		batch.end();

		debugRenderer.render(WorldManager.getWorld(), camera.combined);
	}

	/**
	 * Handles all keyboard stuff
	 */
	private void handleInput() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (player.onGround) {
				player.body.applyLinearImpulse(0, 10f, player.getPosition().x, player.getPosition().y, true);
				player.secondJump = false;
			} else if (!player.onGround && !player.secondJump) {
				player.body.applyLinearImpulse(0, 5.5f, player.getPosition().x, player.getPosition().y, true);
				player.secondJump = true;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			player.body.applyLinearImpulse(-0.50F, 0, player.getPosition().x, player.getPosition().y, true);
		}

		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			player.body.applyLinearImpulse(0.50F, 0, player.getPosition().x, player.getPosition().y, true);
		}
	}
}
