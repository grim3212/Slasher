package com.grim3212.slasher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.grim3212.slasher.Slasher;
import com.grim3212.slasher.stages.MenuStage;

public class MenuScreen extends ScreenAdapter {

	private MenuStage stage;

	public MenuScreen(final Slasher game) {
		this.stage = new MenuStage(game);
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(stage);

		// Update the stage
		stage.draw();
		stage.act(delta);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
