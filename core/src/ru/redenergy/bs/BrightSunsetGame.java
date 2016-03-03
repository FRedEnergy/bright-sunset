package ru.redenergy.bs;

import com.badlogic.gdx.Game;
import ru.redenergy.bs.screen.MainMenuScreen;

public class BrightSunsetGame extends Game {

	public static BrightSunsetGame instance;

	@Override
	public void create() {
		instance = this;
		setScreen(new MainMenuScreen());
	}
}
