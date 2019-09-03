package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameScene {
    private static final String BALL_IMAGE = "ball.png";
    private static final String LEVEL_ONE_BACKGROUND = "background1.jpg";
    private static final String LEVEL_TWO_BACKGROUND = "background2.jpg";
    private static final String LEVEL_THREE_BACKGROUND = "background3.jpg";
    private static final String INTRO_BACKGROUND = "backgroundintro.jpg";
    private static final String HOW_TO_PLAY_BACKGROUND = "backgroundhowtoplay.jpg";
    private static final String LOSE_BACKGROUND = "backgroundlose.jpg";
    private static final String WIN_BACKGROUND = "backgroundwin.jpg";
    private static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    private static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    private static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";
    private static final int BALL_SIZE = 30;
    private static final int BALL_SPEED = 500;
    private final int FIRST_ROW_OFFSET = 50;


    private int myLives;
    private Stage myStage;
    private Scene myScene;
    private Character myMainCharacter;
    private GameSceneType myNextGameSceneType;
    private ImageView myBall;
    private Pane myRoot;
    private ArrayList<Brick> myBricks = new ArrayList<>();
    private GameSceneType myGameSceneType;
    private int[] myBallDirection = {1,-1};



    public GameScene(Stage stage, GameSceneType sceneType) throws Exception {
        myStage = stage;
        myGameSceneType = sceneType;

        if (myGameSceneType == GameSceneType.LEVEL1) {
            myScene = generateLevelScene(LEVEL_ONE_BRICKS_CONFIG_PATH, LEVEL_ONE_BACKGROUND);
            setNextGameSceneType(GameSceneType.LEVEL2);
        } else if (myGameSceneType == GameSceneType.LEVEL2) {
            myScene = generateLevelScene(LEVEL_TWO_BRICKS_CONFIG_PATH, LEVEL_TWO_BACKGROUND);
            setNextGameSceneType(GameSceneType.LEVEL3);
        } else if (myGameSceneType == GameSceneType.LEVEL3) {
            myScene = generateLevelScene(LEVEL_THREE_BRICKS_CONFIG_PATH, LEVEL_THREE_BACKGROUND);
            setNextGameSceneType(GameSceneType.WIN);
        } else if (myGameSceneType == GameSceneType.INTRO) {
            myScene = generateNonLevelScene(INTRO_BACKGROUND);
            setNextGameSceneType(GameSceneType.HOW_TO_PLAY);
        } else if (myGameSceneType == GameSceneType.HOW_TO_PLAY) {
            myScene = generateNonLevelScene(HOW_TO_PLAY_BACKGROUND);
            setNextGameSceneType(GameSceneType.LEVEL1);
        } else if (myGameSceneType == GameSceneType.WIN) {
            myScene = generateNonLevelScene(WIN_BACKGROUND);
            setNextGameSceneType(GameSceneType.INTRO);
        } else if (myGameSceneType == GameSceneType.LOSE) {
            myScene = generateNonLevelScene(LOSE_BACKGROUND);
            setNextGameSceneType(GameSceneType.INTRO);
        }
    }

    public Scene getScene() {
        return myScene;
    }

    public void setNextGameSceneType(GameSceneType nextGameSceneType) {
        myNextGameSceneType = nextGameSceneType;
    }

    private Scene generateNonLevelScene(String backgroundPath) {
        myRoot = new Pane();
        setBackground(backgroundPath);
        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);

        if (myGameSceneType == GameSceneType.INTRO) {
            setButtonToAdvance("Start");
        } else if (myGameSceneType == GameSceneType.HOW_TO_PLAY) {
            setButtonToAdvance("Begin Game");
        } else if (myGameSceneType == GameSceneType.LOSE) {
            setButtonToAdvance("Try Again");
        } else if (myGameSceneType == GameSceneType.WIN) {
            setButtonToAdvance("Play Again");
        }
        return scene;
    }

    private Scene generateLevelScene(String bricksConfigPath, String backgroundPath) throws Exception {
        myRoot = new Pane();
        setupBricksConfig(bricksConfigPath);

        myMainCharacter = new Character(Character.CharacterEnum.HARRY_POTTER);
        myMainCharacter.setCharacterAsPaddle(myRoot);

        createAndSetBall();

        Structure door = new Structure(Structure.StructureEnum.DOOR);
        door.setStructureAsDoor(myRoot);

        myLives = 3;

        setButtonToAdvance("Go to next level"); //TODO: delete this later; only here for easier navigation now

        setBackground(backgroundPath);

        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);
        scene.setOnMouseMoved(e -> movePaddleOnMouseInput(e.getX()));
        return scene;
    }

    private void setButtonToAdvance(String buttonText) {
        Button button = new Button(buttonText);
        button.setLayoutX(GameMain.SCENE_WIDTH / 2 - button.getWidth()/2);
        button.setLayoutY(GameMain.SCENE_HEIGHT - 100);
        button.setOnAction(e -> {
            try {
                GameMain.resetStage(myNextGameSceneType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        myRoot.getChildren().add(button);
    }

    private void setBackground(String backgroundPath) {
        Image imageForBackground = new Image(this.getClass().getClassLoader().getResourceAsStream(backgroundPath));
        BackgroundImage backgroundImage = new BackgroundImage(imageForBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        myRoot.setBackground(background);
    }

    private void setupBricksConfig(String path) throws Exception {
        File bricksConfig = new File(path);
        Scanner scanner = new Scanner(bricksConfig);
        int yCoordinateForRow = FIRST_ROW_OFFSET;

        while (scanner.hasNextLine()) {
            String rowConfiguration = scanner.nextLine();
            setRow(rowConfiguration, yCoordinateForRow);
            yCoordinateForRow += Brick.BRICK_HEIGHT;
        }
        scanner.close();
    }

    private void setRow(String rowConfiguration, int yCoordinateForRow) {
        int[] rowConfigurationArray = Stream.of(rowConfiguration.split(" " )).mapToInt(Integer::parseInt).toArray();
        int xCoordinateForBrick = 0;
        for (int i=0; i<rowConfigurationArray.length; i++) {
            if (rowConfigurationArray[i] != 0) {
                Brick brick = new Brick(rowConfigurationArray[i]);
                brick.getBrickImageView().setX(xCoordinateForBrick);
                brick.getBrickImageView().setY(yCoordinateForRow);
                myBricks.add(brick);
                myRoot.getChildren().add(brick.getBrickImageView());
            }
            xCoordinateForBrick += Brick.BRICK_WIDTH;
        }
    }

    public void setBricksHits() {
        Brick brickToDownsize = null;
        for (Brick brick : myBricks) {
            if (myBall.getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                brickToDownsize = brick;
            }
        }
        downsizeBrick(brickToDownsize);
    }

    private void downsizeBrick(Brick brickToDownsize) {
        if (brickToDownsize != null) {
            brickToDownsize.decreaseHitsRemaining();
            if (brickToDownsize.getHitsRemaining() == 0) {
                myRoot.getChildren().remove(brickToDownsize.getBrickImageView());
                myBricks.remove(brickToDownsize);
            }
            if (myBall.getBoundsInParent().getMaxY() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinY()
                    || myBall.getBoundsInParent().getMinY() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxY()) {
                myBallDirection[1] *= -1;
            }
            if (myBall.getBoundsInParent().getMaxX() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinX()
                    || myBall.getBoundsInParent().getMinX() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxX()) {
                myBallDirection[0] *= -1;
            }
        }
    }

    private void movePaddleOnMouseInput (double x) {
        myMainCharacter.getCharacterImageView().setX(x);
        if (myMainCharacter.getCharacterImageView().getBoundsInLocal().getMaxX() >= GameMain.SCENE_WIDTH) {
            myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH - myMainCharacter.getCharacterImageView().getFitWidth());
        }
    }

    private void createAndSetBall() {
        Image ballImage =  new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBall = new ImageView(ballImage);
        myBall.setFitHeight(BALL_SIZE);
        myBall.setFitWidth(BALL_SIZE);
        myBall.setX(myMainCharacter.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        myBall.setY(myMainCharacter.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
        myRoot.getChildren().add(myBall);
    }

    private void resetBall() {
        myBall.setX(myMainCharacter.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        myBall.setY(myMainCharacter.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
        myBallDirection[0] = 1;
        myBallDirection[1] = -1;
    }

    public void setBallMotion(double elapsedTime) throws Exception {
        myBall.setX(myBall.getX() + myBallDirection[0] * BALL_SPEED * elapsedTime);
        myBall.setY(myBall.getY() + myBallDirection[1] * BALL_SPEED * elapsedTime);
        wallCollisionCheck();
        paddleCollisionCheck();
    }

    private void wallCollisionCheck() throws Exception {
        if (myBall.getX() <= 0 || myBall.getX() >= GameMain.SCENE_WIDTH - myBall.getBoundsInLocal().getWidth()) {
            myBallDirection[0] *= -1;
        }
        if (myBall.getY() <= 0) {
            myBallDirection[1] *= -1;
        } else if (myBall.getY() >= (GameMain.SCENE_HEIGHT - myBall.getBoundsInLocal().getHeight())) {
            myLives--;
            if (myLives == 0) {
                GameMain.resetStage(GameSceneType.LOSE);
            } else {
                resetBall();
                myBallDirection[1] *= -1;
            }
        }
    }

    private void paddleCollisionCheck() {
        if (myBall.getBoundsInParent().intersects(myMainCharacter.getCharacterImageView().getBoundsInParent()) &&
                myBall.getBoundsInParent().getMaxY() <= myMainCharacter.getCharacterImageView().getBoundsInParent().getMinY()) {
            myBallDirection[1] *= -1;
        }
    }
}

