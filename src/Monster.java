import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Monster extends Jouster {

	double dx, dy;
	Rectangle2D.Double rect;
	ArcadeGameComponent component;
	BufferedImage eggImage;
	final double scoreValue = 100;

	public Monster(double x, double y,String rightImg,String leftImg, String eggImg) {
		this.x = x - 5;
		this.y = y - 10;
		startX = this.x;
		startY = this.y;
		this.rect = new Rectangle2D.Double(this.x, this.y, WIDTH, HEIGHT);
		dx = (Math.random() - 0.5) * 1.69;
		dy = (Math.random() - 0.5);
		setImages(rightImg,leftImg);
		if (dx > 0)
			dx += 0.3;
		else
			dx -= 0.3;
		if (dy > 0)
			dy += 0.3;
		else
			dy -= 0.3;
		
		try {
			eggImage = ImageIO.read(new File(eggImg));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + "egg.png");
		}
		
		updateDirection(dx);
	}

	
	
	public void getEgged() {
		
	}
	
	public BufferedImage getEggImage() {
		return eggImage;
	}
	
	@Override
	public void drawOn(Graphics2D g) {
		g.setColor(Color.RED);
		
		if(goingRight)
			g.drawImage(this.rightImage, (int)x, (int)y, WIDTH, HEIGHT, null);
			else
			g.drawImage(this.leftImage, (int)x, (int)y, WIDTH, HEIGHT, null);
			
		
		g.setColor(Color.BLACK);
	}
	public void randomizeMovement() {
		dx = (Math.random() - 0.5) * 1.69;
		dy = (Math.random() - 0.5);
		if (dx > 0)
			dx += 0.3;
		else
			dx -= 0.3;
		if (dy > 0)
			dy += 0.3;
		else
			dy -= 0.3;
	}

	public void updateMonster(JFrame frame, Level level) {
		Rectangle2D.Double ghostRect = new Rectangle2D.Double(rect.getX() + dx, rect.getY() + dy, WIDTH, HEIGHT);
		ghostRect = jousterOnscreen(ghostRect, frame);

		// BURROWING DUCKY
		/*
		 * if(CollisionTester.collisionBeamHorizontal(rect,dx,level.getObjects())!=dx+
		 * rect.getX()) { dx*=-1; }
		 * 
		 * if(CollisionTester.collisionBeamVertical(rect,dy,level.getObjects())!=dy+rect
		 * .getY()) { dy*=-1; dx*=-1; }
		 */
		double newXPos = CollisionTester.collisionBeamHorizontal(rect, dx, dy, level.getObjects());
		double newYPos = CollisionTester.collisionBeamVertical(rect, dy, level.getObjects());
		if ((dy < 0 && newYPos > ghostRect.getY()) || (dy > 0 && newYPos < ghostRect.getY())) {
			dy *= -1;

			if (Math.random() > 0.69)
				dx *= -1;

			// occasionally randomizes velocities to make monsters movements more
			// unpredictable
			if (Math.random() > 0.20) {
				dx = (Math.random() - 0.5) * 1.69;
				dy = (Math.random() - 0.5);
				if (dx > 0)
					dx += 0.3;
				else
					dx -= 0.3;
				if (dy > 0)
					dy += 0.3;
				else
					dy -= 0.3;

			}
			// this is a random idea for random charging boosts of speed for monsters
			/*
			 * if(Math.random() > 0.99) { dx = 2.69; }
			 */
		}
		if ((dx < 0 && newXPos > ghostRect.getX()) || (dx > 0 && newXPos < ghostRect.getX())) {

			dx *= -1;
		}

		rect = ghostRect;
		x = rect.getX();
		y = rect.getY();
		
		this.updateDirection(dx);

	}

	Rectangle2D.Double jousterOnscreen(Rectangle2D.Double ghostRect, JFrame frame) {
		if (ghostRect.getX() > frame.getComponent(0).getWidth() - WIDTH) {
			ghostRect = new Rectangle2D.Double(frame.getComponent(0).getWidth() - WIDTH - 1, ghostRect.getY(), WIDTH,
					HEIGHT);
			dx *= -1;
		} else if (ghostRect.getX() < 0) {
			ghostRect = new Rectangle2D.Double(0, ghostRect.getY(), WIDTH, HEIGHT);
			dx *= -1;
		}

		if (ghostRect.getY() > frame.getComponent(0).getHeight() - HEIGHT) {
			ghostRect = new Rectangle2D.Double(ghostRect.getX(), frame.getComponent(0).getHeight() - HEIGHT, WIDTH,
					HEIGHT);
			dy *= -1;
		} else if (ghostRect.getY() < 0) {
			ghostRect = new Rectangle2D.Double(ghostRect.getX(), 0, WIDTH, HEIGHT);
			dy *= -1;
		}
		return ghostRect;
	}

	@Override
	public Rectangle2D.Double getRect() {
		return this.rect;
	}

}