package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import static com.mygdx.game.handlers.B2DVars.PPM;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.MyContactListener;

public class Play extends GameState {

	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dcam;

	public Play(GameStateManager gsm) {
		super(gsm);
		
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(new MyContactListener());
		b2dr = new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 5 / PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_BOX | B2DVars.BIT_BALL;
		world.createBody(bdef).createFixture(fdef).setUserData("ground");
		
		bdef.position.set(160 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		shape.setAsBox(10 / PPM, 10 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BOX;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		world.createBody(bdef).createFixture(fdef).setUserData("box");
		
		bdef.position.set(145 / PPM, 220 / PPM);
		bdef.type = BodyType.DynamicBody;
		CircleShape cshape = new CircleShape();
		cshape.setRadius(10 / PPM);
		fdef.shape = cshape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		world.createBody(bdef).createFixture(fdef).setUserData("ball");
		
		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false, MyGdxGame.V_WIDTH / PPM, MyGdxGame.V_HEIGHT / PPM);
	}

	@Override
	public void handleInput() {

	}

	@Override
	public void update(float dt) {
		world.step(dt, 6, 2);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		b2dr.render(world, b2dcam.combined);
	}

	@Override
	public void dispose() {

	}

}
