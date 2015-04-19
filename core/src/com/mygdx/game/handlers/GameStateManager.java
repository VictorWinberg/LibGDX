package com.mygdx.game.handlers;

import java.util.Stack;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.states.GameState;
import com.mygdx.game.states.Play;

public class GameStateManager {
	private MyGdxGame game;

	private Stack<GameState> states;

	public final int PLAY = 912837;

	public GameStateManager(MyGdxGame game) {
		this.game = game;
		states = new Stack<GameState>();
		pushState(PLAY);
	}

	public MyGdxGame game() {
		return game;
	}

	public void update(float dt) {
		states.peek().update(dt);
	}

	public void render() {
		states.peek().render();
	}

	private GameState getState(int state) {
		if (state == PLAY)
			return new Play(this);
		return null;
	}

	public void setState(int state) {
		popState();
		pushState(state);
	}

	private void popState() {
		GameState g = states.pop();
		g.dispose();
	}

	private void pushState(int state) {
		states.push(getState(state));
	}
}
