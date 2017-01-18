package demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// everything cleaned up, without any comments and a few extra variables and a restart game feature (key: r)
// shows how easy it can be to get going (once you have a bit of practice)
public class Demo5CleanCode extends ApplicationAdapter implements InputProcessor {

	public static final int screenWidth = 640, screenHeight = 360;
	public static final int viewWidth = 320, viewHeight = 180;

	SpriteBatch batch;
	Viewport viewport;
	OrthographicCamera ortho;

	Texture texture;
	TextureRegion grassRegion;
	TextureRegion waterRegion;
	TextureRegion selectionRegion;

	int mapWidth = 16, mapHeight = 9;
	int tileSize = 20;

	boolean[][] tiles;

	boolean tileSelected;
	int selectedTileX, selectedTileY;

	@Override public void create() {
		Gdx.input.setInputProcessor(this);

		batch = new SpriteBatch();
		ortho = new OrthographicCamera();
		viewport = new FitViewport(viewWidth, viewHeight, ortho);

		texture = new Texture("sheet2.png");
		grassRegion = new TextureRegion(texture, 1, 1, 20, 20);
		waterRegion = new TextureRegion(texture, 23, 1, 20, 20);
		selectionRegion = new TextureRegion(texture, 1, 23, 20, 20);

		tiles = new boolean[mapWidth][mapHeight];

		reset();
	}

	private void reset() {
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				boolean tile = MathUtils.randomBoolean();
				tiles[x][y] = tile;
			}
		}
	}

	@Override public void render() {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(ortho.combined);

		batch.begin();

		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				float xx = x * tileSize;
				float yy = y * tileSize;

				boolean tile = tiles[x][y];

				if (tile) {
					batch.draw(grassRegion, xx, yy);
				} else {
					batch.draw(waterRegion, xx, yy);
				}
			}
		}

		if (tileSelected) {
			float xx = selectedTileX * tileSize;
			float yy = selectedTileY * tileSize;
			batch.draw(selectionRegion, xx, yy);
		}

		batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			reset();
		}
	}

	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			Vector2 mouseWorld = viewport.unproject(new Vector2(screenX, screenY));

			int tileX = MathUtils.clamp((int) (mouseWorld.x / tileSize), 0, mapWidth - 1);
			int tileY = MathUtils.clamp((int) (mouseWorld.y / tileSize), 0, mapHeight - 1);

			if (selectedTileX == tileX && selectedTileY == tileY) {
				tileSelected = false;
				selectedTileX = -1;
				selectedTileY = -1;
			} else {
				tileSelected = true;
				selectedTileX = tileX;
				selectedTileY = tileY;
			}
		}

		// bonus!
		if (button == Buttons.RIGHT) {
			if (tileSelected) {
				tiles[selectedTileX][selectedTileY] = !tiles[selectedTileX][selectedTileY];
			}
		}

		return false;
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Demo 5. Clean Code";
		configuration.width = screenWidth;
		configuration.height = screenHeight;
		configuration.vSyncEnabled = false;

		new LwjglApplication(new Demo5CleanCode(), configuration);
	}

	// ----------
	@Override public boolean keyDown(int keycode) {
		return false;
	}

	@Override public boolean keyUp(int keycode) {
		return false;
	}

	@Override public boolean keyTyped(char character) {
		return false;
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled(int amount) {
		return false;
	}

}
