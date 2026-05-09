package game1.graphics;

import java.util.Random;

import game1.Game;
import game1.input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	private double renderDistance = 4000;

	public static double forward;
	public static double right;
	public static double up;
	public static double rotation;
	public static double cosine;
	public static double sine;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}

	public void floor(Game game) {

//		double renderDistance = 200;

		double floorPos = 8.0;
		double ceilingPos = 8.0;

		forward = game.controls.z;
		right = game.controls.x;
		up = game.controls.y;

		double walking = 0;
//		if (Controller.walk) {
//			walking = Math.sin(game.time / 6.0)*1;
//		} else if (Controller.sprinting) {
//			walking = Math.sin(game.time / 6.0)*10;
//		} else if (Controller.crouching) {
//			walking = Math.sin(game.time / 6.0)*0.1;
//		}
		rotation = game.controls.rotation;

		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = (floorPos + up + walking) / ceiling;

			if (ceiling < 0) {
				z = (ceilingPos - up - walking) / -ceiling;
			}

			// floor render
			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine + right;
				double yy = z * cosine - depth * sine + forward;
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);

				// render fade
				zBuffer[x + y * width] = z;

				// render limit
				if (z < 500) {
					pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
				}
//	Old Render Distance				
//				if (xx < right+renderDistance && yy < forward+renderDistance && xx > right-renderDistance && yy > forward-renderDistance)
//					pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
//				else
//					pixels[x + y * width] = 0;
			}
		}
	}

	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {

		// calculating left corner of wall relative to player
		double xcLeft = ((xLeft) - right / 8);
		double zcLeft = ((zDistanceLeft) - forward / 8);

		// calculating rotation of left corner of wall
		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		// calculating height of left corner of wall
		double yCornerTL = ((-yHeight) + (up / 16));
		double yCornerBL = ((0.5 - yHeight) + (up / 16));

		// calculating right corner of wall relative to player
		double xcRight = ((xRight) - right / 8);
		double zcRight = ((zDistanceRight) - forward / 8);

		// calculating rotation of right corner of wall
		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;

		// calculating height of left corner of wall
		double yCornerTR = ((-yHeight) + (up / 16));
		double yCornerBR = ((0.5 - yHeight) + (up / 16));

		// assigning pixel x-z location
		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

		double tex30 = 0;
		double tex40 = 8;
		double clip = 0.5;

		if (rotLeftSideZ < clip && rotRightSideZ < clip)
			return;

		if (rotLeftSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotLeftSideZ - rotLeftSideZ);
			rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotLeftSideX = rotLeftSideZ + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 += (tex40 - tex30) * clip0;
		}

		if (rotRightSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotLeftSideZ - rotLeftSideZ);
			rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotRightSideX = rotLeftSideZ + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 += (tex40 - tex30) * clip0;
		}

		// if the pixels swap location they dont render
		if (xPixelLeft >= xPixelRight)
			return;

		// int cast for bounds
		int xPixelLeftInt = (int) (xPixelLeft);
		int xPixelRightInt = (int) (xPixelRight);

		// resets pixels to no go out of screen bounds *must be int*
		if (xPixelLeftInt < 0)
			xPixelLeftInt = 0;
		if (xPixelRightInt > width)
			xPixelRightInt = width;

		// extending x-z pos to y
		double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
		double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
		double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
		double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);

		// texture jargon
		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = tex30 / rotLeftSideZ;
		double tex4 = tex40 / rotRightSideZ - tex3;

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / (tex1 + (tex2 - tex1) * pixelRotation));

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

			// int cast
			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);

			// bounding *int*
			if (yPixelTopInt < 0)
				yPixelTopInt = 0;
			if (yPixelBottomInt > height)
				yPixelBottomInt = height;

			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
				int yTexture = (int) (8 * pixelRotationY);
				pixels[x + y * width] = Texture.floor.pixels[(xTexture & 7) + (yTexture & 7) * 8];
				// render fade
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
			}

		}

		System.out.println("rotZ: " + rotLeftSideZ + " floorZ: " + (8.0 / ((height / 2.0 - height / 2.0) / height)));
	}

	public void renderDistanceLimiter() {
		for (int i = 0; i < width * height; i++) {
			int colour = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));

			if (brightness < 0) {
				brightness = 0;
			}

			if (brightness > 255) {
				brightness = 255;
			}

			int r = (colour >> 16) & 0xff;
			int g = (colour >> 8) & 0xff;
			int b = (colour) & 0xff;

			r = r * brightness >>> 8;
			g = g * brightness >>> 8;
			b = b * brightness >>> 8;

			pixels[i] = r << 16 | g << 8 | b;
		}
	}
}
