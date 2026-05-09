package game1.graphics;

import game1.Game;

public class Block {

	public static void Chunk (Render3D render, double xPos, double zPos, double thickness, double length) {
		double blockSize = 0.5;
		
		for (int i = 0; i<thickness;i++) {
			render.renderWall(i*blockSize+xPos, i*blockSize+xPos+blockSize, zPos, zPos, 0);
			render.renderWall(i*blockSize+xPos, i*blockSize+xPos+blockSize, zPos, zPos, 0.5);
			render.renderWall(i*blockSize+xPos+blockSize, i*blockSize+xPos, zPos+length*blockSize, zPos+length*blockSize, 0);
			render.renderWall(i*blockSize+xPos+blockSize, i*blockSize+xPos, zPos+length*blockSize, zPos+length*blockSize, 0.5);
		}
		for (int i = 0; i<length;i++) {
			render.renderWall(xPos, xPos, i*blockSize+zPos+blockSize, i*blockSize+zPos, 0);
			render.renderWall(xPos, xPos, i*blockSize+zPos+blockSize, i*blockSize+zPos, 0.5);
			render.renderWall(thickness*blockSize+xPos, thickness*blockSize+xPos, i*blockSize+zPos, i*blockSize+zPos+blockSize, 0);
			render.renderWall(thickness*blockSize+xPos, thickness*blockSize+xPos, i*blockSize+zPos, i*blockSize+zPos+blockSize, 0.5);
		}
	}
}
