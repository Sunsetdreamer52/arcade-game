import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Platform extends Collidable {

	private Rectangle2D.Double rect;
	BufferedImage skin;
	
	public Platform(int x, int y, BufferedImage platform)
	{
		this.rect = new Rectangle2D.Double(x, y, 30, 30);
		skin = platform;
	}
	public Rectangle2D.Double getRect(){
		return rect;
	}
	public void drawOn(Graphics2D g) {
		g.drawImage(skin, (int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight(), null);
	}
}
