package demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// ApplicationAdapter implements ApplicationListener which is the main-entry point
// for all libGDX Applications. Its based on an Interface because this allows for
// different backends like PC, Android or HTML to create the specific logic based on their
// architecture. i.e. the gameloop of a pc app is fundamentally different from the gameloop of
// an android application. Same goes for input handling, audio playback, ...
// We only care about the interfaces though and not the specific implementations, this is why most of
// libGDX is split into multiple jars. gdx-core are all the main functionality (as interfaces) and utilities.
// libgdx-lwjgl then is one backened which uses lwjgl on pc to implement the core interfaces.

// we use create() which is run once at program start and render() which is the main loop which runs at
// default 60fps and can be used for updates and rendering.
public class Demo1DrawTexture extends ApplicationAdapter {

	SpriteBatch batch;
	Texture texture;

	@Override public void create() {
		batch = new SpriteBatch();
		texture = new Texture("grass.png"); // file location: internal = "assets/grass.png"
	}

	@Override public void render() {
		// clear buffer (here with black)
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// collect a bunch of draw calls between begin/end so data has to be sent less often to the gpu
		batch.begin();

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 9; y++) {
				// 20x20px grass texture, so 20px spacing
				float xx = x * 20;
				float yy = y * 20;
				batch.draw(texture, xx, yy); // draw: stores the draw intent internally
			}
		}

		batch.end(); // end: will flush all collected data to the gpu (if any available)
	}

	public static void main(String[] args) {
		// the lwjgl configuration has some options we can set like framerate, window size, fullscreen or not, ...
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Demo 1. Draw Texture";
		configuration.width = 640;
		configuration.height = 360;
		configuration.vSyncEnabled = false;

		// once we create the LwjglApplication object some stuff will be set up and then the provided
		// ApplicationListener instance (in this case the Demo1DrawTexture) will be used to call create() and
		// render() on. As this is a pc executable it uses the lwjgl backend, but the ApplicationListener core interface
		// will be the same between all platforms, hence why we can write the code once but use it on different platforms.
		new LwjglApplication(new Demo1DrawTexture(), configuration);
	}

}
