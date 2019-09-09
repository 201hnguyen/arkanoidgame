package game;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

public class Powerup {
    public static final int ICON_SIZE = 50;
    public static final int POWERUP_SPEED = 100;

    private ImageView myPowerupImageView;
    private PowerupType myPowerupType;
    private boolean myPowerupIsActivated;

    /**
     * Used to create and manage powerups that become part of the game when certain bricks that contain
     * them are destroyed. Assumes that the images for each powerup's icon, "lightningpower.png", "potionpower.png", and
     * "hornpower.png" exist in the resources folder; thus, these files cannot be removed. For example,
     * this class can be used to set up a power up, set its motion as it floats down the screen, and activate the power
     * up. There are 3 powerups available: the lightning power will come out of the paddle and destroy everything it intersects;
     * the potion power that will add a life to the current level; and the horn power that will allow the character to
     * "Call a friend" and thereby extend the length of the paddle. Two of the paddle's abilities are
     * in use here, which are the ability to shoot lightning and the ability to extend itself.
     * For the ability to shoot lightning, though the method uses both the Character object and the Powerup object,
     * the decision was made to include this ability here to keep all the powerups consistent and
     * nicely consolidated. For the ability to extend itself and create a bigger paddle, the implementation of this ability
     * is split between this object and the Character object (the power is activate here and a call to change characters is
     * issued; however, the Character object is the one doing the actual changing).
     * @param type
     */
    public Powerup(PowerupType type) {
        myPowerupIsActivated = false;
        myPowerupType = type;

        Image powerupImage = new Image(this.getClass().getClassLoader().getResourceAsStream(type.getPowerupFileName()));
        myPowerupImageView = new ImageView(powerupImage);
        myPowerupImageView.setFitWidth(ICON_SIZE);
        myPowerupImageView.setFitHeight(ICON_SIZE);
    }

    /**
     * When a brick with a power up is destroyed, this method is called to
     * initially set up that power up by revealing it on the screen and adding
     * it to the GameScene's ArrayList of current powerups that are present.
     * @param brickWithPowerup the brick where the power up is; used for setting the
     *                         coordinates of where the powerup should appear.
     * @param gameScene the GameScene whose ArrayList of present powerups this new
     *                  powerup should be added to in order to keep track of it.
     */
    public void setPowerup(Brick brickWithPowerup, GameScene gameScene) {
        revealPowerup(brickWithPowerup, gameScene.getRoot());
        gameScene.addToPresentPowerups(this);
    }

    private void revealPowerup(Brick brick, Pane root) {
        myPowerupImageView.setX(brick.getBrickImageView().getBoundsInParent().getCenterX() - ICON_SIZE / 2);
        myPowerupImageView.setY(brick.getBrickImageView().getBoundsInParent().getCenterY() - ICON_SIZE / 2);
        root.getChildren().add(myPowerupImageView);
    }

    /**
     * Makes powerup float down the screen until it hits the character paddle. Once it the powerup hits the character
     * paddle, it will be activated.
     * @param elapsedTime the time elapsed, used to calculate the new position of the powerup as it floats down the scren
     * @param gameScene the gameScene whose the activated powerup will take effect on
     */
    public void setPowerupMotionToWaitActivation(double elapsedTime, GameScene gameScene) {
        myPowerupImageView.setY(myPowerupImageView.getY() + POWERUP_SPEED * elapsedTime);
        if (myPowerupImageView.getBoundsInParent().intersects(gameScene.getMainCharacter().getCharacterImageView().getBoundsInParent())) {
            gameScene.getRoot().getChildren().remove(myPowerupImageView);
            activatePowerup(gameScene);
            myPowerupIsActivated = true;
        }
    }

    private void activatePowerup(GameScene gameScene) {
        if (myPowerupType == PowerupType.POTION) {
            activatePotionPower(gameScene.getGameStatus(), gameScene.getRoot());
        } else if (myPowerupType == PowerupType.HORN) {
            activateHornPower(gameScene, gameScene.getMainCharacter());
        } else if (myPowerupType == PowerupType.LIGHTNING) {
            activateLightningPower(gameScene);
        }
    }

    private void activatePotionPower(GameStatus gameStatus, Pane root) {
        gameStatus.increaseLives(root);
    }

    private void activateHornPower(GameScene gameScene, Character character) {
        if (character.getCharacterImageView() != character.getDumbledoresArmyImageView()) {
            character.changeCharacter(character.getDoubleCharacterImageView(), gameScene.getRoot());
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(e -> character.changeCharacter(character.getSingleCharacterImageView(), gameScene.getRoot()));
            delay.play();
        }
    }

    private void activateLightningPower(GameScene gameScene) {
        Image lightningImage = new Image(this.getClass().getClassLoader().getResourceAsStream("lightninglaser.png"));
        ImageView lightning = new ImageView(lightningImage);
        lightning.setFitHeight(GameMain.SCENE_HEIGHT - gameScene.getMainCharacter().getCharacterImageView().getFitHeight());
        lightning.setFitWidth(30);
        lightning.setX(gameScene.getMainCharacter().getCharacterImageView().getBoundsInParent().getCenterX() - lightning.getFitWidth() / 2);
        lightning.setY(0);
        gameScene.getRoot().getChildren().add(lightning);
        PauseTransition delay = new PauseTransition(Duration.seconds(0.05));
        delay.setOnFinished(e -> makeLightningAndBricksDisappear(lightning, gameScene));
        delay.play();
    }

    private void makeLightningAndBricksDisappear(ImageView lightning, GameScene gameScene) {
        ArrayList<Brick> bricksToRemove = new ArrayList<>();
        for (Brick brick : gameScene.getBricks()) {
            if (lightning.getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                bricksToRemove.add(brick);
            }
        }

        for (Brick brickToRemove : bricksToRemove) {
            gameScene.getRoot().getChildren().remove(brickToRemove.getBrickImageView());
            gameScene.getBricks().remove(brickToRemove);
        }
        gameScene.getRoot().getChildren().remove(lightning);
    }

    /**
     * Gets the ImageView representation of this powerup; useful for removing it from the root of the GameScene
     * if it is still floating at the end.
     * @return the ImageView representation of the powerup.
     */
    public ImageView getPowerupImageView() {
        return myPowerupImageView;
    }

    /**
     * Used to keep track of activated powerups in order to remove their ImageViews,
     * as opposed to unactivated powerups, whose image view will continue floating down the screen.
     * @return the boolean value that expresses whether or not this powerup is activated.
     */
    public boolean powerupIsActivated() {
        return myPowerupIsActivated;
    }

    /**
     * Used to specify which of the three powerups available is being created/used.
     */
    public enum PowerupType {
        LIGHTNING ("lightningpower.png"),
        POTION ("potionpower.png"),
        HORN ("hornpower.png");

        private String myAssociatedFileName;
        PowerupType(String fileName) {
            myAssociatedFileName = fileName;
        }

        private String getPowerupFileName() {
            return myAssociatedFileName;
        }
    }
}
