package a3;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Cheung Hiu Tung (uid: 3035189934)
 * The Animal class, used to set the attributes and behaviors of the animals.
 */
public class Animal{
	/**
	 * A String to store the name of the animal.
	 */
	protected String name;
	/**
	 * A String to store the label of the animal.
	 */
	protected String label;
	/**
	 * A String to store the file path for the icon of the animal.
	 */
	protected String icon;
	/**
	 * A Boolean to store if the animal is selected from the menu.
	 */
	protected boolean selected = true;
	/**
	 * Integers to store the location of animal. 
	 */
	protected int x, y;
	/**
	 * A Boolean to store if the animal is alive.
	 */
	protected boolean alive = true;
	/**
	 * Integers to store the new location of animal.
	 */
	protected int newX, newY;
	/**
	 * For creating a random number.
	 */
	protected static Random rand = new SecureRandom();
	
	/**
	 * To check and update moving information for the animal.
	 */
	void checkMove() {
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				if (((i==y)&&(j==x)) || ((i==y-1)&&(j==x)) || ((i==y)&&(j==x-1)) || ((i==y+1)&&(j==x)) || ((i==y)&&(j==x+1))){
					AnimalGUI.legalMove[i][j] = 1;
					if ((AnimalGUI.forestNow[j][i]!=".") && !((i==y)&&(j==x))) AnimalGUI.legalMove[i][j] = 3;
				}
				else AnimalGUI.legalMove[i][j] = 0;
			}
		}
		
	}
	/**
	 * To see where to move the animal; and update newX,newY.
	 */
	void moveTo(){
		do{
			rand = new Random();
			int ran = rand.nextInt(4);
			//left,right,up,down = [0,1,2,3]
			switch(ran){
				case 0:
					newX = x - 1;
					newY = y;
					break;
				case 1:
					newX = x + 1;
					newY = y;
					break;
				case 2:
					newX = x;
					newY = y - 1;
					break;
				case 3:
					newX = x;
					newY = y + 1;
					break;
				default:
					break;
			}
		}while (newX > 14 || newY < 0 || newX < 0 || newY > 14);
	}

	/**
	 * To see where to move the dead animal; and update newX,newY.
	 */
	void dieTo(){
		do{
			rand = new Random();
			int ran = rand.nextInt(8);
			//left,right,up,down = [0,1,2,3], topleft 4, topright 5, bottomleft 6, bottomright 7
			switch(ran){
				case 0:
					newX = x - 1;
					newY = y;
					break;
				case 1:
					newX = x + 1;
					newY = y;
					break;
				case 2:
					newX = x;
					newY = y - 1;
					break;
				case 3:
					newX = x;
					newY = y + 1;
					break;
				case 4:
					newX = x - 1;
					newY = y - 1;
					break;
				case 5:
					newX = x + 1;
					newY = y - 1;
					break;
				case 6:
					newX = x - 1;
					newY = y + 1;
					break;
				case 7:
					newX = x + 1;
					newY = y + 1;
					break;
				default:
					break;
			}
		}while (newX > 14 || newY < 0 || newX < 0 || newY > 14);
	}
	
	/**
	 * To move the animal to (newX,newY)
	 * @param newX
	 * @param newY
	 */
	void move(int newX, int newY){
		if (newX == this.x && newY == this.y){
			System.out.println(this.name + " moved from " + this.x + "," + this.y + " to " + this.x + "," + this.y);
				return;
		}
		if (AnimalGUI.forestNow[newX][newY].equals(".")){
			//just move
			AnimalGUI.forestNow[newX][newY] = this.label;
			AnimalGUI.forestNow[x][y] = ".";
			System.out.println(this.name + " moved from " + this.x + "," + this.y + " to " + newX + "," + newY);
			x = newX;
			y = newY;
		}else{
			//fight
			int id=-1;
			if (AnimalGUI.forestNow[newX][newY].charAt(0) < 90){
				// dead body
				System.out.println(this.name + " moved from " + this.x + "," + this.y + " to " + this.x + "," + this.y);
				return;
			}else{
				for (int i=0;i<8;i++){
					if (AnimalGUI.myAnimals[i].label.equals(AnimalGUI.forestNow[newX][newY])){
						// find the defender
						id = i;
					}
				}
				attack(AnimalGUI.myAnimals[id]);
			}
		}
	}
		
	/**
	 * To check if the animal can win the defender.
	 * @param defender
	 * @return true if win; false if lose.
	 */
	boolean canWin(Animal defender){
		
		// If a Fox attacks a Cat, Fox wins and Cat dies.
		if (this.label.equals("f") && defender.label.equals("c")){
			return true;
		}

		// If a Feline attacks a Canine, Feline wins and Canine dies.
		if (this.label.equals("c") || this.label.equals("t") || this.label.equals("l")){
			if (defender.label.equals("d") || defender.label.equals("f") || defender.label.equals("w"))
				return true;
		}

		// If a Canine attacks a Feline, there is a 50% chance that one wins and the other dies.
		if (this.label.equals("d") || this.label.equals("w") || this.label.equals("f")){
			if (defender.label.equals("c") || defender.label.equals("t") || defender.label.equals("l")){
				return (rand.nextInt(2)==0);
			}
		}

		// If a Lion attacks a Hippo, Lion wins and Hippo dies.
		if (this.label.equals("l") && defender.label.equals("h")){
			return true;
		}

		// If any Animal attacks a Turtle, there is a 20% chance that the Animal wins and the Turtle dies.
		if (defender.label.equals("u")){
			return (rand.nextInt(5)==0);
		}
		// If a Turtle attacks an Animal, there is a 50% chance that the Turtle wins and the Animal dies.
		if (this.label.equals("u")){
			return (rand.nextInt(2)==0);
		}

		// In general attacker will lose and die
		return false;
	}
	
	/**
	 * To attack the defender.
	 * @param d
	 */
	void attack(Animal d) {
		if (d.alive==true) {

			AnimalGUI.forestNow[x][y] = ".";
			System.out.print(name + " from " + x + "," + y + " attacks " + d.name + " at " + d.x + "," + d.y + " and ");
			if (this.canWin(d)){
				// win
				do{
					d.dieTo();
				}while(!AnimalGUI.forestNow[d.newX][d.newY].equals("."));
				d.alive = false;
				d.label = d.label.toUpperCase();
				AnimalGUI.forestNow[newX][newY] = label;
				AnimalGUI.forestNow[d.newX][d.newY] = d.label;
				d.x = d.newX;
				d.y = d.newY;
				System.out.print("wins.\n");
				System.out.println(d.name + " dies at " + d.x + "," + d.y);
				System.out.println(name + " moved from " + x + "," + y + " to " + newX + "," + newY);
			}else{
				// lose
				System.out.print("loses.\n");
				x = newX;
				y = newY;
				do{
					this.dieTo();
				}while(!AnimalGUI.forestNow[this.newX][this.newY].equals("."));
				alive = false;
				label = label.toUpperCase();
				AnimalGUI.forestNow[newX][newY] = label;	
				System.out.println(name + " dies at " + newX + "," + newY);
			}
			x = newX;
			y = newY;
		}
	}	
}


