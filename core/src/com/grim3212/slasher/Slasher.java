package com.grim3212.slasher;

import com.badlogic.gdx.Game;
import com.grim3212.slasher.screens.MenuScreen;

public class Slasher extends Game {

	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}

}
