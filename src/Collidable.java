import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Collidable {

	abstract void drawOn(Graphics2D g);
	abstract Rectangle2D.Double getRect();
}
