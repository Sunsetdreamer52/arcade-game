import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InstructionPanel extends JPanel {
	
	BufferedImage instructions;

	public InstructionPanel() {
		try {
			this.instructions = ImageIO.read(new File("instructions.png"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load image file " + "instructions.png");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(instructions, 0, 0, 1440, 810, null);
	}
}
