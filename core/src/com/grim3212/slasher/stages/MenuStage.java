package com.grim3212.slasher.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.grim3212.slasher.Slasher;
import com.grim3212.slasher.screens.GameScreen;

public class MenuStage extends Stage {

	private Table table;
	private Skin skin;

	public MenuStage(final Slasher game) {
		table = new Table();
		table.setFillParent(true);
		table.setDebug(true);

		skin = new Skin(Gdx.files.internal("uiskin.json"));

		TextButton button1 = new TextButton("Button 1", skin);
		table.add(button1);

		button1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen());
			}
		});

		TooltipManager.getInstance().initialTime = 0.1f;

		button1.addListener(new TextTooltip("Click to start game", TooltipManager.getInstance(), skin));

		this.addActor(table);
	}

	@Override
	public void draw() {
		super.draw();
	}

	@Override
	public void dispose() {
		skin.dispose();
	}
}
