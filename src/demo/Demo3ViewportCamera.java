package demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Demo3ViewportCamera extends ApplicationAdapter {

	SpriteBatch batch;
	Texture texture;
	TextureRegion grassRegion;
	TextureRegion waterRegion;
	boolean[][] tiles;

	// our game window is set to 640x360 in the main method (configuration).
	// we can now use a 320x180 viewport to effectively scale everything by a factor of 2.
	// if we wanted the gui to be drawn differently (for example at the higher resolution of 640x360)
	// then we could setup a second pair of viewport/ortho and switch to those before rendering the gui.
	Viewport viewport;
	OrthographicCamera ortho;

	@Override public void create() {
		batch = new SpriteBatch();
		texture = new Texture("sheet.png");
		grassRegion = new TextureRegion(texture, 1, 1, 20, 20);
		waterRegion = new TextureRegion(texture, 24, 1, 20, 20);
		tiles = new boolean[16][9];

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 9; y++) {
				boolean tile = MathUtils.randomBoolean();
				tiles[x][y] = tile;
			}
		}

		// orthographic ("top down") camera which we use to control x, y and zoom
		ortho = new OrthographicCamera();

		// use a 320,180 "window" into the world, also provide the camera instance to it so it will call ortho.update()
		// for us when we call viewport.update() in render()
		viewport = new FitViewport(320, 180, ortho);
	}

	@Override public void render() {
		// set the position of the camera to the center of the viewport, this effectively sets 0,0 to the bottom left
		// corner of the screen, 160,90 to the center and 319,179 to the top right corner.
		ortho.position.x = 320 / 2;
		ortho.position.y = 180 / 2;

		// provide the current size of the game window to the viewport so it can update its internals
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// make sure our batch applies the current camera when drawing
		batch.setProjectionMatrix(ortho.combined);

		batch.begin();

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 9; y++) {
				float xx = x * 20;
				float yy = y * 20;

				boolean tile = tiles[x][y];

				if (tile) {
					batch.draw(grassRegion, xx, yy);
				} else {
					batch.draw(waterRegion, xx, yy);
				}
			}
		}

		batch.end();
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Demo 3. Viewport/Camera";
		configuration.width = 640;
		configuration.height = 360;
		configuration.vSyncEnabled = false;

		new LwjglApplication(new Demo3ViewportCamera(), configuration);
	}

}
