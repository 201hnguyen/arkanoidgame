Arkanoid/Breakout Game Implentation
====

This project implements the game of Breakout/Arkanoid, and is completed as part of my
Fall 2019 Advanced Software Design & Implementation class at Duke.

Name: Ha Nguyen

### Timeline

Start Date: August 31, 2019

Finish Date: September 8, 2019

Hours Spent: 50-70 hours

### Resources Used
Images
* [Intro Scene Background](https://www.pexels.com/photo/black-and-white-clouds-dark-dark-clouds-557782/)
* [How To Play Scene Background](https://www.amazon.com/Laeacco-Background-Photography-Backdrops-Customized/dp/B07C3SCTXG)
* [Level 1 Background](https://www.pexels.com/photo/grayscale-photo-of-railway-633565/)
* [Level 2 Background](https://www.pexels.com/photo/gray-concrete-column-inside-vintage-building-157391/)
* [level 3 Background](https://www.pexels.com/photo/monochrome-photo-of-dark-hallway-2823465/)
* [Lose Background](https://www.newsweek.com/what-happens-your-brain-when-you-die-near-death-experiences-explained-1243305#slideshow/1243263)
* [Win Background](https://theultralinx.com/2019/06/wallpaper-of-the-week-402/)
* [Characters](http://clipart-library.com/harry-potter-clip-art.html)
* [Horn icon](https://thenounproject.com/search/?q=bullhorn&i=1273012)
* [Lightning icon](https://thenounproject.com/search/?q=lightning&i=54650)
* [Potion icon](https://thenounproject.com/search/?q=potion&i=886711)
* [Heart icon](https://www.pinclipart.com/pindetail/hRbbxR_anatomy-vector-human-heart-clipart-transparent-stock-realistic/)
* [Spike icon](https://thenounproject.com/search/?q=saw&i=2242779)

Human & technical resources:
* Benjamin Xu helped with debugging bricks removal and level transitions
* Professor Duvall helped with figuring out brick's directions
* Stack overflow 
    * [Switching scenes](https://stackoverflow.com/questions/37200845/how-to-switch-scenes-in-javafx)
    * [On global variables](https://stackoverflow.com/questions/484635/are-global-variables-bad)
    * [Blurring a window](https://stackoverflow.com/questions/37730725/javafx-blur-whole-window)
    * [Background image](https://stackoverflow.com/questions/9851200/setting-background-image-by-javafx-code-not-css/29327234)
    * [Reverting to previous git commit](https://stackoverflow.com/questions/4114095/how-do-i-revert-a-git-repository-to-a-previous-commit)
    * [Concurrent modification error](https://stackoverflow.com/questions/45223227/concurrentmodificationexception-javafx)


### Running the Program

Main class: GameMain

Data files needed: 
* background1.jpg
* background2.jpg
* background3.jpg
* backgroundintro.jpg
* backgroundlose.jpg
* backgroundwin.jpg
* howtoplay.jpg
* ball.png
* characterarmy.png
* characterdouble.png
* charactersingle.png
* door.png
* heart.png
* hornpower.png
* ligntninglaser.png
* lightningpower.png
* potionpower.png
* teeth.png
* level1.txt
* level2.txt
* level3.txt

Key/Mouse inputs:
* Mouse movement: move the paddle from side to side
* UP key: move the paddle upward
* DOWN key: move the paddle downward

Cheat keys:
* L: Increase lives remaining by 1
* R: Resets the ball to its original position and direction, 
which allows it to shoot straight up in a vertical path
* 1: Jump to level 1
* 2: Jump to level 2
* 0 & 3-9: Jump to level 3
* I: Jump to intro scene
* W: Jump to win scene
* D: Activates "Dumbledore's Army," which creates a paddle that
spans the width of the screen for which the ball cannot pass through
to get to the bottom of the screen.
* S: Return to a single character (useful for disabling Dumbledore's Army)
* C: Clears the bottom-most row
* B: Activates "Harry loses glasses mode"
* V: Activates "Harry finds glasses mode"

Known Bugs:
No known bugs at this time