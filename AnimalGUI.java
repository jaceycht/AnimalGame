package a3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Cheung Hiu Tung (uid: 3035189934)
 * The AnimalGUI class, used as the main program to implement a GUI interface for the simulated forest.
 */
public class AnimalGUI {
	/**
	 * An Animal array to store all the animals in the simulated forest.
	 */
	public static Animal[] myAnimals;
	/**
	 * A 2D String array to store the simulated forest.
	 */
	public static String[][] forestNow;
	/**
	 * A 2D String array to store the location (x,y) of each cell (white squares).
	 */
	public static String[][] cellLoc;
	/**
	 * A 2D Integer array to store the moving information of an animal.
	 */
	public static int[][] legalMove;
	/**
	 * A JLabel ArrayList to store the labels for all animals in the simulated forest.
	 */
	List<JLabel> labels = new ArrayList<JLabel>();
	/**
	 * A Boolean to store the mode (true for auto; false for manual). 
	 */
	Boolean auto=false;

	/**
	 * Create the animals and start the menu.
	 * @param args
	 */
	public static void main(String[] args) {
		myAnimals = new Animal[8];
		myAnimals[0] = new Cat();
		myAnimals[1] = new Dog();
		myAnimals[2] = new Fox();
		myAnimals[3] = new Hippo();
		myAnimals[4] = new Lion();
		myAnimals[5] = new Tiger();
		myAnimals[6] = new Turtle();
		myAnimals[7] = new Wolf();
		AnimalGUI animalGUI = new AnimalGUI();
		animalGUI.menu();
	}
	
