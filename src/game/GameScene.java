package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameScene {
    public static final int FIRST_ROW_OFFSET = 50;

    private Scene myScene;
    private GameSceneType myGameSceneType;
    private GameSceneType myNextGameSceneType;
    private Pane myRoot;
    private Ball myBall;
    private Character myMainCharacter;
    private GameStatus myGameStatus;
    private ArrayList<Brick> myBricks = new ArrayList<>();
    private ArrayList<Powerup> myPresentPowerups = new ArrayList<>();

    public GameScene(GameSceneType sceneType) {
        myGameSceneType = sceneType;
        if (myGameSceneType == GameSceneType.LEVEL1) {
            myScene = generateLevelScene(GameSceneType.LEVEL2);
        } else if (myGameSceneType == GameSceneType.LEVEL2) {
            myScene = generateLevelScene(GameSceneType.LEVEL3);
        } else if (myGameSceneType == GameSceneType.LEVEL3) {
            myScene = generateLevelScene(GameSceneType.WIN);
        } else if (myGameSceneType == GameSceneType.INTRO) {
            myScene = generateNonLevelScene(GameSceneType.LEVEL1);
        } else if (myGameSceneType == GameSceneType.WIN) {
            myScene = generateNonLevelScene(GameSceneType.INTRO);
        } else if (myGameSceneType == GameSceneType.LOSE) {
            myScene = generateNonLevelScene(GameSceneType.INTRO);
        }
        myScene.setOnKeyPressed(e -> {
            try {
                handleKeyInput(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private Scene generateNonLevelScene(GameSceneType nextGameSceneType) {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;
        setBackground(myGameSceneType.getBackground());
        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);
        setButtonToAdvance(myGameSceneType.getBricksConfigOrButtonText());
        return scene;
    }

    private void setButtonToAdvance(String buttonText) {
        Button button = new Button(buttonText);
        button.setLayoutX(GameMain.SCENE_WIDTH / 2 - button.getBoundsInParent().getWidth() / 2); //TODO: Figure out what's going on here
        button.setLayoutY(GameMain.SCENE_HEIGHT - 100);
        button.setOnAction(e -> {
            GameMain.resetStage(myNextGameSceneType);
        });
        myRoot.getChildren().add(button);
    }

    private Scene generateLevelScene(GameSceneType nextGameSceneType) {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;
        configureBricks(myGameSceneType.getBricksConfigOrButtonText());
        myMainCharacter = new Character(myRoot);
        myBall = new Ball(myRoot, myMainCharacter);
        BackgroundStructure door = new BackgroundStructure(BackgroundStructure.StructureType.DOOR, myRoot);
        myGameStatus = new GameStatus(myRoot);

        setBackground(myGameSceneType.getBackground());
        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);
        scene.setOnMouseMoved(e -> myMainCharacter.movePaddleOnMouseInput(e.getX()));
        return scene;
    }

    private void configureBricks(String path) {
        File bricksConfig = new File(path);
        try {
            Scanner scanner = new Scanner(bricksConfig);
            int yCoordinateForRow = FIRST_ROW_OFFSET;
            int row = 0;
            while (scanner.hasNextLine()) {
                String rowConfiguration = scanner.nextLine();
                setRow(rowConfiguration, row, yCoordinateForRow);
                yCoordinateForRow += Brick.BRICK_HEIGHT;
                row++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception");
        }
    }

    private void setRow(String rowConfiguration, int row, int yCoordinateForRow) {
        int[] rowConfigurationArray = Stream.of(rowConfiguration.split(" " )).mapToInt(Integer::parseInt).toArray();
        int xCoordinateForBrick = 0;
        for (int i=0; i<rowConfigurationArray.length; i++) {
            if (rowConfigurationArray[i] != 0) {
                Brick brick = new Brick(rowConfigurationArray[i], row);
                brick.getBrickImageView().setX(xCoordinateForBrick);
                brick.getBrickImageView().setY(yCoordinateForRow);
                myBricks.add(brick);
                myRoot.getChildren().add(brick.getBrickImageView());
            }
            xCoordinateForBrick += Brick.BRICK_WIDTH;
        }
    }

    public void reconfigureBricksBasedOnHits() {
        Brick brickToDownsize = null;
        double[] brickToDownsizeCoordinates = {0,0};
        for (Brick brick : myBricks) {
            if (myBall.getBallImageView().getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                brickToDownsize = brick;
            }
        }
        if (brickToDownsize != null) {
            brickToDownsizeCoordinates[0] = brickToDownsize.getBrickImageView().getBoundsInParent().getCenterX();
            brickToDownsizeCoordinates[1] = brickToDownsize.getBrickImageView().getBoundsInParent().getCenterY();
            myBall.reflectBall(brickToDownsize);
            brickToDownsize.downsizeBrick(this);
        }
    }

    private void handleKeyInput(KeyEvent e) {
        if (e.getCode() == KeyCode.DIGIT1) {
            GameMain.resetStage(GameSceneType.LEVEL1);
        } else if (e.getCode() == KeyCode.DIGIT2) {
            GameMain.resetStage(GameSceneType.LEVEL2);
        } else if (e.getCode().isDigitKey()) {
            GameMain.resetStage(GameSceneType.LEVEL3);
        } else if (e.getCode() == KeyCode.I) {
            GameMain.resetStage(GameSceneType.INTRO);
        } else if (e.getCode() == KeyCode.W) {
            GameMain.resetStage(GameSceneType.WIN);
        } else if (e.getCode() == KeyCode.R) {
            try {
                myBall.resetBall(myMainCharacter);
            } catch (NullPointerException ex) {
                System.out.println("Cannot reset ball when not in a level");
            }
        } else if (e.getCode() == KeyCode.L) {
            try {
                myGameStatus.increaseLives(myRoot);
            } catch (NullPointerException ex) {
                System.out.println("Cannot increase lives when not in a level");
            }
        } else if (e.getCode() == KeyCode.D) {
            try {
                myMainCharacter.changeCharacter(myMainCharacter.getDumbledoresArmyImageView(), myRoot);
            } catch (NullPointerException ex) {
                System.out.println("Cannot create army when not in a level");
            }
        } else if (e.getCode() == KeyCode.B) {
            myRoot.setEffect(new GaussianBlur(100));
        } else if (e.getCode() == KeyCode.V) {
            myRoot.setEffect(new GaussianBlur(0));
        } else if (e.getCode() == KeyCode.C) {

        } else if (e.getCode() == KeyCode.S) {
            try {
                myMainCharacter.changeCharacter(myMainCharacter.getSingleCharacterImageView(), myRoot);
            } catch (NullPointerException ex) {
                System.out.println("Cannot create single character when not in a level");
            }
        } else {
            return;
        }
    }

    private void setBackground(String backgroundPath) {
        Image imageForBackground = new Image(this.getClass().getClassLoader().getResourceAsStream(backgroundPath));
        BackgroundImage backgroundImage = new BackgroundImage(imageForBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        myRoot.setBackground(background);
    }

    public void clearLevel(double elapsedTime) {
        myScene.setOnMouseMoved(null);
        myRoot.getChildren().remove(myBall.getBallImageView());
        myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH / 2 - myMainCharacter.getCharacterImageView().getBoundsInLocal().getWidth() / 2);
        myMainCharacter.getCharacterImageView().setY(myMainCharacter.getCharacterImageView().getY() - 100 * elapsedTime);
        if (myMainCharacter.getCharacterImageView().getBoundsInParent().getMaxY() == 0) {
            GameMain.resetStage(myNextGameSceneType);
        }
    }

    public void loseLevel() {
        GameMain.resetStage(GameSceneType.LOSE);
    }

    public void handlePowerup(double elapsedTime) {
        ArrayList<Powerup> activatedPowerup = new ArrayList<>();
        for (Powerup powerup : myPresentPowerups) {
            powerup.setPowerupMotion(elapsedTime, this);
            if (powerup.powerupIsActivated()) {
                activatedPowerup.add(powerup);
            }
        }
        for (Powerup powerup : activatedPowerup) {
            myPresentPowerups.remove(powerup);
        }
    }

    public void addToPresentPowerups(Powerup powerup) {
        myPresentPowerups.add(powerup);
    }

    public Scene getScene() {
        return myScene;
    }

    public Pane getRoot() {
        return myRoot;
    }

    public ArrayList<Brick> getBricks() {
        return myBricks;
    }

    public Character getMainCharacter() {
        return myMainCharacter;
    }

    public Ball getBall() {
        return myBall;
    }

    public GameStatus getGameStatus() {
        return myGameStatus;
    }

    public GameSceneType getGameSceneType() {
        return myGameSceneType;
    }

    public enum GameSceneType {
        INTRO ("backgroundintro.jpg", "Start"),
        LEVEL1 ("background1.jpg", "resources/level1.txt"),
        LEVEL2 ("background2.jpg", "resources/level2.txt"),
        LEVEL3 ("background3.jpg", "resources/level3.txt"),
        LOSE ("backgroundlose.jpg", "Try Again"),
        WIN ("backgroundwin.jpg", "Play again");

        private String myBackground;
        private String myBricksConfigOrButtonText;


        GameSceneType(String background, String bricksConfigOrButtonText) {
            myBackground = background;
            myBricksConfigOrButtonText = bricksConfigOrButtonText;
        }

        private String getBricksConfigOrButtonText() {
            return myBricksConfigOrButtonText;
        }

        private String getBackground() {
            return myBackground;
        }
    }
}

