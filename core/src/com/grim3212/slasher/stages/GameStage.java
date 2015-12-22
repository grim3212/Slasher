package com.grim3212.slasher.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.grim3212.slasher.entity.EntityManager;
import com.grim3212.slasher.groups.GameGroup;
import com.grim3212.slasher.util.GameInfo;
import com.grim3212.slasher.world.WorldManager;

public class GameStage extends Stage {

	private SpriteBatch fontBatch;
	private BitmapFont font;

	private final float TIME_STEP = 1 / 60f;

	public GameStage() {
		GameInfo.loadGameInfo();
		Gdx.graphics.setTitle(GameInfo.title);
		WorldManager.initWorld();

		fontBatch = new SpriteBatch();
		font = new BitmapFont();

		this.addActor(new GameGroup());
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		WorldManager.getWorld().step(TIME_STEP, 6, 2);
	}

	@Override
	public void draw() {
		super.draw();

		fontBatch.begin();
		font.setColor(Color.BLACK);
		if (GameInfo.drawFPS)
			font.draw(fontBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()) + "\nX: " + EntityManager.getPlayer().getPosition().x + "\nY: " + EntityManager.getPlayer().getPosition().y + "\nOnGround: " + EntityManager.getPlayer().onGround + "\nVelocity X: " + EntityManager.getPlayer().body.getLinearVelocity().x + "\nVelocity Y: " + EntityManager.getPlayer().body.getLinearVelocity().y, 5, Gdx.graphics.getHeight());
		fontBatch.end();
	}
}
