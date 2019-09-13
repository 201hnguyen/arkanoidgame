game: design
====

Ha Nguyen

### Design Goals
The design goals of the project were to make it easy to add a few new levels and new power-ups. 
By making all level configurations almost the same, except for the brick configurations
and the dead zones, when one looks to add a new level, they simply have provide the desired brick
configuration for the new level, add it in as an enum, and add in the dead zones under the 
BackgroundStructure section. Additionally, new power-ups are easy to add because all of the functionalities
of power-ups are generalized. As such, one merely has to establish a new brick identity for the power-up in the
Bricks class (so that, when we read the configuration file, we know what to do with a number that is out of the
original 1-6) range. Then, one just has to define the functionality of the power-up in the Powerup class.


### High-level Design
**The GameMain class:** establishes the primary timeline, sets the initial stage, and manages the
game loop. 

**The GameScene class:** primarily responsible for running each of the scene in the game. This includes both levels
and non-levels (intro/win/lose/etc. scenes). The GameScene class is responsible for setting up the scenes, configuring
the bricks and collectively managing all the power-ups beneath these bricks, handling key inputs, and switching scenes
when a level is cleared or lost. 

**The GameStatus class:** works closely with both the GameMain and GameScene classes to determine the lives the player has 
throughout each level and reset the lives for new levels while keeping the score continuous across different levels. 
The Character class: responsible for the paddle itself; this includes setting the paddle itself up in the
Scene when it is called; it can change its size from single to double to spanning the entire screen, can move itself
according to the user input, and can interact with the ball to reverse its direction when the ball hits the paddle. 

**The Ball class:** responsible for setting itself up and resetting itself in the game Scene; in addition, 
it interacts with the Character, Brick, and BackgroundStructure class to check if it hits any of them in order to 
reflect itself. 

**The Brick class:** primarily responsible for keeping track of its own hits remaining as well as its power-ups; it downsizes
itself/decreases its remaining hits and release power-ups when destroyed. It primarily interacts with the GameScene to 
change/update the status of the game once the brick has downsized or destroyed itself. 

**The BackgroundStructure class:** handles all structures that are present in the game apart from the key elements that
are the bricks, paddle, and ball. Because these structures are not as central to the game, at the time of the original 
design, I made the class a bit more passive -- only able to know its qualities and set up itself, as they elements here
are not key elements in the game, but putting all the details of setting them up in the GameScene class would unnecessary
crowd the class. Now, knowing what I know about design, I would have made the class more active my moving the dead-zone 
handling to this class rather than keeping it in the GameScene class. 

**The Powerup class:** responsible for managing individual power-ups in the game. On an individual level, this means 
revealing itself, floating down the screen, and activating itself at the appropriate
moment. The powerup class interacts with the GameScene and GameStatus class in order to alter the status of the game when
it is activated (e.g., add an extra life). It also interacts with the Character class to determine when it has hit the 
paddle, which prompts activation. 

### Simplifying Assumptions/Decisions
My simplifying assumptions for the game were that, although new features can be added, the game's scale will remain that
of a student/school project rather than something massive. For example, though the game supports adding levels, this is
only supported for adding a few more levels rather than an entire map of levels(as some real games would have 50-100) levels
or even more. Similarly, with bricks, power-ups, and dead zones, the game can support adding a limited number of these,
but when the number becomes massive, then the design itself will need to be reworked in order to provide ease for adding
in these features. When it comes to the ball, however, new features concerning the ball should be fairly easy to add,
as unlike the levels and power-ups, they aren't built on enums.

### Adding New Features
I was able to implement all the features I wanted to by the deadline of the game. However, one feature that I thought 
about exploring had I had time was the idea of Power-downs, which can be activated either on certain bricks or by hitting
certain dead zones. For now, I will focus on how Power-downs could be implemented within bricks. First, I would need to
go to the levels' configuration document and create new brickId numbers that would represent a brick with power-downs.
Then, I would go the `Brick` class and add in this brick as one of the nums, specifying the appearance of the brick and
the points value it is worth (perhaps a negative value). In the constructor of the brick, I will need to add another 
case for establishing how many hits this brick would take. Likewise, in the `downsizeBrick` method, I would add another 
case for handling the power-down when the bricks are destroyed, similar to how I handled power-ups. Then, when it comes
to actually implementing the power-downs themselves, since the functionality of a power-down is essentially the same as 
a Power-up, I will add this as a new power-down in the `Powerup` class (and perhaps just refractor and rename the class 
to `Power`) only. To do this, I first create the png that represents the icon of the power-down. Then, in the
`activatePowerup` method, I just add new cases for my power-down, and create new private methods that actually implement
what happens to the game itself when that power-down is activated. That should be all, and nothing would have to change
in the `GameScene` class, which handles all the power-ups as a collective, as a result of this because the GameScene class
doesn't need to know about all the new changes internally with the new power-ups. 
