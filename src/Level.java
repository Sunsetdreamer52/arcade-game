import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Level {
	JFrame frame;
	ArrayList<Collidable> objects;
	ArrayList<Collidable> monsters;
	ArrayList<Collidable> eggs;
	Hero hero;
	ArcadeGameComponent component;
	ArrayList<String> levelTitles;
	HashMap<String, BufferedImage> backgrounds;
	HashMap<String, BufferedImage> platforms;
	static final String[] themes = {"cobblestone", "stone", "spruceLog", "snowyDirt", "oakPlank", "stoneBrick", "grass"};
	String theme;
	
	public Level(JFrame j) {
		objects = new ArrayList<Collidable>();
		monsters = new ArrayList<Collidable>();
		eggs = new ArrayList<Collidable>();
		backgrounds = new HashMap<String, BufferedImage>();
		try {
		    this.backgrounds.put("cobblestone", ImageIO.read(new File("Backgrounds/cottage.png")));
		    this.backgrounds.put("stone", ImageIO.read(new File("Backgrounds/flyingNirb.png")));
		    this.backgrounds.put("spruceLog", ImageIO.read(new File("Backgrounds/largeHouse.png")));
		    this.backgrounds.put("snowyDirt", ImageIO.read(new File("Backgrounds/nirbContemplates.png")));
		    this.backgrounds.put("oakPlank", ImageIO.read(new File("Backgrounds/roseStation.png")));
		    this.backgrounds.put("stoneBrick", ImageIO.read(new File("Backgrounds/stationClose.png")));
		    this.backgrounds.put("grass", ImageIO.read(new File("Backgrounds/townScene.png")));
		} catch (IOException e) {
		    throw new RuntimeException("Could not load image file");
		}
		platforms = new HashMap<String, BufferedImage>();
		try {
		    this.platforms.put("cobblestone", ImageIO.read(new File("Platforms/cobblestone.png")));
		    this.platforms.put("stone", ImageIO.read(new File("Platforms/stone.png")));
		    this.platforms.put("spruceLog", ImageIO.read(new File("Platforms/spruce_log.png")));
		    this.platforms.put("snowyDirt", ImageIO.read(new File("Platforms/grass_block_snow.png")));
		    this.platforms.put("oakPlank", ImageIO.read(new File("Platforms/oak_planks.png")));
		    this.platforms.put("stoneBrick", ImageIO.read(new File("Platforms/stone_bricks.png")));
		    this.platforms.put("grass", ImageIO.read(new File("Platforms/grass_block_side.png")));
		} catch (IOException e) {
		    throw new RuntimeException("Could not load image file");
		}
		
		// writeFile();
		readFile("Levels/level1.txt");
		frame = j;
		levelTitles = new ArrayList<String>();
		
		levelTitles.add("Levels/level1.txt");
		levelTitles.add("Levels/level2.txt");
		levelTitles.add("Levels/level3.txt");
		levelTitles.add("Levels/level4.txt");
		levelTitles.add("Levels/level5.txt");
	}

	public Level(JFrame j, int index) {
		objects = new ArrayList<Collidable>();
		monsters = new ArrayList<Collidable>(); 
		eggs = new ArrayList<Collidable>();
		backgrounds = new HashMap<String, BufferedImage>();
		try {
		    this.backgrounds.put("cobblestone", ImageIO.read(new File("Backgrounds/cottage.png")));
		    this.backgrounds.put("stone", ImageIO.read(new File("Backgrounds/flyingNirb.png")));
		    this.backgrounds.put("spruceLog", ImageIO.read(new File("Backgrounds/largeHouse.png")));
		    this.backgrounds.put("snowyDirt", ImageIO.read(new File("Backgrounds/nirbContemplates.png")));
		    this.backgrounds.put("oakPlank", ImageIO.read(new File("Backgrounds/roseStation.png")));
		    this.backgrounds.put("stoneBrick", ImageIO.read(new File("Backgrounds/stationClose.png")));
		    this.backgrounds.put("grass", ImageIO.read(new File("Backgrounds/townScene.png")));
		} catch (IOException e) {
		    throw new RuntimeException("Could not load image file");
		}
		platforms = new HashMap<String, BufferedImage>();
		try {
		    this.platforms.put("cobblestone", ImageIO.read(new File("Platforms/cobblestone.png")));
		    this.platforms.put("stone", ImageIO.read(new File("Platforms/stone.png")));
		    this.platforms.put("spruceLog", ImageIO.read(new File("Platforms/spruce_log.png")));
		    this.platforms.put("snowyDirt", ImageIO.read(new File("Platforms/grass_block_snow.png")));
		    this.platforms.put("oakPlank", ImageIO.read(new File("Platforms/oak_planks.png")));
		    this.platforms.put("stoneBrick", ImageIO.read(new File("Platforms/stone_bricks.png")));
		    this.platforms.put("grass", ImageIO.read(new File("Platforms/grass_block_side.png")));
		} catch (IOException e) {
		    throw new RuntimeException("Could not load image file");
		}
		
		// writeFile();

		frame = j;
		ArrayList<String> levelTitles = new ArrayList<String>();
		levelTitles.add("Levels/level1.txt");
		levelTitles.add("Levels/level2.txt");
		levelTitles.add("Levels/level3.txt");
		levelTitles.add("Levels/level4.txt");
		levelTitles.add("Levels/level5.txt");
		readFile(levelTitles.get(index));
	}

	void readFile(String fileName) {
		theme = themes[(int) (Math.random()*7)];
		objects.removeAll(objects);
		monsters.removeAll(monsters);
		eggs.removeAll(eggs);
		Scanner scanner = new Scanner("");
		while (true) {
			try {
				scanner = new Scanner(new File(fileName));
			} catch (FileNotFoundException e) {
				System.out.println("Error 404 - File not found");
				// e.printStackTrace();
			}
			break;
		}

		double drawX = 0, drawY = 0;

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			for (int i = 0; i < line.length(); i++) {
				// System.out.println(line.charAt(i));
				char cur = line.charAt(i);
				if (cur == 'A')
					;
				if (cur == 'P') {
					objects.add(new Platform((int)drawX, (int)drawY, platforms.get(theme)));
					// System.out.println("Platform drawn");
				}
				if (cur == 'H') {
					hero = new Hero((int)drawX, (int)drawY, component,"Jousters/coding-duck-right.png","Jousters/coding-duck-left.png");
					// System.out.println("It's dangerous to go alone, take this!");
				}
				if(cur == 'M')
				{
					//this is the monster arraylist recruiter
					monsters.add(new Monster(drawX,drawY,"Jousters/frankenduck-right.png","Jousters/frankenduck-left.png","Jousters/blue-egg.png"));
				}
				if(cur == 'R') {
					monsters.add(new RangedMonster(drawX, drawY,"Jousters/goose-right-unpowered.png","Jousters/goose-left-unpowered.png","Jousters/goose-right-powered.png","Jousters/goose-left-powered.png","Jousters/goose-right-extend.png","Jousters/goose-left-extend.png","Jousters/red-egg.png"));
				}
				
				drawX += 30;
			}
			drawX = 0;
			drawY += 30;
		}
		scanner.close();
	}

	void readFile(int levelId) {
		while (true) {
			try {
				readFile(levelTitles.get(levelId));
			} catch (ArrayIndexOutOfBoundsException a) {
				System.out.println("Error: File No. not in database");
			}
			break;
		}
	}

	public ArrayList<Collidable> getObjects() {
		return objects;
	}

	public ArrayList<String> getFiles() {
		return levelTitles;
	}

	public void setComponent(ArcadeGameComponent comp) {
		component = comp;
	}

	public int getPreferredWidth() {
		return (int) (backgrounds.get(theme).getWidth()*0.75);
	}

	public int getPreferredHeight() {
		return (int) (backgrounds.get(theme).getHeight()*0.75);
	}
}