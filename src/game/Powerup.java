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
    private double[] myPowerBrickCoordinate;
    private boolean myPowerupIsActivated;

    public Powerup(PowerupType type) {
        myPowerupIsActivated = false;
        myPowerupType = type;
        myPowerBrickCoordinate = new double[]{0,0};

        Image powerupImage = new Image(this.getClass().getClassLoader().getResourceAsStream(type.getPowerupFileName()));
        myPowerupImageView = new ImageView(powerupImage);
        myPowerupImageView.setFitWidth(ICON_SIZE);
        myPowerupImageView.setFitHeight(ICON_SIZE);
    }

    public void setPowerup(Brick brickWithPowerup, GameScene gameScene) {
        revealPowerup(brickWithPowerup, gameScene.getRoot());
        gameScene.addToPresentPowerups(this);
        setPowerBrickCoordinate(brickWithPowerup.getBrickImageView().getBoundsInParent().getCenterX(),
                brickWithPowerup.getBrickImageView().getBoundsInParent().getMinY());
    }

    public void revealPowerup(Brick brick, Pane root) {
        myPowerupImageView.setX(brick.getBrickImageView().getBoundsInParent().getCenterX() - ICON_SIZE / 2);
        myPowerupImageView.setY(brick.getBrickImageView().getBoundsInParent().getCenterY() - ICON_SIZE / 2);
        root.getChildren().add(myPowerupImageView);
    }

    public void setPowerupMotion(double elapsedTime, GameScene gameScene) {
        myPowerupImageView.setY(myPowerupImageView.getY() + POWERUP_SPEED * elapsedTime);
        if (myPowerupImageView.getBoundsInParent().intersects(gameScene.getMainCharacter().getCharacterImageView().getBoundsInParent())) {
            gameScene.getRoot().getChildren().remove(myPowerupImageView);
            activatePowerup(gameScene);
            myPowerupIsActivated = true;
        }
    }

    private void activatePowerup(GameScene gameScene) {
        if (myPowerupType == PowerupType.POTION) {
            activatePotionPower(gameScene, gameScene.getGameStatus(), gameScene.getRoot());
        } else if (myPowerupType == PowerupType.HORN) {
            activateHornPower(gameScene, gameScene.getMainCharacter());
        } else if (myPowerupType == PowerupType.LIGHTNING) {
            activateLightningPower(gameScene);
        }
    }

    private void activatePotionPower(GameScene gameScene, GameStatus gameStatus, Pane root) {
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
        Image lightningImage = new Image(this.getClass().getClassLoader().getResourceAsStream("lightning.png"));
        ImageView lightning = new ImageView(lightningImage);
        lightning.setFitHeight(150);
        lightning.setFitWidth(150);
        lightning.setX(myPowerBrickCoordinate[0]);
        lightning.setY(myPowerBrickCoordinate[1]);
        gameScene.getRoot().getChildren().add(lightning);
        PauseTransition delay = new PauseTransition(Duration.seconds(0.05));
        delay.setOnFinished(e -> makeLightningAndBricksDisappear(lightning, gameScene));
        delay.play();
    }

    private void makeLightningAndBricksDisappear(ImageView lightning, GameScene gameScene) {
        lightning.setX(lightning.getX());
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

    private void setPowerBrickCoordinate(double x, double y) {
        myPowerBrickCoordinate[0] = x;
        myPowerBrickCoordinate[1] = y;
    }

    public boolean powerupIsActivated() {
        return myPowerupIsActivated;
    }

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
