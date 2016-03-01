package ru.redenergy.bs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.redenergy.bs.screen.MainMenuScreen;

public class BrightSunsetGame extends Game {

	public static BrightSunsetGame instance;

	@Override
	public void create() {
		instance = this;
		setScreen(new MainMenuScreen());
	}
}
