package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameScene {
    public static final int FIRST_ROW_OFFSET = 50;
    public static final int CHARACTER_FLOAT_SPEED = 200;

    private Stage myStageReference;
    private Scene myScene;
    private GameSceneType myGameSceneType;
    private GameSceneType myNextGameSceneType;
    private Pane myRoot;
    private Ball myBall;
    private Character myMainCharacter;
    private GameStatus myGameStatus;
    private ArrayList<Brick> myBricks = new ArrayList<>();
    private ArrayList<Powerup> myPresentPowerups = new ArrayList<>();
    private ArrayList<BackgroundStructure> myDeadZones = new ArrayList<>();

    public GameScene(Stage stage, GameSceneType sceneType, GameStatus gameStatus) {
        myStageReference = stage;
        myGameStatus = gameStatus;
        myGameSceneType = sceneType;

        if (myGameSceneType == GameSceneType.INTRO) {
            myScene = generateNonLevelScene(GameSceneType.HOW_TO_PLAY);
            myGameStatus.resetGameStatus();

        } else if (myGameSceneType == GameSceneType.HOW_TO_PLAY) {
            myScene = generateNonLevelScene(GameSceneType.LEVEL1);

        } else if (myGameSceneType == GameSceneType.LEVEL1) {
            myScene = generateLevelScene(GameSceneType.LEVEL2);

        } else if (myGameSceneType == GameSceneType.LEVEL2) {
            myScene = generateLevelScene(GameSceneType.LEVEL3);

        } else if (myGameSceneType == GameSceneType.LEVEL3) {
            myScene = generateLevelScene(GameSceneType.WIN);

        } else if (myGameSceneType == GameSceneType.WIN) {
            myScene = generateNonLevelScene(GameSceneType.INTRO);

        } else if (myGameSceneType == GameSceneType.LOSE) {
            myScene = generateNonLevelScene(GameSceneType.INTRO);
        }
        myScene.setOnKeyPressed(e -> handleKeyInput(e));
    }

    private Scene generateNonLevelScene(GameSceneType nextGameSceneType) {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;
        setBackground(myGameSceneType.getBackgroundPath());
        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);
        setButtonToAdvance(myGameSceneType.getBricksConfigOrButtonText());
        if (myGameSceneType != GameSceneType.INTRO) {
            myRoot.getChildren().add(myGameStatus.getScoreLabelForNonLevel());
        }
        return scene;
    }

    private void setButtonToAdvance(String buttonText) {
        Button button = new Button(buttonText);
        button.setLayoutX(GameMain.SCENE_WIDTH / 2 - button.getBoundsInParent().getWidth() / 2);
        button.setLayoutY(GameMain.SCENE_HEIGHT - 70);
        button.setOnAction(e -> {
            GameMain.resetStage(myStageReference, myNextGameSceneType, myGameStatus);
        });
        myRoot.getChildren().add(button);
    }

    private Scene generateLevelScene(GameSceneType nextGameSceneType) {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;
        configureBricks(myGameSceneType.getBricksConfigOrButtonText());
        myMainCharacter = new Character();
        myBall = new Ball(myMainCharacter);
        BackgroundStructure door = new BackgroundStructure(BackgroundStructure.StructureType.DOOR, Optional.empty(), Optional.empty(), Optional.empty());

        myRoot.getChildren().add(myMainCharacter.getCharacterImageView());
        myRoot.getChildren().add(myBall.getBallImageView());
        myRoot.getChildren().add(door.getStructureImageView());
        myRoot.getChildren().add(myGameStatus.getScoreLabelForLevel());
        addDeadZonesToLevel();
        myGameStatus.resetLivesAndAddImageViewToRoot(myRoot);

        setBackground(myGameSceneType.getBackgroundPath());
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

    private void addDeadZonesToLevel() {
        BackgroundStructure deadZoneOne = new BackgroundStructure(BackgroundStructure.StructureType.TEETH,
                Optional.of(BackgroundStructure.RIGHT_WALL_COORDINATE_FOR_TEETH), Optional.of(200), Optional.of(0));
        BackgroundStructure deadZoneTwo = new BackgroundStructure(BackgroundStructure.StructureType.TEETH,
                Optional.of(0), Optional.of(450), Optional.of(180));
        BackgroundStructure deadZoneThree = new BackgroundStructure(BackgroundStructure.StructureType.TEETH,
                Optional.of(BackgroundStructure.RIGHT_WALL_COORDINATE_FOR_TEETH), Optional.of(200), Optional.of(0));
        BackgroundStructure deadZoneFour = new BackgroundStructure(BackgroundStructure.StructureType.TEETH,
                Optional.of(0), Optional.of(100), Optional.of(180));

        myDeadZones.add(deadZoneOne);
        if (myGameSceneType == GameScene.GameSceneType.LEVEL2) {
            myDeadZones.add(deadZoneTwo);
        } else if (myGameSceneType == GameScene.GameSceneType.LEVEL3) {
            myDeadZones.add(deadZoneTwo);
            myDeadZones.add(deadZoneThree);
            myDeadZones.add(deadZoneFour);
        }

        for (BackgroundStructure deadZone : myDeadZones) {
            myRoot.getChildren().add(deadZone.getStructureImageView());
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

    private void setBackground(String backgroundPath) {
        Image imageForBackground = new Image(this.getClass().getClassLoader().getResourceAsStream(backgroundPath));
        BackgroundImage backgroundImage = new BackgroundImage(imageForBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        myRoot.setBackground(background);
    }

    private void handleKeyInput(KeyEvent e) {
        if (e.getCode() == KeyCode.DIGIT1) {
            GameMain.resetStage(myStageReference, GameSceneType.LEVEL1, myGameStatus);
        } else if (e.getCode() == KeyCode.DIGIT2) {
            GameMain.resetStage(myStageReference, GameSceneType.LEVEL2, myGameStatus);
        } else if (e.getCode().isDigitKey()) {
            GameMain.resetStage(myStageReference, GameSceneType.LEVEL3, myGameStatus);
        } else if (e.getCode() == KeyCode.I) {
            GameMain.resetStage(myStageReference, GameSceneType.INTRO, myGameStatus);
        } else if (e.getCode() == KeyCode.W) {
            GameMain.resetStage(myStageReference, GameSceneType.WIN, myGameStatus);
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
        } else if (e.getCode() == KeyCode.S) {
            try {
                myMainCharacter.changeCharacter(myMainCharacter.getSingleCharacterImageView(), myRoot);
            } catch (NullPointerException ex) {
                System.out.println("Cannot create single character when not in a level");
            }
        } else if (e.getCode() == KeyCode.B) {
            myRoot.setEffect(new GaussianBlur(100));
        } else if (e.getCode() == KeyCode.V) {
            myRoot.setEffect(new GaussianBlur(0));
        } else if (e.getCode() == KeyCode.C) {
            try {
                clearRow();
            } catch (NullPointerException ex) {
                System.out.println("Cannot clear brick when not in a level");
            }
        } else if (e.getCode() == KeyCode.UP) {
            myMainCharacter.changePosition(10);
        } else if (e.getCode() == KeyCode.DOWN) {
            myMainCharacter.changePosition(-10);
        } else {
            return;
        }
    }

    private void clearRow() {
        int lowestRow = 0;
        ArrayList<Brick> brickInLowestRow = new ArrayList<>();
        for (Brick brick : myBricks) {
            if (brick.getRow() > lowestRow) {
                lowestRow = brick.getRow();
            }
        }
        for (Brick brick : myBricks) {
            if (brick.getRow() == lowestRow) {
                brickInLowestRow.add(brick);
            }
        }
        for (Brick brick : brickInLowestRow) {
            myRoot.getChildren().remove(brick.getBrickImageView());
            myBricks.remove(brick);
        }
    }

    public void clearLevel(double elapsedTime) {
        for (int i=0; i<myGameStatus.getLivesRemaining(); i++) {
            myGameStatus.increaseScore(2);
        }
        for (Powerup powerup : myPresentPowerups) {
            myRoot.getChildren().remove(powerup);
        }
        myScene.setOnMouseMoved(null);
        myRoot.getChildren().remove(myBall.getBallImageView());
        myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH / 2 - myMainCharacter.getCharacterImageView().getBoundsInLocal().getWidth() / 2);
        myMainCharacter.getCharacterImageView().setY(myMainCharacter.getCharacterImageView().getY() - CHARACTER_FLOAT_SPEED * elapsedTime);
        if (myMainCharacter.getCharacterImageView().getBoundsInParent().getMaxY() == 0) {
            GameMain.resetStage(myStageReference, myNextGameSceneType, myGameStatus);
        }
    }

    public void loseLevel() {
        GameMain.resetStage(myStageReference, GameSceneType.LOSE, myGameStatus);
    }

    public void addToPresentPowerups(Powerup powerup) {
        myPresentPowerups.add(powerup);
    }

    public void handlePowerup(double elapsedTime) {
        ArrayList<Powerup> activatedPowerup = new ArrayList<>();
        for (Powerup powerup : myPresentPowerups) {
            powerup.setPowerupMotionToWaitActivation(elapsedTime, this);
            if (powerup.powerupIsActivated()) {
                activatedPowerup.add(powerup);
            }
        }
        for (Powerup powerup : activatedPowerup) {
            myPresentPowerups.remove(powerup);
        }
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

    public ArrayList<BackgroundStructure> getDeadZones() {
        return myDeadZones;
    }

    public enum GameSceneType {
        INTRO ("backgroundintro.jpg", "Start"),
        HOW_TO_PLAY ("howtoplay.jpg", "Begin game"),
        LEVEL1 ("background1.jpg", "resources/level1.txt"),
        LEVEL2 ("background2.jpg", "resources/level2.txt"),
        LEVEL3 ("background3.jpg", "resources/level3.txt"),
        LOSE ("backgroundlose.jpg", "Try Again"),
        WIN ("backgroundwin.jpg", "Play again");

        private String myBackgroundPath;
        private String myBricksConfigOrButtonText;


        GameSceneType(String backgroundPath, String bricksConfigOrButtonText) {
            myBackgroundPath = backgroundPath;
            myBricksConfigOrButtonText = bricksConfigOrButtonText;
        }

        private String getBricksConfigOrButtonText() {
            return myBricksConfigOrButtonText;
        }

        private String getBackgroundPath() {
            return myBackgroundPath;
        }
    }
}

