package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Texture dropImage, bucketImage;
	private Rectangle bucket;
	private Sound dropSound;
	private Music bgmusic;
	private Array<Rectangle> raindrops;
	private long lastDropTime;

	@Override
	public void create() {
		batch = new SpriteBatch();

		// Create camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// Load textures
		dropImage = new Texture("droplet.png");
		bucketImage = new Texture("bucket.png");

		// Load sound and music
		dropSound = Gdx.audio.newSound(Gdx.files.internal("shot.mp3"));
		bgmusic = Gdx.audio.newMusic(Gdx.files.internal("Anton.mp3"));

		// Play and loop music
		bgmusic.setLooping(true);
		bgmusic.play();

		// Create bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		// Create raindrops
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		// Spritebatch
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Rectangle raindrop : raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.end();

		// Inputs
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), 0, 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 600 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 600 * Gdx.graphics.getDeltaTime();
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0) {
				dropSound.play();
				bgmusic.stop();
				iter.remove();
			}
			if (raindrop.overlaps(bucket) && !(raindrop.y < 64)) {
				bgmusic.play();
				iter.remove();
			}
		}
	}
	
	@Override
	   public void dispose() {
	      dropImage.dispose();
	      bucketImage.dispose();
	      dropSound.dispose();
	      bgmusic.dispose();
	      batch.dispose();
	   }
}
