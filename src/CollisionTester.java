import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class CollisionTester {

	public static boolean testGeneralCollision(Rectangle2D.Double r1, Rectangle2D.Double r2) {

		return r1.intersects(r2);
	}

	// only call this if testGeneralCollision eval to true
	public static boolean testJoustCollision(Rectangle2D.Double hero, Rectangle2D.Double monster) {

		if (hero.getY() < monster.getY())
			return true;

		return false;
	}

	/**
	 * this method may or may not fix collisions and minecraft horse step mode it'll
	 * definitely fix phasing through blocks tho for sure
	 */
	public static double collisionBeamVertical(Rectangle2D.Double creature, double dy, ArrayList<Collidable> array) {
		Rectangle2D.Double beam = new Rectangle2D.Double(creature.getX(), creature.getY() + Math.abs(dy),
				creature.getWidth(), Math.abs(dy));
		if (dy > 0) {
			beam = new Rectangle2D.Double(creature.getX(), creature.getY() + creature.getHeight(), creature.getWidth(),
					dy);
		}

		ArrayList<Collidable> collidedRects = new ArrayList<Collidable>();

		for (Collidable c : array) {
			if (beam.intersects(c.getRect())) {
				collidedRects.add(c);
			}
		}
		if (!collidedRects.isEmpty()) {
			double yDistanceTocreature = Math
					.abs(creature.getY() + creature.getHeight() - collidedRects.get(0).getRect().getY());
			int shortestDistanceIndex = 0;
			for (int i = 0; i < collidedRects.size(); i++) {
				if (Math.abs(creature.getY() + creature.getHeight()
						- collidedRects.get(i).getRect().getY()) < yDistanceTocreature) {
					shortestDistanceIndex = i;
					yDistanceTocreature = Math.abs(creature.getY() - collidedRects.get(i).getRect().getY());
				}
			}

			if (dy > 0) {
				return collidedRects.get(shortestDistanceIndex).getRect().getY() - creature.getHeight();
			}

			if (dy < 0) {
				return collidedRects.get(shortestDistanceIndex).getRect().getY()
						+ collidedRects.get(shortestDistanceIndex).getRect().getHeight();
			}
		}

		return creature.getY() + dy;
	}

	public static double collisionBeamHorizontal(Rectangle2D.Double creature, double dx, double dy,
			ArrayList<Collidable> array) {

		Rectangle2D.Double beam = new Rectangle2D.Double(creature.getX() - Math.abs(dx) - creature.getWidth(),
				creature.getY(), Math.abs(dx) + creature.getWidth(), creature.getHeight());

		if (dx > 0) {
			beam = new Rectangle2D.Double(creature.getX(), creature.getY(), dx + creature.getWidth(),
					creature.getHeight());
		}

		double newYPos = CollisionTester.collisionBeamVertical(creature, dy, array);

		if ((dy < 0 && newYPos > creature.getY())) {

			beam = new Rectangle2D.Double(creature.getX() - Math.abs(dx) - creature.getWidth(),
					creature.getY() + creature.getHeight() - 3, Math.abs(dx) + creature.getWidth(), 2);

			if (dx > 0) {
				beam = new Rectangle2D.Double(creature.getX(), creature.getY() + creature.getHeight() - 3,
						dx + creature.getWidth(), 2);
			}
		}
		if (dy > 0 && newYPos < creature.getY()) {
			beam = new Rectangle2D.Double(creature.getX() - Math.abs(dx) - creature.getWidth(), creature.getY() - 3,
					Math.abs(dx) + creature.getWidth(), 2);

			if (dx > 0) {
				beam = new Rectangle2D.Double(creature.getX(), creature.getY() - 3, dx + creature.getWidth(), 2);
			}
		}

		ArrayList<Collidable> collidedRects = new ArrayList<Collidable>();

		for (Collidable c : array) {
			if (beam.intersects(c.getRect())) {
				collidedRects.add(c);
			}
		}

		if (!collidedRects.isEmpty()) {
			double xDistanceTocreature = Math.abs(creature.getX() - collidedRects.get(0).getRect().getX());

			int shortestDistanceIndex = 0;
			for (int i = 0; i < collidedRects.size(); i++) {
				if (Math.abs(creature.getX() - collidedRects.get(i).getRect().getX()) < xDistanceTocreature) {
					shortestDistanceIndex = i;
					xDistanceTocreature = Math.abs(creature.getX() - collidedRects.get(i).getRect().getX());
				}
			}

			if (dx > 0) {
				return collidedRects.get(shortestDistanceIndex).getRect().getX() - creature.getWidth();
			}
			if (dx < 0) {
				return collidedRects.get(shortestDistanceIndex).getRect().getX()
						+ collidedRects.get(shortestDistanceIndex).getRect().getWidth() + 1;
			}
		}
		return creature.getX() + dx;
	}
}