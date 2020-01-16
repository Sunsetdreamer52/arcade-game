import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The main class for your arcade game.
 * 
 * You can design your game any way you like, but make the game start by running
 * main here.
 * 
 * Also don't forget to write javadocs for your classes and functions!
 * 
 * @author Brian Chan and Veronica Kleinschmidt
 *
 */
public class Main {
	
	static int gameRunning;
	static JFrame gameFrame;
	static JFrame instructionFrame;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		gameFrame = new JFrame("Arcade Game");
		instructionFrame = new JFrame("Instructions");
		JPanel instructionPanel = new InstructionPanel();
		JButton closeInstruction = new JButton("Back to menu");
		instructionFrame.add(instructionPanel, BorderLayout.NORTH);
		instructionFrame.add(closeInstruction, BorderLayout.SOUTH);

		Level level = new Level(gameFrame);
		ArcadeGameComponent component = new ArcadeGameComponent(level, gameFrame, instructionFrame);
		component.setPreferredSize(new Dimension(level.getPreferredWidth(), level.getPreferredHeight()));
		level.setComponent(component);
		gameFrame.add(component, BorderLayout.CENTER);
		gameFrame.pack();
		
		class startGameListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		}
		closeInstruction.addActionListener(new startGameListener());
		instructionFrame.repaint();
		instructionFrame.pack();

		GameAdvanceListener advanceListener = new GameAdvanceListener(component);

		Timer t = new Timer(1, advanceListener);

		t.start();

		class KeyboardListener implements KeyListener {

			Level level;

			public KeyboardListener(Level level) {
				this.level = level;
			}

			@Override
			public void keyPressed(KeyEvent e) {

				char key = (char) e.getKeyCode();
				// System.out.println(key);
				if (key == '%') {
					level.hero.setDx(-2.69);
					// System.out.println("left");
				}
				if (key == '\'') {
					level.hero.setDx(2.69);
					// System.out.println("right");
				}

				if (key == 'D') {
					if (component.fileNum == 0)
						component.fileNum = level.getFiles().size() - 1;
					else
						component.fileNum--;
					level.readFile(component.fileNum);
				}
				if (key == 'U') {
					if (component.fileNum == level.getFiles().size() - 1)
						component.fileNum = 0;
					else
						component.fileNum++;
					level.readFile(component.fileNum);
				}
				component.repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				char key = (char) e.getKeyCode();
				if (key == '%') {
					level.hero.setDx(0);
				}

				if (key == '\'') {
					level.hero.setDx(0);
				}
				if (key == ' ' || key == '&') {
					level.hero.flap(level);
				}
				component.repaint();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		}
		gameFrame.addKeyListener(new KeyboardListener(level));
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameRunning = startGame();
		while (gameRunning==0||gameRunning==1) {
			if (gameRunning==1) {
				if(instructionFrame==null)
					gameRunning = startGame();
			}
		}
		gameFrame.dispose();
		instructionFrame.dispose();
	}

	public static int startGame() {
		gameFrame.setVisible(false);
		instructionFrame.setVisible(false);
		Object[] possibleValues = { "Start Game", "Instructions", "Quit" };
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose one", "Input", JOptionPane.INFORMATION_MESSAGE,
				null, possibleValues, possibleValues[0]);
		if (selectedValue.equals("Start Game")) {
			gameFrame.setVisible(true);
			return 0;
		}
		else if (selectedValue.equals("Instructions")) {
			instructionFrame.setVisible(true);
			return 1;
		}
		else
			return 2;
	}
}
