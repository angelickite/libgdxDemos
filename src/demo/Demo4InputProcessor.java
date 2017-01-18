package demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
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

// InputProcessor is a libgdx core interface that has methods for handling input events.
// we can hook up our key input logic or mouse input logic with that.
public class Demo4InputProcessor extends ApplicationAdapter implements InputProcessor {

	SpriteBatch batch;
	Viewport viewport;
	OrthographicCamera ortho;
	Texture texture;
	TextureRegion grassRegion;
	TextureRegion waterRegion;
	boolean[][] tiles;

	// used to draw a tile selection indicator image
	int selectedTileX, selectedTileY;
	boolean tileSelected;

	TextureRegion selectionRegion;

	@Override public void create() {
		batch = new SpriteBatch();
		ortho = new OrthographicCamera();
		viewport = new FitViewport(320, 180, ortho);
		texture = new Texture("sheet2.png");
		grassRegion = new TextureRegion(texture, 1, 1, 20, 20);
		waterRegion = new TextureRegion(texture, 23, 1, 20, 20);
		tiles = new boolean[16][9];

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 9; y++) {
				boolean tile = MathUtils.randomBoolean();
				tiles[x][y] = tile;
			}
		}

		selectionRegion = new TextureRegion(texture, 1, 23, 20, 20);

		// We hook up ourselves as the receiver for all input events. (works because we implement InputProcessor) 
		Gdx.input.setInputProcessor(this);
	}

	@Override public void render() {
		ortho.position.x = 320 / 2;
		ortho.position.y = 180 / 2;

		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

		if (tileSelected) {
			float xx = selectedTileX * 20;
			float yy = selectedTileY * 20;
			batch.draw(selectionRegion, xx, yy);
		}

		batch.end();
	}

	// ---
	// In a fancier application we could create input queues, input distribution between different sub-systems
	// or whatever else our architecture intends to do.

	@Override public boolean keyDown(int keycode) {
		return false;
	}

	@Override public boolean keyUp(int keycode) {
		return false;
	}

	@Override public boolean keyTyped(char character) {
		return false;
	}

	// this method is not called mouseDown due to android having no mouse, but rather multiple pointer with
	// their own id (i.e. one per finger), so its called whenever the screen has been "touched", be it by a finger
	// or a mouse pointer/button
	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if (button == Buttons.LEFT) {
			// transform from screen coordinates to world coordinates. Where we clicked depends on how
			// the viewport presents the world to us (indirectly through the camera and rendering).
			Vector2 mouseWorld = viewport.unproject(new Vector2(screenX, screenY));

			// calculate the tile that we clicked, since we setup our tiles to be drawn starting at 0,0
			// we can simple divide by 20 to get the x and y positions.
			int tileX = (int) (mouseWorld.x / 20);
			int tileY = (int) (mouseWorld.y / 20);
			// clamp the result to be within the grid, this way if the user clicked outside the grid we "move"
			// to a tile within the grid
			tileX = MathUtils.clamp(tileX, 0, 15);
			tileY = MathUtils.clamp(tileY, 0, 8);

			// toggle the tile selection
			if (selectedTileX == tileX && selectedTileY == tileY) {
				// same tile again -> disable selection
				tileSelected = false;
				selectedTileX = -1;
				selectedTileY = -1;
			} else {
				// different tile -> move selection
				tileSelected = true;
				selectedTileX = tileX;
				selectedTileY = tileY;
			}
		}

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

	public static void main(String[] args) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Demo 4. InputProcessor";
		configuration.width = 640;
		configuration.height = 360;
		configuration.vSyncEnabled = false;

		new LwjglApplication(new Demo4InputProcessor(), configuration);
	}

}
