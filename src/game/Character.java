/*
Code Masterpiece comments: the purpose of this class is to act as the paddle in the game; this includes implementing the
functionality related to the paddle's movements, ball collision, appearance, etc. This class is one of the better-designed
classes from the beginning, and have been slightly improved upon by refractoring for various reasons:
1) Global constants and instance variables, as well as method names, are clear and easy to understand. All numbers used
more than once are encapsulated in a global constant, resulting in no "magic numbers" throughout the code.
2) There are few duplication in the class
3) Comments that mask code smells are avoided by using private helper methods throughout the class. For example,
in the constructor, `setCurrentCharacterAsPaddle` is an example of using a method as a way to replace commenting,
which makes the code messy and hard to maintain for the future.
4) The class contributes meaningfully to the project without trying to do too much. It holds the information of all the
possible ImageViews it can have; it can control its own motion and blur itself upon cheat keys or dead zones activation;
it can check its position relative to the ball in order to allow for a bounce off; it can change its ImageView when a
power-up or cheat key is pressed. All of the methods in this class, thus, deal primarily with objects of the class itself;
these methods do not overreach into other classes/objects and attempt to do too much in terms of modifying those objects.
5) Each method is short and concise, the principle of shy code is also embraced. For example, look at any of the parameters
in each of these method, there are no more parameters than necessary; the code is not dragging in other pieces of code
too much in order to do its work.
6) The principle of DRY code is embraced; for example, createCharacter is refractored into a method in order to keep
all the knowledge of creating the character itself in one place, and at a high-level abstraction, the constructor does
not need to know all the details about how the ImageView is set up.
7) Previously, the reflectBall method was in this class; however, this was refractored and extracted to belong in the
Ball class, as it had too many elements that relate to the Ball and therefore should be long in that class. In addition,
that method also had the power to directly change the position of the ball; because of that, allowing the Ball class to have
full control of that class rather than taking that power and putting it in this class is a better design. However, the
checking of the collision with the ball is moved to this class because this is something that the paddle can be
responsible for.
8) There are no static or public variables that are open to modification by other classes. All variables in this class
are kept as private as possible unless modification is absolutely essential (e.g., another class can turn myBlurOn and
off, but cannot modify any of the ImageViews besides just changing them out as the current).
 */
package game;