	/**
	 *  Menu - Allow user to select animals and their image icons.
	 */
	void menu() {
		JFrame frame = new JFrame("Startup Menu"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panelA = new JPanel(new GridLayout(0, 1));
		JPanel panelB = new JPanel(new GridLayout(0, 1));
		JPanel panelC = new JPanel(new GridLayout(0, 1));
		List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
		List<JLabel> lbs = new ArrayList<JLabel>();
		List<JButton> buttons = new ArrayList<JButton>();
		
		for (int i=0; i<myAnimals.length; i++){
			JCheckBox checkbox = new JCheckBox(myAnimals[i].name);
			checkBoxes.add(checkbox);
			checkbox.setName(myAnimals[i].name+"CheckBox");
			if (myAnimals[i].selected == true) {
				checkbox.setSelected(true);	
			} else {
				checkbox.setSelected(false);	
			}
			ActionListener actionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AbstractButton abstractButton = (AbstractButton) e.getSource();
					boolean selected = abstractButton.getModel().isSelected();
					int index = checkBoxes.indexOf(checkbox);
					myAnimals[index].selected = selected;
				}
			};
			checkbox.addActionListener(actionListener);
			panelA.add(checkbox);
			
			ImageIcon icon = new ImageIcon(myAnimals[i].icon); 
			JLabel label = new JLabel();
			lbs.add(label);
			label.setIcon(icon);
			panelB.add(label);
			
			JButton button = new JButton("Pick an alternative icon");
			buttons.add(button);
			button.setName(myAnimals[i].name+"Button");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JFileChooser fileChooser = new JFileChooser();
					int returnValue = fileChooser.showOpenDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						int index = buttons.indexOf(button);
						myAnimals[index].icon = selectedFile.getPath();
						menu();
					}
				}
		    });
			panelC.add(button);
		}
	    
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				cellLoc = initializeCell();
				legalMove = initializeMove();
				forestNow = initializeForest();
				draw();
				frame.dispose();	
			}
		});

        frame.getContentPane().add(panelA, BorderLayout.WEST);
        frame.getContentPane().add(panelB, BorderLayout.CENTER);
        frame.getContentPane().add(panelC, BorderLayout.EAST);
		frame.getContentPane().add(startButton, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Initialize the location (x,y) of each cell (white squares).
	 * @return a 2D String array
	 */
	String[][] initializeCell() {
		String[][] ans = new String[15][15];
		int x=10, y=10;
		for (int i=0; i<15; i++) {
			for (int j=0; j<15; j++) {
				ans[i][j] = x+","+y;
				x=x+45;	
			}	
			x=10;
			y=y+45;
		}
		return ans;
	}
	
	/**
	 * Initialize moving information for an animal.
	 * @return a 2D Integer array
	 */
	int[][] initializeMove() {
		int[][] ans = new int[15][15];
		for (int i=0; i<15; i++) {
			for (int j=0; j<15; j++) {
				ans[i][j] = 0;
			}	
		}
		return ans;
	}
	
	/**
	 * Initialize a simulated forest.
	 * @return a 2D String array
	 */
	String[][] initializeForest() {
		String[][] ans = new String[15][15];
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				ans[i][j] = ".";
			}
		}
		for (int i=0;i<8;i++){
			if (myAnimals[i].selected==true) {
				Random ran = new Random();
				int row, column;
				do {
					row = ran.nextInt(15);
					column = ran.nextInt(15);
				} while (!ans[row][column].equals("."));
				myAnimals[i].x = row;
				myAnimals[i].y = column;
				ans[row][column] = myAnimals[i].label;
			}
		}
		System.out.println("-------- START --------");
		for(int i=0;i<8;i++){
			if (myAnimals[i].selected==true) {
				System.out.println(myAnimals[i].name + " initialized at " + myAnimals[i].x + "," + myAnimals[i].y);
			}
		}
		return ans;
	}
	
	/**
	 * Refresh the simulated forest.
	 * @return 2D String array
	 */
	String[][] drawForest(){
		String[][] ans = new String[15][15];
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				ans[i][j]= ".";
			}
		}
		for (int i=0;i<8;i++){
			if (myAnimals[i].selected==true) {
				int x = myAnimals[i].x;
				int y = myAnimals[i].y;
				ans[x][y] = myAnimals[i].label;
			}
		}
		return ans;
	}
	
	/**
	 * @author Cheung Hiu Tung (uid: 3035189934)
	 * The MyDrawPanel class, used to implement the GUI using Graphics.
	 */
	class MyDrawPanel extends JPanel{
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g) {
			g.fillRect(5,5,680,680);
			for (int i=0; i<15; i++) {
				for (int j=0; j<15; j++) {
					String s = cellLoc[i][j];
					String[] loc = s.split(",");
					int col = Integer.parseInt(loc[0]);
					int row = Integer.parseInt(loc[1]);
					if (legalMove[i][j]==0) g.setColor(Color.WHITE);
					else if (legalMove[i][j]==1) g.setColor(Color.ORANGE);	// orange: can move to
					else if (legalMove[i][j]==2) g.setColor(Color.BLUE);	// blue: moving to empty space
					g.fillRect(row,col,40,40);
				}	
			}
			for (Animal a : myAnimals) {
				if (a.selected==true) {
					if (a.alive==true) {									// print live animals
						Image image = new ImageIcon(a.icon).getImage();
						int col = a.x*40 + a.x*5 + 10;
						int row = a.y*40 + a.y*5 + 10;
						g.drawImage(image,row,col,40,40,this);
					} else {												// print dead animals with yellow background
						Image image = new ImageIcon(a.icon).getImage();
						int col = a.x*40 + a.x*5 + 10;
						int row = a.y*40 + a.y*5 + 10;
						g.setColor(Color.YELLOW);
						g.fillRect(row,col,40,40);
						g.drawImage(image,row,col,20,20,this);
					}
				}
			}
			for (int i=0; i<15; i++) {
				for (int j=0; j<15; j++) {
					String s = cellLoc[i][j];
					String[] loc = s.split(",");
					int col = Integer.parseInt(loc[0]);
					int row = Integer.parseInt(loc[1]);
					if (forestNow[j][i].charAt(0)>90) {					// if not dead
						if (legalMove[i][j]==3) {						// red: has another animal
							g.setColor(Color.RED);
							g.fillRect(row,col,40,40);
						} else if (legalMove[i][j]==4) {				// blue: moving to another animal's space
							g.setColor(Color.BLUE);	
							g.fillRect(row,col,40,40);
						}
					}
				}	
			}
		}
	}
	
	/**
	 * Update the moving information (for cell to be painted with blue).
	 * @param r: row
	 * @param c: column
	 */
	void moveToHere (int r, int c) {
		if (r>=0 && r<15 && c>=0 && c<15) {
			if (legalMove[r][c]==1) legalMove[r][c]=2;
			else if (legalMove[r][c]==3) legalMove[r][c]=4;
		}
	}
	
	/**
	 * Perform animal moving or animal attacking
	 * @param k: index of animal
	 * @param r: row
	 * @param c: column
	 */
	void moveORattack(int k, int r, int c) {
		if (legalMove[r][c]==2) {		// animal moving
			System.out.print(myAnimals[k].name + " moved from " + myAnimals[k].x + "," + myAnimals[k].y + " to ");
			myAnimals[k].x = c;
			myAnimals[k].y = r;
			System.out.println(myAnimals[k].x + "," + myAnimals[k].y);
		} else if (legalMove[r][c]==4) {	// animal attacking
			for (int d=0; d<8; d++) {
				if (myAnimals[d].label==forestNow[c][r]) {
					myAnimals[k].newX = c;
					myAnimals[k].newY = r;
					forestNow = drawForest();
					myAnimals[k].attack(myAnimals[d]);
					break;
				}
			}
		}
	}
	
	/**
	 * Reset the bounds for the labels of the animals (after moving).
	 */
	void resetBounds() {
		int i=0;
		for (JLabel jlb : labels) {
			if (myAnimals[i].alive==true && myAnimals[i].selected==true) {
				jlb.setVisible(true);
				int col = myAnimals[i].x*40 + myAnimals[i].x*5 + 10;
				int row = myAnimals[i].y*40 + myAnimals[i].y*5 + 10;
				jlb.setBounds(row, col, 40, 40);
			} else {
				jlb.setVisible(false);
			}
			i++;
		}
	}
	
	/**
	 * Start a new round immediately if only one survivor left.
	 * @return true if only one survivor left
	 */
	Boolean checkSurvivor() {
		int count=0;
		String res=null;
		for (Animal a : myAnimals) {
			if (a.selected==true) {
				if (a.alive==true) {
					count++;
					res = "The survivor is " + a.name + " at " + a.x + "," + a.y;
				}
			}
		}
		if (count==1) {
			System.out.println("-------- END --------");
			System.out.println(res + "\n");
			for (Animal a : myAnimals) {
				if (a.selected==true) {
					if (a.alive==false) {
						a.alive=true;
						a.label= a.label.toLowerCase();
					}
				}
			}
			legalMove = initializeMove();
			forestNow = initializeForest();
			resetBounds();	
			return true;
		} 
		else return false;
	}
		
	/**
	 * To draw the GUI for the simulated forest.
	 */
	void draw(){
		
		JFrame frame = new JFrame(); 		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyDrawPanel p = new MyDrawPanel();
		
		for (int i=0; i<8; i++) {
			JLabel label = new JLabel();
			labels.add(label);
			if (myAnimals[i].selected==true) {
				
				resetBounds();
				
				label.addMouseMotionListener(new MouseMotionListener() {
					
					@Override
					public void mouseDragged(MouseEvent e) {
						
						int k = labels.indexOf(label);
						myAnimals[k].checkMove();
						frame.getContentPane().repaint();
						
						int col = myAnimals[k].x;
						int row = myAnimals[k].y;
						int x = e.getX();
						int y = e.getY();
						if (x>0 && x<=40 && y>40 && y<=90) moveToHere (row, col+1);
						else if (x>0 && x<=40 && y>90 && y<=135) moveToHere (row, col+2);
						else if (x>40 && x<=90 && y>0 && y<=40) moveToHere (row+1, col); 
						else if (x>90 && x<=135 && y>0 && y<=40) moveToHere (row+2, col);
						else if (x>0 && x<=40 && y>-45 && y<=0) moveToHere (row, col-1);
						else if (x>0 && x<=40 && y>-90 && y<=-45) moveToHere (row, col-2);
						else if (x>-45 && x<=0 && y>0 && y<=40) moveToHere (row-1, col);
						else if (x>-90 && x<=-45 && y>0 && y<=40) moveToHere (row-2, col);
						else if (x>-45 && x<=0 && y>-45 && y<=0) moveToHere (row-1, col-1);
						else if (x>45 && x<=85 && y>-45 && y<=0) moveToHere (row+1, col-1);
						else if (x>-40 && x<=0 && y>40 && y<=90) moveToHere (row-1, col+1);
						else if (x>45 && x<=85 && y>45 && y<=85) moveToHere (row+1, col+1);
						frame.getContentPane().repaint();
					}
					
					@Override
					public void mouseMoved(MouseEvent e) {}
					
				});
				
				label.addMouseListener(new MouseListener(){
					
					@Override
					public void mouseClicked(MouseEvent e) {}
					
					@Override
					public void mousePressed(MouseEvent e) {}
					
					@Override
					public void mouseReleased(MouseEvent e) {
						
						int k = labels.indexOf(label);
						int col = myAnimals[k].x;
						int row = myAnimals[k].y;
						int x = e.getX();
						int y = e.getY();
						
						if (x>0 && x<=40 && y>40 && y<=90) moveORattack(k, row, col+1);
						else if (x>0 && x<=40 && y>90 && y<=135) {
							moveToHere (row, col+1);
							moveORattack(k, row, col+1);
							if (myAnimals[k].alive) moveORattack(k, row, col+2);
						}
						else if (x>40 && x<=90 && y>0 && y<=40) moveORattack(k, row+1, col);
						else if (x>90 && x<=135 && y>0 && y<=40) {
							moveToHere (row+1, col);
							moveORattack(k, row+1, col);
							if (myAnimals[k].alive) moveORattack(k, row+2, col);
						}
						else if (x>0 && x<=40 && y>-45 && y<=0) moveORattack(k, row, col-1);
						else if (x>0 && x<=40 && y>-90 && y<=-45) {
							moveToHere (row, col-1);
							moveORattack(k, row, col-1);
							if (myAnimals[k].alive) moveORattack(k, row, col-2);
						}
						else if (x>-45 && x<=0 && y>0 && y<=40) moveORattack(k, row-1, col);
						else if (x>-90 && x<=-45 && y>0 && y<=40) {
							moveToHere (row-1, col);
							moveORattack(k, row-1, col);
							if (myAnimals[k].alive) moveORattack(k, row-2, col);
						}
						else if (x>-45 && x<=0 && y>-45 && y<=0) moveORattack(k, row-1, col-1);
						else if (x>45 && x<=85 && y>-45 && y<=0) moveORattack(k, row+1, col-1);
						else if (x>-40 && x<=0 && y>40 && y<=90) moveORattack(k, row-1, col+1);
						else if (x>45 && x<=85 && y>45 && y<=85) moveORattack(k, row+1, col+1);
						
						resetBounds();
						forestNow = drawForest();
						legalMove = initializeMove();
						frame.getContentPane().repaint();

						checkSurvivor();

					}
					
					@Override
					public void mouseEntered(MouseEvent e) {}
					
					@Override
					public void mouseExited(MouseEvent e) {}
					
				});
				
			} else {
				label.setVisible(false);
			}
			frame.getContentPane().add(label);
		}		
			
		JButton button = new JButton("Auto Mode");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(){
				    public void run(){
						while (auto) {
							for(int i=0;i<8;i++){
								if (myAnimals[i].selected) {
									if (myAnimals[i].alive){
										myAnimals[i].moveTo();
										myAnimals[i].move(myAnimals[i].newX, myAnimals[i].newY);
										resetBounds();
										forestNow = drawForest();
										legalMove = initializeMove();
										frame.getContentPane().repaint();
										auto = !(checkSurvivor());
										try {
											Thread.sleep(50);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										if (auto==false) {
											button.setText("Auto Mode");
											break;
										}
									}
								}
							}
						}
				    }
				};
				if (auto==false) {
					auto=true;
					thread.start();
					button.setText("Manual Mode");
				} else {
					auto=false;
					button.setText("Auto Mode");
				}
			}
		});
		
		frame.getContentPane().add(p);
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.setSize(690,800);
		frame.setVisible(true);
	}
}