/**
 * @author Cheung Hiu Tung
 * The Canine class, used to set the attributes and behaviors of Canine.
 */
class Canine extends Animal{
	/**
	 * To set name for the Canine
	 */
	Canine(){
		name = "Canine";
	}
	/* (non-Javadoc)
	 * @see a3.Animal#checkMove()
	 */
	void checkMove() {
		//check first step
		super.checkMove();
		//check second step
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				if ((i==y-2)&&(j==x)) {
					if ((AnimalGUI.legalMove[y-1][x]==1) || (AnimalGUI.forestNow[x][y-1].charAt(0)>90)) {
						AnimalGUI.legalMove[i][j]=1; 
						if ((AnimalGUI.forestNow[j][i]!=".") && !((i==y)&&(j==x))) AnimalGUI.legalMove[i][j] = 3;
					}
				} else if ((i==y)&&(j==x-2)) {
					if ((AnimalGUI.legalMove[y][x-1]==1) || (AnimalGUI.forestNow[x-1][y].charAt(0)>90)) {
						AnimalGUI.legalMove[i][j]=1; 
						if ((AnimalGUI.forestNow[j][i]!=".") && !((i==y)&&(j==x))) AnimalGUI.legalMove[i][j] = 3;

					}
				} else if ((i==y+2)&&(j==x)) {
					if ((AnimalGUI.legalMove[y+1][x]==1) || (AnimalGUI.forestNow[x][y+1].charAt(0)>90)) {
						AnimalGUI.legalMove[i][j]=1; 
						if ((AnimalGUI.forestNow[j][i]!=".") && !((i==y)&&(j==x))) AnimalGUI.legalMove[i][j] = 3;
					}
				} else if ((i==y)&&(j==x+2)) {
					if ((AnimalGUI.legalMove[y][x+1]==1) || (AnimalGUI.forestNow[x+1][y].charAt(0)>90)) {
						AnimalGUI.legalMove[i][j]=1; 
						if ((AnimalGUI.forestNow[j][i]!=".") && !((i==y)&&(j==x))) AnimalGUI.legalMove[i][j] = 3;
					}
				}				
			}
		}
	}
	/* (non-Javadoc)
	 * @see a3.Animal#moveTo()
	 */
	void moveTo(){
		super.moveTo();
	}
	/* (non-Javadoc)
	 * @see a3.Animal#move(int, int)
	 */
	void move(int newX, int newY){
		int oldX = x, oldY = y;
		if (rand.nextInt(2) == 0){
			// move 1 step
			super.move(this.newX,this.newY);
		}else{
			// move 2 step
			if (!AnimalGUI.forestNow[newX][newY].equals(".")){
				super.move(this.newX,this.newY);
				if (this.alive){
					this.newX = 2 * this.x - oldX;
					this.newY = 2 * this.y - oldY;
					if (this.newX > 14 || this.newY > 14 || this.newX < 0 || this.newY < 0) return;
					super.move(this.newX,this.newY);
				}
			}else{
				this.newX = 2 * this.newX - oldX;
				this.newY = 2 * this.newY - oldY;
				if (this.newX > 14 || this.newY > 14 || this.newX < 0 || this.newY < 0) return;
				super.move(this.newX,this.newY);
			}
			
		}

	}
}
/**
 * @author Cheung Hiu TUng
 * The Feline class, used to set the attributes and behaviors of Feline.
 */