import javafx.geometry.Bounds;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Character {
    public static final int CHARACTER_HEIGHT = 150;
    public static final int SINGLE_CHARACTER_WIDTH = 200;
    public static final int DOUBLE_CHARACTER_WIDTH = 350;
    public static final int THIRD_DIVISION = 50;
    public static final int BLUR_RADIUS = 100;
    public static final String SINGLE_CHARACTER_IMAGE_PATH = "charactersingle.png";
    public static final String DOUBLE_CHARACTER_IMAGE_PATH = "characterdouble.png";
    public static final String ARMY_CHARACTER_IMAGE_PATH = "characterarmy.png";

    private ImageView myCurrentCharacterImageView;
    private ImageView mySingleCharacterImageView;
    private ImageView myDoubleCharacterImageView;
    private ImageView myDumbledoresArmyImageView;
    private boolean myBlurOn;

    /**
     * Used as the paddle in the game to stop the ball from falling off the bottom edge of the screen. Assumes that
     * "charactersingle.png," "characterdouble.png," and "characterarmy.png" exists in the resource folder. Upon creation,
     * the single character ImageView, double character ImageView, and army ImageView are all created to allow for
     * a smoother transition when the characters are switched upon power-up or cheat key activation. The paddle's abilities
     * include move in the Y direction as well as the standard X direction, switch out characters, and become blurry
     * when a dead zone is hit. Visibly, within the game, the paddle can also bounce the ball differently depending on
     * where it is hit and can shoot lightning on a power-up activation, but the method for shooting lightning is placed within
     * the power-up Class and the method for reflecting the Ball is placed in the Ball class for internal design consistency purpose.
     *
     */
    public Character() {
        mySingleCharacterImageView = createCharacter(SINGLE_CHARACTER_IMAGE_PATH, SINGLE_CHARACTER_WIDTH);
        myDoubleCharacterImageView = createCharacter(DOUBLE_CHARACTER_IMAGE_PATH, DOUBLE_CHARACTER_WIDTH);
        myDumbledoresArmyImageView = createCharacter(ARMY_CHARACTER_IMAGE_PATH, GameMain.SCENE_WIDTH);

        myBlurOn = false;
        myCurrentCharacterImageView = mySingleCharacterImageView;
        setCurrentCharacterAsPaddle();
    }

    /**
     * One of the paddle's abilities, which allows it to change Y position if the player presses the UP or DOWN key.
     * If the paddle hits the bottom of the screen, the DOWN button will be disabled.
     * @param value the value for which the paddle will move in the Y direction; can be positive or negative for up or down.
     */
    public void changeYPosition(int value) {
        myCurrentCharacterImageView.setY(myCurrentCharacterImageView.getY() - value);
        double maximumYForScene = GameMain.SCENE_HEIGHT - myCurrentCharacterImageView.getFitHeight();
        if (myCurrentCharacterImageView.getY() >= maximumYForScene) {
            myCurrentCharacterImageView.setY(maximumYForScene);
        }
    }

    /**
     * Allows for switching from single to double character or to army and back. This is activated when the horn powerup
     * is used or when the 'D' cheat key is pressed.
     * @param desiredCharacterImageView the character that the current character should change into based on the powerup or cheat key
     * @param root the root of the current GameScene, used to add in the new ImageView of the character.
     */
    public void changeCharacter(ImageView desiredCharacterImageView, Pane root) {
        double currentXCoordinate = myCurrentCharacterImageView.getX();
        double currentYCoordinate = myCurrentCharacterImageView.getY();
        root.getChildren().remove(myCurrentCharacterImageView);

        myCurrentCharacterImageView = desiredCharacterImageView;
        if (myCurrentCharacterImageView == myDumbledoresArmyImageView) {
            myCurrentCharacterImageView.setX(0);
        } else {
            myCurrentCharacterImageView.setX(currentXCoordinate);
        }
        myCurrentCharacterImageView.setY(currentYCoordinate);
        if (myBlurOn) {
            blurCharacter(BLUR_RADIUS);
        }
        root.getChildren().add(myCurrentCharacterImageView);
    }

    /**
     * Moves the paddle side to side according to the position of the mouse. Used for controlling the paddle.
     * @param x the x position of the mouse event
     */
    public void movePaddleOnMouseInput(double x) {
        myCurrentCharacterImageView.setX(x);
        if (myCurrentCharacterImageView.getBoundsInLocal().getMaxX() >= GameMain.SCENE_WIDTH) {
            myCurrentCharacterImageView.setX(GameMain.SCENE_WIDTH - myCurrentCharacterImageView.getFitWidth());
        }
    }

    /**
     * One of the character/paddle's abilities, which blurs/turns almost invisible when a dead zone is hit.
     * In order to undo the blur, the player must hit the dead zone again
     */
    public void setBlur() {
        if (!myBlurOn) {
            myBlurOn = true;
            blurCharacter(BLUR_RADIUS);
        } else {
            myBlurOn = false;
            blurCharacter(0);
        }
    }

    /**
     * Checks the collision between the paddle and the ball
     * @param ballBounds the bounds of the ball that will be checked against the paddle
     */
    public boolean detectBallCollision (Bounds ballBounds) {
        if (ballBounds.intersects(myCurrentCharacterImageView.getBoundsInParent()) &&
                ballBounds.getMaxY() <= myCurrentCharacterImageView.getBoundsInParent().getMinY()) {
            return true;
        }
        return false;
    }

    /**
     * Gets the current ImageView representation of the Character, useful for adding to the root of the gameScene.
     * @return the current ImageView of the character to be visible.
     */
    public ImageView getCharacterImageView() {
        return myCurrentCharacterImageView;
    }

    /**
     * Gets the ImageView representation of the character when it is just a single character. This is useful as a parameter
     * for the desired character when changing character.
     * @return the ImageView of the character when it is in single mode.
     */
    public ImageView getSingleCharacterImageView() {
        return mySingleCharacterImageView;
    }

    /**
     * Gets the ImageView representation of the character when it is a double character. This is useful as a parameter
     * for the desired character when changing character.
     * @return the ImageView of the character when it is in double mode.
     */
    public ImageView getDoubleCharacterImageView() {
        return myDoubleCharacterImageView;
    }

    /**
     * Gets the ImageView representation of the character when it is just an army. This is useful as a parameter
     * for the desired character when changing character.
     * @return the ImageView of the character when it is in army mode.
     */
    public ImageView getDumbledoresArmyImageView() {
        return myDumbledoresArmyImageView;
    }

    private ImageView createCharacter(String characterImagePath, int characterWidth) {
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(characterImagePath));
        ImageView characterImageView = new ImageView(image);
        characterImageView.setFitWidth(characterWidth);
        characterImageView.setFitHeight(CHARACTER_HEIGHT);
        return characterImageView;
    }

    private void setCurrentCharacterAsPaddle() {
        myCurrentCharacterImageView.setX(GameMain.SCENE_WIDTH / 2 - SINGLE_CHARACTER_WIDTH / 2);
        myCurrentCharacterImageView.setY(GameMain.SCENE_HEIGHT - CHARACTER_HEIGHT);
    }

    private void blurCharacter(int radius) {
        GaussianBlur blur = new GaussianBlur(radius);
        myCurrentCharacterImageView.setEffect(blur);
    }
}
