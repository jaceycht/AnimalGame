COMP2396 Object-Oriented Programming and Java

Assignment 3 - A simple Java program which simulates the wild life in a forest. 

# Class

Animals are categorized into 4 groups: Canine, Feline, Hippo and Turtle. 
- Under Canine, we have Dog, Fox and Wolf.
- Under Feline, we have Cat, Lion and Tigger. 


# GUI interface

When the program starts, a setup menu should be shown which allows the user to select animals to be included in the current simulation run. User can select any animal using the setup menu. Each animal has a default image which will be shown in the “simulated” forest. On the other hand, user can select any image file for the selected animal in the current simulation run. 

The selected animals are then put into different cells of the 15x15 table randomly. To indicate which animal is in a particular cell, the cell is shown filled with an icon corresponding to that kind of animal.

After that, it creates a window on your screen, with a table of size 15 × 15 inside as the forest. Each cell can only hold one animal. If at some time, an animal A moves into the cell of another animal B, A will attack B before moving. 

# Animals Moving

Different animals move randomly in a different manner:
-	Feline moves in all eight directions, one step a time. 
-	Canine moves in four directions, one or two steps a time. 
-	Turtle has 50% chance stay in the same position, and 50% chance move in four directions, one step at a time.
-	All other animals move in four directions, one step a time. 

Note that the animals should not move out of the forest. When an animal moves from one location to another, your program should print the following information: Animal_type moved from ?, ? to ?, ?
For example:
> Fox moved from 2, 0 to 0, 0

> Hippo moved from 3, 0 to 4, 0

> ...

The movement of animals can be in one of the following 2 modes:
1. Auto mode: animals are moving in the simulated forest automatically in the following order: Cat, Dog, Fox, Hippo, Lion, Tiger, Turtle, Wolf. 
2. Manual mode: movements of animals are controlled manually by dragging the mouse. Specifically, if an animal A is currently at (i, j), and can move to (i + 1, j) as an option at one step, then the user press the mouse at cell (i, j) and drag it to the corresponding target cell. Note that the icon is not required to be with the cursor during dragging, but make sure it causes a move or attack when you release it at a proper position, and update the layout and icons in the forest.

Of course, nothing happens when you press on an empty cell. But when there is an animal A (holding the left button):
1. Cells that it can reach at one move will be painted with orange color;
2. If there are other animals under its attack range, their cells will be covered with red color.
3. If A is Turtle, it can always move because you drag it.

If you just release the mouse at its original cell or other cells it cannot reach, nothing happens. 
    
But if you move the mouse (holding the left button) to one of the optional cell, the color of that cell will be painted with blue as a real-time process, even if there is another animal. And if you just release the left button at an empty cell it can reach, the animal A simply moves to the new cell with the icon. 

User can switch between auto mode and manual mode through a button. After each round, text information should be outputted as above.

# Animals Attacking

When an animal moves to a location that is occupied by another animal, the moving animal will attack the occupying animal before moving. The result of an attack follows the following rules:
-	If a Feline attacks a Canine, Feline wins and Canine dies.
-	If a Canine attacks a Feline, there is a 50% chance that one wins and the other dies.
-	If a Lion attacks a Hippo, Lion wins and Hippo dies.
-	If a Fox attacks a Cat, Fox wins and Cat dies.
-	If any Animal attacks a Turtle, there is a 20% chance that the Animal wins and the Turtle dies.
-	If a Turtle attacks an Animal, there is a 50% chance that the Turtle wins and the Animal dies.
-	For all other cases, the attacker loses and dies.

When an animal attacks another animal, your program should print the following information: Attacker_type from ?, ? attacks occupant_type at ?, ? and wins/loses. The Loser dies at ?, ?

The location of the dead animal should be the location of the dead body. The cell contains a dead body should set yellow as the background color.

For example:
> Tiger from 2, 1 attacks Cat at 2, 2 and loses

> Tiger dies at 2, 1

> Lion from 4, 6 attacks Hippo at 3, 5 and wins

> Hippo dies at 2, 5

> Lion moved from 4,6 to 3, 5

Note that an animal will move if attack is successful. 

# New round

The program should be able to start a new round (i.e. 8 animals created again randomly) when there is only one survivor in the table. It can be triggered either immediately after the final attack or with a simple click.

When the program terminates, it will print out the survivor and its location. 