class Feline extends Animal{
	/**
	 * Set the name of Feline.
	 */
	Feline(){
		name = "Feline";
	}
	/* (non-Javadoc)
	 * @see a3.Animal#checkMove()
	 */
	void checkMove() {
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				if (((i==y)&&(j==x)) || ((i==y-1)&&(j==x)) || ((i==y)&&(j==x-1)) || ((i==y+1)&&(j==x)) || ((i==y)&&(j==x+1)) || ((i==y-1)&&(j==x-1)) || ((i==y+1)&&(j==x+1)) || ((i==y+1)&&(j==x-1)) || ((i==y-1)&&(j==x+1)) ) {
					AnimalGUI.legalMove[i][j] = 1;					
					if ((AnimalGUI.forestNow[j][i]!=".") && !((i==y)&&(j==x))) AnimalGUI.legalMove[i][j] = 3;
				} 
				else AnimalGUI.legalMove[i][j] = 0;
			}
		}
	}
	/* (non-Javadoc)
	 * @see a3.Animal#moveTo()
	 */
	void moveTo(){
		super.dieTo();
	}
}
/**
 * @author Cheung Hiu Tung
 * The Hippo class, used to set the attributes and behaviors of Hippo.
 */
class Hippo extends Animal{
	/**
	 * To set name, label and icon for Hippo.
	 */
	Hippo(){
		name = "Hippo";
		label = "h";
		icon = "Icons/Hippo.png";
	}
}
/**
 * @author Cheung Hiu Tung
 * The Turtle class, used to set the attributes and behaviors of Turtle.
 */
class Turtle extends Animal{
	/**
	 * To set name, label and icon for Turtle.
	 */
	Turtle(){
		name = "Turtle";
		label = "u";
		icon = "Icons/Turtle.png";
	}
	/* (non-Javadoc)
	 * @see a3.Animal#moveTo()
	 */
	void moveTo(){
		super.moveTo();
		if (rand.nextInt(2) == 1){
			newX = x;
			newY = y;
		}
	}
}
/**
 * @author Cheung Hiu Tung
 * The Dog class, used to set the attributes and behaviors of Dog.
 */
class Dog extends Canine{
	/**
	 * To set name, label and icon for Dog.
	 */
	Dog(){
		name = "Dog";
		label = "d";
		icon = "Icons/Dog.png";
	}
}
/**
 * @author Cheung Hiu Tung
 * The Fox class, used to set the attributes and behaviors of Fox.
 */
class Fox extends Canine{
	/**
	 * To set name, label and icon for fox.
	 */
	Fox(){
		name = "Fox";
		label = "f";
		icon = "Icons/Fox.png";
	}
}
/**
 * @author Cheung Hiu Tung
 * The Wolf class, used to set the attributes and behaviors of Wolf.
 */
class Wolf extends Canine{
	/**
	 * To set name, label and icon for Wolf.
	 */
	Wolf(){
		name = "Wolf";
		label = "w";
		icon = "Icons/Wolf.png";
	}
}
/**
 * @author Cheung Hiu Tung
 * The Cat class, used to set the attributes and behaviors of Cat.
 */
class Cat extends Feline{
	/**
	 * To set name, label and icon for Cat.
	 */
	Cat(){
		name = "Cat";
		label = "c";
		icon = "Icons/Cat.png";
	}
}
/**
 * @author Cheung Hiu Tung
 * The Lion class, used to set the attributes and behaviors of Lion.
 */
class Lion extends Feline{
	/**
	 * To set name, label and icon for Lion.
	 */
	Lion(){
		name = "Lion";
		label = "l";
		icon = "Icons/Lion.png";
	}
}
/**
 * @author Cheung Hiu Tung
 * The Tiger class, used to set the attributes and behaviors of Tiger.
 */
class Tiger extends Feline{
	/**
	 * To set name, label and icon for Tiger.
	 */
	Tiger(){
		name = "Tiger";
		label = "t";
		icon = "Icons/Tiger.png";
	}
}
