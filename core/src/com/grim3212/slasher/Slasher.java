package com.grim3212.slasher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.grim3212.slasher.entity.player.Player;

public class Slasher extends ApplicationAdapter {

	private SpriteBatch fontBatch;
	private TiledMap map;
	private BitmapFont font;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Player player;

	public static final float GRAVITY = -2.5F;

	float unitScale = 1 / 16F;
	float zoomScale = 1.75F;

	float playerWidth = unitScale * 16;
	float playerHeight = unitScale * 32;

	int mapWidth = 0;
	int mapHeight = 0;

	@Override
	public void create() {
		GameInfo.loadGameInfo();
		Gdx.graphics.setTitle(GameInfo.title);

		player = new Player("Grim3212", 0.2F, 95F);
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

		initWorld();
	}

	World world;
	Box2DDebugRenderer debugRenderer;

	private void initWorld() {
		Box2D.init();

		world = new World(new Vector2(), true);

		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float deltaTime = Gdx.graphics.getDeltaTime();

		update(deltaTime);

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

		renderer.setView(camera);
		renderer.render();

		fontBatch.begin();
		font.setColor(Color.BLACK);
		if (GameInfo.drawFPS)
			font.draw(fontBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()) + "\nX: " + player.getPosition().x + "\nY: " + player.getPosition().y, 5, Gdx.graphics.getHeight());
		fontBatch.end();

		renderPlayer(deltaTime);

		debugRenderer.render(world, camera.combined);

		world.step(1 / 300F, 6, 2);
	}

	private void renderPlayer(float deltaTime) {
		ShapeRenderer render = new ShapeRenderer();
		Batch batch = renderer.getBatch();

		render.setProjectionMatrix(batch.getProjectionMatrix());
		render.setTransformMatrix(batch.getTransformMatrix());
		render.begin(ShapeType.Filled);
		render.setColor(1, 0, 0, 1);
		render.rect(player.getPosition().x, player.getPosition().y, playerWidth, playerHeight);
		render.end();

		batch.begin();
		if (player.isFacingRight()) {
			batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, playerWidth, playerHeight);
		} else {
			batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, playerWidth, playerHeight);
		}
		batch.end();
	}

	private void update(float deltaTime) {
		if (deltaTime == 0)
			return;

		// Handle keyboard stuff
		handleInput();

		// Update player handling gravity and stuff
		updatePlayer(deltaTime);
	}

	private void updatePlayer(float deltaTime) {
		// Apply gravity to the player
		player.getVelocity().add(0, GRAVITY);

		// clamp the velocity to the maximum, x-axis only
		if (Math.abs(player.getVelocity().x) > player.getMaxVelocity()) {
			player.getVelocity().x = Math.signum(player.getVelocity().x) * player.getMaxVelocity();
		}

		// clamp the velocity to 0 if it's < 1
		if (Math.abs(player.getVelocity().x) < 1) {
			player.getVelocity().x = 0;
		}

		// multiply by delta time so we know how far we go
		player.getVelocity().scl(deltaTime);

		checkPlayerCollision();

		// unscale the velocity by the inverse delta time and set
		// the latest position

		boolean posVelocity = false;
		if (player.getPosition().x <= 0) {
			player.getPosition().x = 0;
			posVelocity = true;
		}

		boolean negVelocity = false;
		if (player.getPosition().x >= 99) {
			player.getPosition().x = 99;
			negVelocity = true;
		}

		if (posVelocity) {
			if (player.getVelocity().x < 0) {
				player.getVelocity().x = 0;
			}
		}

		if (negVelocity) {
			if (player.getVelocity().x > 0) {
				player.getVelocity().x = 0;
			}
		}

		player.getPosition().add(player.getVelocity());

		player.getVelocity().scl(1 / deltaTime);

		// Apply damping to the velocity on the x-axis so we don't
		// walk infinitely once a key was pressed
		player.getVelocity().x *= player.getVelocityDamping();
	}

	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	private Array<Rectangle> tiles = new Array<Rectangle>();

	private void checkPlayerCollision() {
		// perform collision detection & response, on each axis, separately
		// if the koala is moving right, check the tiles to the right of it's
		// right bounding box edge, otherwise check the ones to the left
		Rectangle playerRect = rectPool.obtain();
		playerRect.set(player.getPosition().x, player.getPosition().y, playerWidth, playerHeight);

		int startX, startY, endX, endY;
		if (player.getVelocity().x > 0) {
			startX = endX = (int) (player.getPosition().x + playerWidth + player.getVelocity().x);
		} else {
			startX = endX = (int) (player.getPosition().x + player.getVelocity().x);
		}
		startY = (int) (player.getPosition().y);
		endY = (int) (player.getPosition().y + playerHeight);
		getTiles(startX, startY, endX, endY, tiles);
		playerRect.x += player.getVelocity().x;
		for (Rectangle tile : tiles) {
			if (playerRect.overlaps(tile)) {
				player.getVelocity().x = 0;
				break;
			}
		}
		playerRect.x = player.getPosition().x;

		// if the koala is moving upwards, check the tiles to the top of it's
		// top bounding box edge, otherwise check the ones to the bottom
		if (player.getVelocity().y > 0) {
			startY = endY = (int) (player.getPosition().y + playerHeight + player.getVelocity().y);
		} else {
			startY = endY = (int) (player.getPosition().y + player.getVelocity().y);
		}
		startX = (int) (player.getPosition().x);
		endX = (int) (player.getPosition().x + playerWidth);
		getTiles(startX, startY, endX, endY, tiles);
		playerRect.y += player.getVelocity().y;
		for (Rectangle tile : tiles) {
			if (playerRect.overlaps(tile)) {
				// we actually reset the koala y-position here
				// so it is just below/above the tile we collided with
				// this removes bouncing :)
				if (player.getVelocity().y > 0) {
					player.getPosition().y = tile.y - playerHeight;
					// we hit a block jumping upwards, let's destroy it!
					// TiledMapTileLayer layer = (TiledMapTileLayer)
					// map.getLayers().get("walls");
					// layer.setCell((int) tile.x, (int) tile.y, null);
				} else {
					player.getPosition().y = tile.y + tile.height;
					// if we hit the ground, mark us as grounded so we can jump
					player.setInAir(false);
				}
				player.getVelocity().y = 0;
				break;
			}
		}
		rectPool.free(playerRect);
	}

	/**
	 * Handles all keyboard stuff
	 */
	private void handleInput() {

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyPressed(Keys.SPACE) && !player.isInAir()) {
			player.getVelocity().y += player.getJumpVelocity();
			player.setInAir(true);
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A) || isTouched(0, 0.25f)) {
			player.getVelocity().x = -player.getMaxVelocity();
			player.setFacingRight(false);
		}

		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D) || isTouched(0.25f, 0.5f)) {
			player.getVelocity().x = player.getMaxVelocity();
			player.setFacingRight(true);
		}
	}

	private boolean isTouched(float startX, float endX) {
		// check if any finge is touch the area between startX and endX
		// startX/endX are given between 0 (left edge of the screen) and 1
		// (right edge of the screen)
		for (int i = 0; i < 2; i++) {
			float x = Gdx.input.getX(i) / (float) Gdx.graphics.getWidth();
			if (Gdx.input.isTouched(i) && (x >= startX && x <= endX)) {
				return true;
			}
		}
		return false;
	}

	private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("foreground");
		rectPool.freeAll(tiles);
		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Rectangle rect = rectPool.obtain();
					rect.set(x, y, 1, 1);
					tiles.add(rect);
				}
			}
		}
	}
}
