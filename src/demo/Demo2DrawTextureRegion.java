package demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Demo2DrawTextureRegion extends ApplicationAdapter {

	SpriteBatch batch;
	Texture texture;

	TextureRegion grassRegion;
	TextureRegion waterRegion;

	boolean[][] tiles;

	@Override public void create() {
		batch = new SpriteBatch();
		texture = new Texture("sheet.png");

		// TextureRegion := rectangular reference to a texture; part of a texture
		grassRegion = new TextureRegion(texture, 1, 1, 20, 20); //  x= 1, y=1, width=20, height=20 
		waterRegion = new TextureRegion(texture, 24, 1, 20, 20); // x=24, y=1, width=20, height=20 
		// origin: x=0,y=0 is top left of the texture

		// note: i've manually added a 1px gap around the two images on the texture.
		//       this counteracts the phenomenon called "Texture Bleeding" where some
		//       calculations during texture sampling on the gpu result in artifacts during the rendering.
		//       this process later will be automated with a texture packer tool which takes a bunch of individual
		//       images and packs them into a single texure. this tool also includes the gaps around the images
		//       and lets us quickly retrieve the regions by a unique id (image filename by default).

		// encoding: true=grass, false=water
		tiles = new boolean[16][9];

		// random tile setup
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 9; y++) {
				// MathUtils: has functions for sin/cos, random number generation, ...
				boolean tile = MathUtils.randomBoolean();
				tiles[x][y] = tile;
			}
		}
	}

	@Override public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 9; y++) {
				float xx = x * 20;
				float yy = y * 20;

				boolean tile = tiles[x][y];

				// at this point, when rendering, we benefit from the texture regions:
				// instead of having to send one texture per image to the gpu, we only send one texture
				// and then specify which area of that texture is used during rendering
				// this increases performance immensely by reducing cpu<->gpu communication and also
				// gpu internally switching textures costs a lot which is preferably avoided
				// note: texture switching cant always be avoided, but the less the better
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
		configuration.title = "Demo 2. Draw TextureRegion";
		configuration.width = 640;
		configuration.height = 360;
		configuration.vSyncEnabled = false;

		new LwjglApplication(new Demo2DrawTextureRegion(), configuration);
	}

}
