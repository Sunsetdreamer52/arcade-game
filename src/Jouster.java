import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public abstract class Jouster extends Collidable {
	
	double x, y;
	final int WIDTH = 40;
	final int HEIGHT = 40;
	double startX, startY;
	BufferedImage rightImage;//facing right
	BufferedImage leftImage;//facing left
	boolean goingRight;
	
	Rectangle2D.Double rect;
	ArcadeGameComponent component;
	
	public void drawOn(Graphics2D g) {
		g.fill(rect);
		g.setColor(Color.BLACK);
	}
	
	@Override
	public Rectangle2D.Double getRect() {
		return rect;
	}
	
	public void updateDirection(double dx) {
		if(dx>0) {
			goingRight = true;
		}
		if(dx<0) {
			goingRight = false;
		}
	}
	
	public void setImages(String rightImg,String leftImg)
	{
		try {
			this.rightImage = ImageIO.read(new File(rightImg));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + rightImg);
		}
		
		try {
			this.leftImage = ImageIO.read(new File(leftImg));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + leftImg);
		}
	}
	
	abstract Rectangle2D.Double jousterOnscreen(Rectangle2D.Double ghostRect, JFrame frame);
	
}