import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import javax.swing.JFrame;

public class Hero extends Jouster {

	double dx, dy;
	final double GRAVITY = 0.03;
	int invincibilityTimer;
	
	public Hero(int x, int y, ArcadeGameComponent comp, String rightImg, String leftImg) {
		this.x = x - 5;
		this.y = y - 10;
		startX = this.x;
		startY = this.y;
		dx = 0;
		dy = 0;
		
		invincibilityTimer = 269;
		goingRight = true;

		setImages(rightImg, leftImg);

		rect = new Rectangle2D.Double(this.x, this.y, WIDTH, HEIGHT);
		component = comp;
	}

	public void setDx(double newDx) {
		dx = newDx;
	}

	public void updateHero(JFrame frame, Level level) {
		// this is a "ghost rectangle", it foreshadows where the rectangle will be after
		// velocity is applied to it
		// so that collisions can be detected beforehand
		dy += GRAVITY;
		Rectangle2D.Double ghostRect = new Rectangle2D.Double(rect.getX() + dx, rect.getY() + dy, WIDTH, HEIGHT);

		double newXPos = CollisionTester.collisionBeamHorizontal(rect, dx, dy, level.getObjects());
		double newYPos = CollisionTester.collisionBeamVertical(rect, dy, level.getObjects());

		if ((dy < 0 && newYPos > ghostRect.getY()) || (dy > 0 && newYPos < ghostRect.getY())) {
			dy = 0;
		}

		ghostRect = new Rectangle2D.Double(newXPos, newYPos, WIDTH, HEIGHT);
		ghostRect = jousterOnscreen(ghostRect, frame);

		rect = ghostRect;
		x = rect.getX();
		y = rect.getY();
		updateDirection(dx);
	}

	public void flap(Level level) {
		if (dy >= 0) {
			dy = -1.69;
		} else if (dy > -3) {
			dy -= 0.69 * 1.25;
		}
	}

	@Override
	Double jousterOnscreen(Double ghostRect, JFrame frame) {
		if (ghostRect.getX() > frame.getComponent(0).getWidth() - WIDTH) {
			ghostRect = new Rectangle2D.Double(frame.getComponent(0).getWidth() - WIDTH, ghostRect.getY(), WIDTH,
					HEIGHT);
		} else if (ghostRect.getX() < 0) {
			ghostRect = new Rectangle2D.Double(0, ghostRect.getY(), WIDTH, HEIGHT);
		}

		if (ghostRect.getY() > frame.getComponent(0).getHeight() - HEIGHT) {
			ghostRect = new Rectangle2D.Double(ghostRect.getX(), frame.getComponent(0).getHeight() - HEIGHT, WIDTH,
					HEIGHT);
			dy = 0;
		} else if (ghostRect.getY() < 0) {
			ghostRect = new Rectangle2D.Double(ghostRect.getX(), 0, WIDTH, HEIGHT);
			dy = 0;
		}
		return ghostRect;
	}

	@Override
	public void drawOn(Graphics2D g) {
		// g.fill(rect);
		
		if ((invincibilityTimer % 4 != 0 || invincibilityTimer == 0)) {

			if (goingRight)
				g.drawImage(this.rightImage, (int) x, (int) y, WIDTH, HEIGHT, null);
			else
				g.drawImage(this.leftImage, (int) x, (int) y, WIDTH, HEIGHT, null);

			g.setColor(Color.BLACK);
		}

		if(invincibilityTimer>0)
		invincibilityTimer--;
		
	}
	public void heroDead() {
		invincibilityTimer = 369;
	}

}