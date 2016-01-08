package com.grim3212.slasher;

import com.badlogic.gdx.Game;
import com.grim3212.slasher.screens.GameScreen;
import com.grim3212.slasher.screens.MenuScreen;
import com.grim3212.slasher.util.GameInfo;

public class Slasher extends Game {

	@Override
	public void create() {
		GameInfo.loadGameInfo();
		
		if (GameInfo.debug && GameInfo.skip) {
			setScreen(new GameScreen());
		} else {
			setScreen(new MenuScreen(this));
		}
	}

}
