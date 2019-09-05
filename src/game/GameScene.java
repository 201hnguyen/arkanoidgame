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
    // TODO: Implement power ups
    // TODO: Implement dead zones
    public static final String BALL_IMAGE = "ball.png";
    public static final String HEART_IMAGE = "heart.png";
    public static final int HEART_WIDTH = 25;
    public static final int HEART_HEIGHT = 30;
    public static final String LEVEL_ONE_BACKGROUND = "background1.jpg";
    public static final String LEVEL_TWO_BACKGROUND = "background2.jpg";
    public static final String LEVEL_THREE_BACKGROUND = "background3.jpg";
    public static final String INTRO_BACKGROUND = "backgroundintro.jpg";
    public static final String LOSE_BACKGROUND = "backgroundlose.jpg";
    public static final String WIN_BACKGROUND = "backgroundwin.jpg";
    public static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    public static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    public static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";
    public static final int BALL_SIZE = 50;
    public static final int BALL_SPEED = 350;
    public static final int FIRST_ROW_OFFSET = 50;
    public static final int PADDLE_THIRD_DIVISION = 50;

    private Stage myStage;
    private Scene myScene;
    private ImageView myBall;
    private Pane myRoot;
    private Character myMainCharacter;
    private int myLives;
    private int[] myBallDirection;
    private GameSceneType myGameSceneType;
    private GameSceneType myNextGameSceneType;
    private ArrayList<Brick> myBricks = new ArrayList<>();
    private ArrayList<ImageView> myHearts = new ArrayList<>();

    public GameScene(Stage stage, GameSceneType sceneType) throws Exception {
        myStage = stage;
        myGameSceneType = sceneType;

        if (myGameSceneType == GameSceneType.LEVEL1) {
            myScene = generateLevelScene(LEVEL_ONE_BRICKS_CONFIG_PATH, LEVEL_ONE_BACKGROUND, GameSceneType.LEVEL2);
        } else if (myGameSceneType == GameSceneType.LEVEL2) {
            myScene = generateLevelScene(LEVEL_TWO_BRICKS_CONFIG_PATH, LEVEL_TWO_BACKGROUND, GameSceneType.LEVEL3);
        } else if (myGameSceneType == GameSceneType.LEVEL3) {
            myScene = generateLevelScene(LEVEL_THREE_BRICKS_CONFIG_PATH, LEVEL_THREE_BACKGROUND, GameSceneType.WIN);
        } else if (myGameSceneType == GameSceneType.INTRO) {
            myScene = generateNonLevelScene(INTRO_BACKGROUND, GameSceneType.LEVEL1);
        } else if (myGameSceneType == GameSceneType.WIN) {
            myScene = generateNonLevelScene(WIN_BACKGROUND, GameSceneType.INTRO);
        } else if (myGameSceneType == GameSceneType.LOSE) {
            myScene = generateNonLevelScene(LOSE_BACKGROUND, GameSceneType.INTRO);
        }
    }

    public Scene getScene() {
        return myScene;
    }

    public int getBricksRemaining() {
        return myBricks.size();
    }

    private Scene generateNonLevelScene(String backgroundPath, GameSceneType nextGameSceneType) {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;
        setBackground(backgroundPath);
        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);

        if (myGameSceneType == GameSceneType.INTRO) {
            setButtonToAdvance("Start");
        } else if (myGameSceneType == GameSceneType.LOSE) {
            setButtonToAdvance("Try Again");
        } else if (myGameSceneType == GameSceneType.WIN) {
            setButtonToAdvance("Play Again");
        }
        return scene;
    }

    private Scene generateLevelScene(String bricksConfigPath, String backgroundPath, GameSceneType nextGameSceneType) throws Exception {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;

        setupBricksConfig(bricksConfigPath);

        myMainCharacter = new Character(Character.CharacterEnum.HARRY_POTTER);
        myMainCharacter.setCharacterAsPaddle(myRoot);

        myBallDirection = new int[]{0, -1};
        createAndSetBall();

        Structure door = new Structure(Structure.StructureEnum.DOOR);
        door.setStructureAsDoor(myRoot);

        myLives = 3;
        setHearts();

        setButtonToAdvance("Go to next level"); //TODO: delete this later; only here for easier navigation now

        setBackground(backgroundPath);
        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);
        scene.setOnMouseMoved(e -> movePaddleOnMouseInput(e.getX()));
        return scene;
    }

    private void setButtonToAdvance(String buttonText) {
        Button button = new Button(buttonText);
        button.setLayoutX(GameMain.SCENE_WIDTH / 2 - button.getBoundsInParent().getWidth() / 2); //TODO: Figure out what's going on here
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

    public void clearLevel(double elapsedTime) throws Exception {
        myScene.setOnMouseMoved(null);
        myRoot.getChildren().remove(myBall);
        myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH / 2 - myMainCharacter.getCharacterImageView().getBoundsInLocal().getWidth() / 2);
        myMainCharacter.getCharacterImageView().setY(myMainCharacter.getCharacterImageView().getY() - 100 * elapsedTime);
        if (myMainCharacter.getCharacterImageView().getBoundsInParent().getMaxY() == 0) {
            GameMain.resetStage(myNextGameSceneType);
        }
    }

    public void  reconfigureBricksBasedOnHits() {
        Brick brickToDownsize = null;
        for (Brick brick : myBricks) {
            if (myBall.getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                brickToDownsize = brick;
            }
        }
        reflectBallAndDownsizeBrick(brickToDownsize);
    }

    private void reflectBallAndDownsizeBrick(Brick brickToDownsize) {
        if (brickToDownsize != null) {
            boolean ballComesFromTop = myBall.getBoundsInParent().getCenterY() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinY();
            boolean ballComesFromBottom = myBall.getBoundsInParent().getCenterY() >= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxY();
            boolean ballComesFromLeft = myBall.getBoundsInParent().getCenterX() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinX();
            boolean ballComesFromRight = myBall.getBoundsInParent().getCenterX() >= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxX();

            if (ballComesFromTop && ballComesFromLeft || ballComesFromTop && ballComesFromRight ||
            ballComesFromBottom && ballComesFromLeft || ballComesFromBottom && ballComesFromRight) {
                myBallDirection[0] *= -1;
                myBallDirection[1] *= -1;
            } else if (ballComesFromBottom || ballComesFromTop) {
                myBallDirection[1] *= -1;
            } else if (ballComesFromLeft || ballComesFromRight) {
                myBallDirection[0] *= -1;
            }

            brickToDownsize.decreaseHitsRemaining();
            if (brickToDownsize.getHitsRemaining() == 0) {
                myRoot.getChildren().remove(brickToDownsize.getBrickImageView());
                myBricks.remove(brickToDownsize);
            }
        }
    }

    public void setHearts() {
        Image heart = new Image(this.getClass().getClassLoader().getResourceAsStream(HEART_IMAGE));
        int xOffset = 95;
        int yOffset = 5;
        for (int i=0; i<myLives; i++) {
            ImageView heartImageView = new ImageView(heart);
            heartImageView.setFitWidth(HEART_WIDTH);
            heartImageView.setFitHeight(HEART_HEIGHT);
            heartImageView.setX(xOffset);
            heartImageView.setY(yOffset);
            myHearts.add(heartImageView);
            myRoot.getChildren().add(heartImageView);
            xOffset += HEART_WIDTH + 5;
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
        myRoot.getChildren().add(myBall);
        resetBall();
    }

    private void resetBall() {
        myBall.setX(myMainCharacter.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
        myBall.setY(myMainCharacter.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
        myBallDirection[0] = 0;
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
        } else if (myBall.getY() >= (GameMain.SCENE_HEIGHT)) {
            myLives--;
            if (myLives == 0) {
                GameMain.resetStage(GameSceneType.LOSE);
            } else {
                myRoot.getChildren().remove(myHearts.get(myHearts.size()-1));
                myHearts.remove(myHearts.size() -1);
                resetBall();
                myBallDirection[1] *= -1;
            }
        }
    }

    private void paddleCollisionCheck() {
        if (myBall.getBoundsInParent().intersects(myMainCharacter.getCharacterImageView().getBoundsInParent()) &&
                myBall.getBoundsInParent().getMaxY() <= myMainCharacter.getCharacterImageView().getBoundsInParent().getMinY()) {
            if (myBallDirection[0] == 0) {
                myBallDirection[0] = -1;
            }
            if (myBall.getBoundsInParent().getCenterX() <= myMainCharacter.getCharacterImageView().getBoundsInParent().getCenterX() - PADDLE_THIRD_DIVISION) {
                myBallDirection[0] = -1;
                myBallDirection[1] = -1;
            } else if (myBall.getBoundsInParent().getCenterX() >= myMainCharacter.getCharacterImageView().getBoundsInParent().getCenterX() + PADDLE_THIRD_DIVISION) {
                myBallDirection[0] = 1;
                myBallDirection[1] = -1;
            } else {
                myBallDirection[1] *= -1;
            }
        }
    }
}

