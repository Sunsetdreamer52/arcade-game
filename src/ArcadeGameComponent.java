import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ArcadeGameComponent extends JComponent {

	private Level level;
	private JFrame frame;
	private JFrame instructionFrame;
	public int fileNum;
	public ScoreTracker tracker;

	public ArcadeGameComponent(Level level, JFrame frame, JFrame instructionFrame) {
		fileNum = 0;
		this.level = level;
		this.frame = frame;
		this.instructionFrame = instructionFrame;
		tracker = new ScoreTracker();
	}

	public void updateState() {

		level.hero.updateHero(frame, level);
		tracker.updateTimer();

		for (Collidable m : level.monsters) {
			Monster o = (Monster) m;
			o.updateMonster(frame, level);

		}

		// killing goes here?(after hero and monster have updated their positions
		ArrayList<Collidable> toRemove = new ArrayList<Collidable>();

		boolean didHeroDie = false;
		for (Collidable m : level.monsters) {
			Monster o = (Monster) m;

			if (o != null && level.hero.invincibilityTimer == 0) {
				if (CollisionTester.testGeneralCollision(level.hero.getRect(), o.getRect())) {
					if (CollisionTester.testJoustCollision(level.hero.getRect(), o.getRect())) {
						toRemove.add(o);
						level.eggs.add(new MonsterEgg(o));
						o.getEgged();

						// whenever monster dies do X
					} else {
						didHeroDie = true;
					}
				}
			}
		}

		for (Collidable m : level.monsters) {

			try {
				RangedMonster r = (RangedMonster) m;

				Rectangle2D.Double laser = r.getLaserRect();
				if (CollisionTester.testGeneralCollision(level.hero.getRect(), laser)) {
					didHeroDie = true;
				}
			} catch (ClassCastException e) {
			} catch (NullPointerException n) {
			}
		}

		ArrayList<Collidable> eggsToRemove = new ArrayList<Collidable>();

		// respawn eggos
		for (Collidable c : level.eggs) {
			MonsterEgg e = (MonsterEgg) c;

			if (e.updateEgg(frame, level)) {
				level.monsters.add(e.monster);
				e.monster.randomizeMovement();
				eggsToRemove.add(e);

			} else if (!e.eggInvincibility && CollisionTester.testGeneralCollision(e.getRect(), level.hero.rect)) {
				eggsToRemove.add(e);
				tracker.grabEgg(e.monster.scoreValue);
			}
		}

		level.eggs.removeAll(eggsToRemove);
		level.monsters.removeAll(toRemove);
		if (didHeroDie && level.hero.invincibilityTimer == 0) {
			tracker.loseLife();
			for (Collidable c : level.monsters) {
				Monster o = (Monster) c;
				o.rect = new Rectangle2D.Double(o.startX, o.startY, o.WIDTH, o.HEIGHT);
				o.getEgged();
			}
			if (tracker.lives <= 0)
				level.hero.heroDead();
			level.hero.rect = new Rectangle2D.Double(level.hero.startX, level.hero.startY, level.hero.WIDTH,
					level.hero.HEIGHT);
			level.hero.invincibilityTimer = 369;
		}
		if (level.monsters.isEmpty() && level.eggs.isEmpty()) {
			if (fileNum == level.getFiles().size() - 1) {
				tracker.highScore();
			} else {
				fileNum++;
				level.readFile(fileNum);
				tracker.lives++;
			}
		}
	}

	public void drawScreen() {
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(level.backgrounds.get(level.theme), 0, 0, level.getPreferredWidth(), level.getPreferredHeight(),
				null);
		for (Collidable cur : level.objects) {
			cur.drawOn(g2);
		}
		for (Collidable m : level.monsters) {
			m.drawOn(g2);
		}
		for (Collidable e : level.eggs) {
			e.drawOn(g2);
		}

		if (tracker.lives > 0)
			level.hero.drawOn(g2);

		if (level.monsters.isEmpty() && level.eggs.isEmpty()) {
			if (fileNum == level.getFiles().size() - 1) {
				Font WIN = new Font("Comic Sans", Font.PLAIN, 200);
				g.setColor(Color.GREEN);
				g.setFont(WIN);
				g.drawString("YOU WIN!!!", 50, 405);
				if (level.hero.invincibilityTimer <=0)
					Main.startGame();
			}

		}

		tracker.drawOn(g2);
	}

}
