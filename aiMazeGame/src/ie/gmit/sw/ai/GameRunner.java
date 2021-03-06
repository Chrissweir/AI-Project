package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ie.gmit.sw.ai.Nodes.Node;
import ie.gmit.sw.ai.Sprites.FuzzySprite;
import ie.gmit.sw.ai.Sprites.ItemSprite;
import ie.gmit.sw.ai.Sprites.NeuralSprite;
import ie.gmit.sw.ai.Sprites.Sprite;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 15;
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;
	private FuzzySprite sprite;
	
	//Labels
	public JLabel weaponLabel;
	public JLabel healthLabel;
	public JLabel bombLabel ;
	
	public GameRunner() throws Exception{
		
		
		model = new Maze(MAZE_DIMENSION);
    	view = new GameView(model);
    	
    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	
    	//placePlayer();
    	updateView();
    	
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	
    	JPanel panel = new JPanel();
    	weaponLabel = new JLabel("Weapon: None");
    	healthLabel = new JLabel("Health: " + 100);
    	panel.add(weaponLabel);
    	panel.add(healthLabel);

    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        FlowLayout layout = new FlowLayout();
        f.getContentPane().setLayout(layout);
        view.add(panel);
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}
	
	private void updateView(){
		view.setPlayer(model.getPlayer());
		currentRow = model.getPlayer().getRow();
    	currentCol = model.getPlayer().getCol();
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e) {
    	updatePlayerStats();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++;   		
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;	
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;        	  	
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        }else{
        	return;
        }
        updateView();       
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore
	
	public void updatePlayerStats(){
		healthLabel.setText("Health: " + (int)model.getPlayer().getHealth());
		if(model.getPlayer().isSword()){
			weaponLabel.setText("Weapon: Sword");
		}
		else if(model.getPlayer().isBomb()){
			weaponLabel.setText("Weapon: Bomb");
		}
		else if(model.getPlayer().isHbomb()){
			weaponLabel.setText("Weapon: H-Bomb");
		}
		else{
			weaponLabel.setText("Weapon: None");
		}
	}
    
	private boolean isValidMove(int row, int col){
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getNodeType() == -1){
			model.set(currentRow, currentCol, model.get(row, col));
			model.set(row, col, model.getPlayer());
			if(model.getPlayer().getHealth() ==0){
				System.out.println("GAMEOVER");
				JFrame winning = new JFrame("Lost !!");
				winning.setLayout(new GridLayout(1, 1));
				winning.setSize(400, 300);
				JPanel panel = new JPanel(new FlowLayout());
				JLabel label = new JLabel("Sorry you lost !");
				panel.add(label);
				winning.add(panel);
				winning.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				winning.setVisible(true);
			}
			return true;
		}else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getNodeType() == -1|| model.get(row, col).getNodeType() == 1){
			model.get(row, col).setNodeType(0);
			model.getPlayer().setSword(true);
			model.getPlayer().setBomb(false);
			model.getPlayer().setHbomb(false);
			updatePlayerStats();
			return false;
		}
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getNodeType() == -1|| model.get(row, col).getNodeType() == 2){
			model.get(row, col).setNodeType(0);
			model.getPlayer().setHelp(true);
			updatePlayerStats();
			return false;
		}
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getNodeType() == -1|| model.get(row, col).getNodeType() == 3){
			model.get(row, col).setNodeType(0);
			model.getPlayer().setSword(false);
			model.getPlayer().setBomb(true);
			model.getPlayer().setHbomb(false);
			updatePlayerStats();
			return false;
		}
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getNodeType() == -1|| model.get(row, col).getNodeType() == 4){
			model.get(row, col).setNodeType(0);
			model.getPlayer().setSword(false);
			model.getPlayer().setBomb(false);
			model.getPlayer().setHbomb(true);
			updatePlayerStats();
			return false;
			}
			else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getNodeType() == -1|| model.get(row, col).getNodeType() >5 && model.get(row, col).getNodeType() <14){
			//sprite = model.getSpriteId(row, col);
			//sprite.engage();
			//sprite.setId(-1);
			updatePlayerStats();
			if(model.getPlayer().getHealth() > 1){
				model.set(row, col, new Node(row,col,-1));
			}else{
				System.out.println("GAMEOVER");
				JFrame winning = new JFrame("Lost !!");
				winning.setLayout(new GridLayout(1, 1));
				winning.setSize(400, 300);
				JPanel panel = new JPanel(new FlowLayout());
				JLabel label = new JLabel("Sorry you lost !");
				panel.add(label);
				winning.add(panel);
				winning.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				winning.setVisible(true);
			}
			return false;
		}
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getNodeType() == -1|| model.get(row, col).getNodeType() == 14){
			JFrame winning = new JFrame("Winning !!");
			winning.setLayout(new GridLayout(1, 1));
			winning.setSize(400, 300);
			JPanel panel = new JPanel(new FlowLayout());
			JLabel label = new JLabel("Congratulations you won !");
			panel.add(label);
			winning.add(panel);
			winning.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			winning.setVisible(true);
			
			System.out.println("Congratulations you Won!!!");
			//System.exit(0);
		}else{
			return false; //Can't move
		}
		return false;
	}
	
	private Sprite[] getSprites() throws Exception{
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images... 
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new ItemSprite("Hedge", "resources/hedge.png");
		sprites[1] = new ItemSprite("Sword", "resources/sword.png");
		sprites[2] = new ItemSprite("Help", "resources/help.png");
		sprites[3] = new ItemSprite("Bomb", "resources/bomb.png");
		sprites[4] = new ItemSprite("Hydrogen Bomb", "resources/h_bomb.png");
		sprites[5] = new ItemSprite("Spartan Warrior", "resources/spartan_1.png", "resources/spartan_2.png");
		sprites[6] = new FuzzySprite("Black Spider", "resources/black_spider_1.png", "resources/black_spider_2.png");
		sprites[7] = new FuzzySprite("Blue Spider", "resources/blue_spider_1.png", "resources/blue_spider_2.png");
		sprites[8] = new FuzzySprite("Brown Spider", "resources/brown_spider_1.png", "resources/brown_spider_2.png");
		sprites[9] = new FuzzySprite("Green Spider", "resources/green_spider_1.png", "resources/green_spider_2.png");
		sprites[10] = new NeuralSprite("Grey Spider", "resources/grey_spider_1.png", "resources/grey_spider_2.png");
		sprites[11] = new NeuralSprite("Orange Spider", "resources/orange_spider_1.png", "resources/orange_spider_2.png");
		sprites[12] = new NeuralSprite("Red Spider", "resources/red_spider_1.png", "resources/red_spider_2.png");
		sprites[13] = new NeuralSprite("Yellow Spider", "resources/yellow_spider_1.png", "resources/yellow_spider_2.png");
		sprites[14] = new ItemSprite("Door", "resources/door.png");
		return sprites;
	}
	public static void main(String[] args) throws Exception{
		GameRunner view = new GameRunner();
	}
	
}