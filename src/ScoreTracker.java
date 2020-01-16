import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ScoreTracker {

	double multiplierTimer;
	int score;
	int lives;
	double divideCooldown;
	final double COOLDOWN = 0.2069;
	PrintWriter pw;
	BufferedImage onFire;

	public ScoreTracker() {
		score = 0;
		lives = 3;
		multiplierTimer = 1;
		divideCooldown = 100;
		try {
			FileWriter fw = new FileWriter("highScores.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
		} catch (IOException e) {}

		
		try {
			this.onFire = ImageIO.read(new File("Jousters/fire.png"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + "fire");
		}
}

	public void grabEgg(double scoreValue) {
		score += (int)(scoreValue * ((int) multiplierTimer));

		multiplierTimer *= 2;
		divideCooldown +=25;
	}

	public boolean loseLife() {
		lives--;
		multiplierTimer = 1;
		if (lives == 0)
			return false;
		return true;
		//if this thingy ever returns false end the game end the hero end everything and try to bring it back to the menu screen?
	}
	
	public void resetLives(){
		lives = 3;
	}
	
	public void updateTimer() {
		divideCooldown-=COOLDOWN;
		if(divideCooldown<=0)
		{
			if(multiplierTimer>1)
			multiplierTimer/=2;
			
			divideCooldown = 100;
		}
	}

	public void highScore() {
		pw.println(JOptionPane.showInputDialog("Enter your name to be added to the high scores list")+"\t"+score);
	}
	
	public void drawOn(Graphics2D g) {
		g.fillRect(0,790,200,20);
		
		if((int)multiplierTimer == 1)
		g.fillRect(300,790,75,20);
		else
		{
			g.drawImage(onFire,290,780,95,30,null);
		}
		
		
		g.fillRect(700,790,100,20);
		
		Font FONT = new Font("Lucida Sans", Font.PLAIN, 20);
		g.setFont(FONT);
		g.setColor(Color.WHITE);
		
		//draws the score
		g.drawString("Score:", 20, 810);
		String tempScore = new Integer(score).toString();
		g.drawString(tempScore, 80, 810);
		
		//draws the multiplier
		String tempMultiply = new Integer((int)multiplierTimer).toString();
		g.drawString(tempMultiply+" X",315,810);
		
		//draws the lives
		g.drawString("Lives:",710,810);
		String tempLives = new Integer(lives).toString();
		g.drawString(tempLives,770,810);
		
		//game over message!
		if(lives == 0)
		{
			Font DEATH = new Font("Chiller", Font.PLAIN, 200);
			g.setColor(Color.RED);
			g.setFont(DEATH);
			g.drawString("GAME OVER!!!", 250,405);
			long startTime = System.currentTimeMillis();
			while (System.currentTimeMillis()-startTime<3000) {
				;
			}
			Main.startGame();
		}
		g.setColor(Color.BLACK);
	}
}
