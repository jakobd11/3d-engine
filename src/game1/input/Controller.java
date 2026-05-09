package game1.input;

import game1.Display;

public class Controller {

	public static double x, y, z, rotation, xa, za, rotationa;
	
	public static boolean turnRight = false, turnLeft = false, walk = false, sprinting = false, crouching = false;

	public void tick(boolean forward, boolean back, boolean right, boolean left, boolean jump, boolean crouch, boolean sprint) {
;
		double walkSpeed = 0.3;
		double jumpHeight = 0.5;
		double crouchHeight = 0.3;
		double xMove = 0;
		double zMove = 0;

		if (forward) {
			zMove++;
			walk = true;
		}
		if (back) {
			zMove--;
			walk = true;
		}
		if (right) {
			xMove++;
			walk = true;
		}
		if (left) {
			xMove--;
			walk = true;
		}

		if (jump) {
			y += jumpHeight;
			sprint = false;
			walk = false;
		}
		if (crouch) {
			walkSpeed*=0.5;
			y -= crouchHeight;
			sprint = false;
//			if (walk) {
//				crouching = true;
//			} else crouching = false;
		} else crouching = false;
		if (sprint) {
			walkSpeed*=2;
			crouch = false;
//			if(walk) {
//				sprinting = true;
//			} else sprinting = false;
		} else sprinting = false;
		if (!forward && !back && !right && !left) {
			walk = false;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

		x += xa;
		z += za;
		y *= 0.9;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotationa *= 0.8;
	}
}
