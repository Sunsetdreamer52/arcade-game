import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class MonsterEgg extends Collidable{

	Monster monster;
	double eggTimer;
	double EGG_CONSTANT;
	boolean eggInvincibility;
	final double EGGRAVITY = 0.069;
	
	BufferedImage eggImage;
	
	public MonsterEgg(Monster monster)
	{
		this.monster = monster;
		eggTimer = 0;
		eggInvincibility = true;
		
		eggImage = monster.getEggImage();
	}
	
	@Override
	public void drawOn(Graphics2D g) {
		g.setColor(Color.YELLOW);
		//g.fill(monster.getRect());
		g.drawImage(this.eggImage, (int)monster.getRect().getX(), (int)monster.getRect().getY(), monster.WIDTH, monster.HEIGHT, null);
		g.setColor(Color.BLACK);
	}

	@Override
	public Rectangle2D.Double getRect() {
		return monster.getRect();
	}
	
	public boolean updateEgg(JFrame frame, Level level)
	{
		eggTimer+=0.1;
		if(eggTimer>75)
		{
			return true;
		}
		
		
		monster.dy += EGGRAVITY;
		Rectangle2D.Double ghostRect = new Rectangle2D.Double(monster.x, monster.rect.getY() + monster.dy, monster.rect.getWidth(), monster.rect.getHeight());

		
		double newYPos = CollisionTester.collisionBeamVertical(monster.rect, monster.dy, level.getObjects());

		if ((monster.dy < 0 && newYPos > ghostRect.getY()) || (monster.dy > 0 && newYPos < ghostRect.getY())) {
			monster.dy = 0;
			eggInvincibility = false;
		}

		ghostRect = new Rectangle2D.Double(monster.x, newYPos, monster.rect.getWidth(), monster.rect.getHeight());

		
		if (ghostRect.getY() > frame.getComponent(0).getHeight() - monster.rect.getHeight()) {
			ghostRect = new Rectangle2D.Double(ghostRect.getX(), frame.getComponent(0).getHeight() - monster.rect.getHeight(), monster.rect.getWidth(),
					monster.rect.getHeight());
			monster.dy = 0;
		} else if (ghostRect.getY() < 0) {
			ghostRect = new Rectangle2D.Double(ghostRect.getX(), 0, monster.rect.getWidth(), monster.rect.getHeight());
			monster.dy = 0;
		}

		monster.rect = ghostRect;
		monster.x = monster.rect.getX();
		monster.y = monster.rect.getY();
		
		
		return false;
	}

}
