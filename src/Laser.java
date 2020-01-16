import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Laser extends Collidable {

	int laserProgress;
	int direction;
	double laserChargePercent;

	final double LASER_RANGE = 300;
	final double TRACER_WIDTH = 30;
	final double LIVE_WIDTH = 10;
	final double TRACER_CHARGE_CONSTANT = 0.1;
	final double LIVE_CHARGE_CONSTANT = 0.50;
	final double LIVE_FIRING_CONSTANT = 1;

	BufferedImage tracerLaser;
	BufferedImage liveLaserRight;//facing right
	BufferedImage liveLaserLeft;//facing left
	double x, y;
	double monsterWidth;
	
	/**
	 * 
	 * @param centerX of its RangedMonster parent class
	 * @param centerY of its RangedMonster parent class
	 * @param initial dx of its RangedMonster parent class
	 */
	public Laser(double x, double y, double dx,double width, String tracer, String liveRight,String liveLeft) {
		updateLaser(x, y, dx);
		laserProgress = 0;
		laserChargePercent = Math.random()*100;
		monsterWidth = width;
		try {
			this.tracerLaser = ImageIO.read(new File(tracer));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + tracer);
		}
		
		try {
			this.liveLaserRight = ImageIO.read(new File(liveRight));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + liveRight);
		}

		try {
			this.liveLaserLeft = ImageIO.read(new File(liveLeft));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + liveLeft);
		}
		
	}

	public void updateLaser(double x, double y, double dx) {

		this.x = x;
		this.y = y;

		if (dx > 0) {
			direction = 1;
		}
		if (dx < 0) {
			direction = -1;
		}

		progressLaser();
	}

	@Override
	public void drawOn(Graphics2D g) {
		// draw the laser first before doing the RangedMonster
		if(getRect()!=null) {
		
			Rectangle2D.Double r = getRect();
			if (laserProgress == 1)
			{
				if((Math.random()*100)<laserChargePercent)
				g.drawImage(tracerLaser,(int)r.getX(),(int)r.getY(),(int)LASER_RANGE,(int)TRACER_WIDTH,null);
			}
			else {
				if(direction == -1)
				g.drawImage(liveLaserLeft,(int)r.getX(),(int)r.getY(),(int)LASER_RANGE,(int)LIVE_WIDTH,null);
				else
					g.drawImage(liveLaserRight,(int)r.getX(),(int)r.getY(),(int)LASER_RANGE,(int)LIVE_WIDTH,null);
			}
		}
		g.setColor(Color.BLACK);
	}

	@Override
	public Rectangle2D.Double getRect() {

		Rectangle2D.Double laserRect = null;

		if(laserProgress == 1) {
			if (direction == -1) {
				laserRect = new Rectangle2D.Double(x - LASER_RANGE, y - (TRACER_WIDTH / 2), LASER_RANGE, TRACER_WIDTH);
			}
			if (direction == 1) {
				laserRect = new Rectangle2D.Double(x, y - (TRACER_WIDTH / 2), LASER_RANGE+monsterWidth, TRACER_WIDTH);
			}
		}
		if (laserProgress == 2) {
			if (direction == -1) {
				laserRect = new Rectangle2D.Double(x - LASER_RANGE, y - (LIVE_WIDTH / 2), LASER_RANGE, LIVE_WIDTH);
			}
			if (direction == 1) {
				laserRect = new Rectangle2D.Double(x, y - (LIVE_WIDTH / 2), LASER_RANGE+monsterWidth, LIVE_WIDTH);
			}
		}

		return laserRect;
	}

	public void progressLaser() {
		if (laserProgress == 0) {
			// increment progress count
			laserChargePercent += TRACER_CHARGE_CONSTANT;
			if (laserChargePercent > 100) {
				laserProgress = 1;
				laserChargePercent = 0;
			}
		}
		if (laserProgress == 1) {
			laserChargePercent += LIVE_CHARGE_CONSTANT;
			if (laserChargePercent > 100) {
				laserProgress = 2;
				laserChargePercent = 0;

			}
		}
		if (laserProgress == 2) {
			laserChargePercent += LIVE_FIRING_CONSTANT;
			if (laserChargePercent > 100) {
				laserProgress = 0;
				laserChargePercent = 0;
			}
		}
	}
}
