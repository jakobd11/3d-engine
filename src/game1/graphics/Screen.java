package game1.graphics;

import java.util.*;

import game1.Game;

public class Screen extends Render {

	private Render3D render;

	public Screen(int width, int height) {
		super(width, height);
		render = new Render3D(width, height);

	}

	public void render(Game game) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
		
		Random random = new Random(42);
		
		render.floor(game);
		
		for (int i = 0; i < 100; i++) {
			int x = random.nextInt(20) - 10;
			int z = random.nextInt(20) - 10;
			int thickness = random.nextInt(3) + 1;
			int length = random.nextInt(3)+1;
			if (Math.abs(x) < 3 && Math.abs(z) < 3) continue;
			Block.Chunk(render, x, z, thickness, length);
		}

		render.renderDistanceLimiter();
		
		draw(render ,0 ,0);
	}
}
