import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class RangedMonster extends Monster {

	Laser laserBeam;
	BufferedImage rightHonk;
	BufferedImage leftHonk;
	BufferedImage rightImagePowered;
	BufferedImage leftImagePowered;
	boolean isMoving;
	final int scoreValue = 500;

	public RangedMonster(double x, double y, String rightImg, String leftImg, String rightImgPwrd, String leftImgPwrd,
			String rightHonk, String leftHonk, String eggImg) {
		super(x, y, rightImg, leftImg, eggImg);

		this.dx = (Math.random() - 0.5) * 8;
		if (Math.abs(dx) < 3.0) {
			if (Math.random() > 0.5)
				dx = 3.0;
			else
				dx = -3.0;
		}
		
		isMoving = true;
		
		this.dy = Math.random() - 0.5;
		laserBeam = new Laser(x, y, dx, WIDTH, "Jousters/goose-tracer-glow.png", "Jousters/goose-live-right.png", "Jousters/goose-live-left.png");

		try {
			this.rightHonk = ImageIO.read(new File(rightHonk));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + rightHonk);
		}

		try {
			this.leftHonk = ImageIO.read(new File(leftHonk));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + leftHonk);
		}

		try {
			this.rightImagePowered = ImageIO.read(new File(rightImgPwrd));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + rightImgPwrd);
		}

		try {
			this.leftImagePowered = ImageIO.read(new File(leftImgPwrd));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + leftImgPwrd);
		}
	}

	@Override
	public void getEgged() {
		laserBeam.laserProgress = 0;
		laserBeam.laserChargePercent = Math.random() * 75;
	}

	public Rectangle2D.Double getLaserRect() {
		if (laserBeam.laserProgress == 2)
			return laserBeam.getRect();
		else
			return null;
	}

	@Override
	public void drawOn(Graphics2D g) {

		laserBeam.drawOn(g);

		g.setColor(Color.MAGENTA);
		if (goingRight) {

			if (laserBeam.laserProgress != 2) {
				if (!isMoving) {
					g.drawImage(this.rightImage, (int) x, (int) y, WIDTH, HEIGHT, null);
				} else
					g.drawImage(this.rightImagePowered, (int) x, (int) y, WIDTH, HEIGHT, null);
			} else {
				g.drawImage(this.rightHonk, (int) x, (int) y, WIDTH, HEIGHT, null);
			}
		} else {
			if (laserBeam.laserProgress != 2) {
				if(!isMoving)
				{
					g.drawImage(this.leftImage, (int) x, (int) y, WIDTH, HEIGHT, null);
				}
				else
					g.drawImage(this.leftImagePowered, (int) x, (int) y, WIDTH, HEIGHT, null);
			} else {
				
				g.drawImage(this.leftHonk, (int) x, (int) y, WIDTH, HEIGHT, null);
			}
		}
		g.setColor(Color.BLACK);

	}

	@Override
	public void updateMonster(JFrame frame, Level level) {

		// most of this is the same as Monster's updateMonster
		// add special case for staying in place while either tracerLaserActive or
		// liveLaserActive are true

		Rectangle2D.Double ghostRect = new Rectangle2D.Double(rect.getX() + dx, rect.getY() + dy, WIDTH, HEIGHT);

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

		double newXPos = CollisionTester.collisionBeamHorizontal(rect, dx, dy, level.getObjects());
		double newYPos = CollisionTester.collisionBeamVertical(rect, dy, level.getObjects());

		if ((dy < 0 && newYPos > ghostRect.getY()) || (dy > 0 && newYPos < ghostRect.getY())) {
			dy *= -1;

			if (Math.random() < 0.2) {
				dx *= -1;
			}

		}
		if ((dx < 0 && newXPos > ghostRect.getX()) || (dx > 0 && newXPos < ghostRect.getX())) {

			dx *= -1;
		}
		isMoving = true;
		if (laserBeam.laserProgress != 0) {
			ghostRect = new Rectangle2D.Double(rect.getX(), rect.getY(), WIDTH, HEIGHT);
		isMoving = false;
		}

		rect = ghostRect;

		x = rect.getX();
		y = rect.getY();
		laserBeam.updateLaser(rect.getCenterX(), rect.getCenterY(), dx);

		updateDirection(dx);
	}

}
