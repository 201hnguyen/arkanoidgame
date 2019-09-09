package game;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Character {
    public static final int CHARACTER_HEIGHT = 150;
    public static final int SINGLE_CHARACTER_WIDTH = 200;
    public static final int DOUBLE_CHARACTER_WIDTH = 350;
    public static final int THIRD_DIVISION = 50;
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
     * a smoother transition when the characters are switched upon powerup or cheat key activation. The paddle's abilities
     * include bouncing the ball differently depending on where it is hit, move in the Y direction as well as the standard X
     * direction, switch out characters, and become blurry when a dead zone is hit. Visibly, within the game, the paddle
     * can also shoot lightning upon a powerup, but the method for shooting lightning is placed within the powerup Class
     * for consistency purpose.
     */
    public Character() {
        mySingleCharacterImageView = createCharacter(SINGLE_CHARACTER_IMAGE_PATH, SINGLE_CHARACTER_WIDTH);

        myDoubleCharacterImageView = createCharacter(DOUBLE_CHARACTER_IMAGE_PATH, DOUBLE_CHARACTER_WIDTH);

        myDumbledoresArmyImageView = createCharacter(ARMY_CHARACTER_IMAGE_PATH, GameMain.SCENE_WIDTH);

        myBlurOn = false;
        myCurrentCharacterImageView = mySingleCharacterImageView;
        setCurrentCharacterAsPaddle();
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

    /**
     * One of the paddle's abilities, which allows it to change Y position if the player presses the UP or DOWN key.
     * If the paddle hits the bottom of the screen, the DOWN button will no longer work.
     * @param value the value for which the paddle will move in the Y direction; can be positive or negative for up or down.
     */
    public void changeYPosition(int value) {
        myCurrentCharacterImageView.setY(myCurrentCharacterImageView.getY() - value);
        if (myCurrentCharacterImageView.getY() >= GameMain.SCENE_HEIGHT - myCurrentCharacterImageView.getFitHeight()) {
            myCurrentCharacterImageView.setY(GameMain.SCENE_HEIGHT - myCurrentCharacterImageView.getFitHeight());
        }
    }

    /**
     * Allows for switching from single to double character or to army and back. This is activated when the horn powerup
     * is used or when the 'D' cheat key is pressed.
     * @param desiredCharacterImageView the character that the current character should change into based on the powerup or cheat key
     * @param root the root of the current GameScene, used to add in the new ImageView of the character.
     */
    public void changeCharacter(ImageView desiredCharacterImageView, Pane root) {
        double x = myCurrentCharacterImageView.getX();
        double y = myCurrentCharacterImageView.getY();
        root.getChildren().remove(myCurrentCharacterImageView);

        myCurrentCharacterImageView = desiredCharacterImageView;
        if (myCurrentCharacterImageView == myDumbledoresArmyImageView) {
            myCurrentCharacterImageView.setX(0);
        } else {
            myCurrentCharacterImageView.setX(x);
        }
        myCurrentCharacterImageView.setY(y);
        if (myBlurOn) {
            blurCharacter();
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
            blurCharacter();
        } else {
            myBlurOn = false;
            unblurCharacter();
        }
    }

    private void blurCharacter() {
        GaussianBlur blur = new GaussianBlur(100);
        myCurrentCharacterImageView.setEffect(blur);
    }

    private void unblurCharacter() {
        GaussianBlur unblur = new GaussianBlur(0);
        myCurrentCharacterImageView.setEffect(unblur);
    }

    /**
     * One of the paddle's abilities, which reflects the ball based on the position it is hit at. If the ball is hit
     * in the middle, it will bounce off in the opposite direction; if it hits to the left, it will bounce to the left;
     * if it hits to the right, it will bounce to the right.
     * @param ball the ball whose direction will be reflected by the paddle.
     */
    public void reflectBall(Ball ball) {
        if (ball.getDirection()[0] == 0) {
            ball.getDirection()[0] = -1;
        }
        if (ball.getBallImageView().getBoundsInParent().getCenterX() <= myCurrentCharacterImageView.getBoundsInParent().getCenterX() - THIRD_DIVISION) {
            ball.getDirection()[0] = -1;
            ball.getDirection()[1] = -1;
        } else if (ball.getBallImageView().getBoundsInParent().getCenterX() >= myCurrentCharacterImageView.getBoundsInParent().getCenterX() + THIRD_DIVISION) {
            ball.getDirection()[0] = 1;
            ball.getDirection()[1] = -1;
        } else {
            ball.getDirection()[1] *= -1;
        }
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
}
