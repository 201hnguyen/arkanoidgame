package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class GameScene {
    public static final String BALL_IMAGE = "ball.png";
    public static final String HEART_IMAGE = "heart.png";
    public static final int HEART_WIDTH = 25;
    public static final int HEART_HEIGHT = 30;
    public static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    public static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    public static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";
    public static final int BALL_SIZE = 50;
    public static final int BALL_SPEED = 350;
    public static final int FULL_LIVES = 3;

    private Scene myScene;
    private ImageView myBall;
    private Pane myRoot;
    private Character myMainCharacter;
    private int[] myBallDirection;
    private GameSceneType myGameSceneType;
    private GameSceneType myNextGameSceneType;
    private ArrayList<ImageView> myLives = new ArrayList<>();
    private BrickStructure myBrickStructure;

    public GameScene(GameSceneType sceneType) throws Exception {
        myGameSceneType = sceneType;

        if (myGameSceneType == GameSceneType.LEVEL1) {
            myScene = generateLevelScene(LEVEL_ONE_BRICKS_CONFIG_PATH, GameSceneType.LEVEL2);
        } else if (myGameSceneType == GameSceneType.LEVEL2) {
            myScene = generateLevelScene(LEVEL_TWO_BRICKS_CONFIG_PATH, GameSceneType.LEVEL3);
        } else if (myGameSceneType == GameSceneType.LEVEL3) {
            myScene = generateLevelScene(LEVEL_THREE_BRICKS_CONFIG_PATH, GameSceneType.WIN);
        } else if (myGameSceneType == GameSceneType.INTRO) {
            myScene = generateNonLevelScene(GameSceneType.LEVEL1);
        } else if (myGameSceneType == GameSceneType.WIN) {
            myScene = generateNonLevelScene(GameSceneType.INTRO);
        } else if (myGameSceneType == GameSceneType.LOSE) {
            myScene = generateNonLevelScene(GameSceneType.INTRO);
        }
    }

    public Scene getScene() {
        return myScene;
    }

    public BrickStructure getBrickStructure() {
        return myBrickStructure;
    }

    public ImageView getBall() {
        return myBall;
    }

    public int[] getBallDirection() {
        return myBallDirection;
    }

    private Scene generateNonLevelScene(GameSceneType nextGameSceneType) {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;
        setBackground(myGameSceneType.getBackground());
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

    private Scene generateLevelScene(String bricksConfigPath, GameSceneType nextGameSceneType) throws Exception {
        myRoot = new Pane();
        myNextGameSceneType = nextGameSceneType;

        myBrickStructure = new BrickStructure(bricksConfigPath, myRoot);

        myMainCharacter = new Character(Character.CharacterEnum.HARRY_POTTER);
        myMainCharacter.setCharacterAsPaddle(myRoot);

        myBallDirection = new int[]{0, -1};
        createAndSetBall();

        BackgroundStructure door = new BackgroundStructure(BackgroundStructure.StructureEnum.DOOR);
        door.setStructureAsDoor(myRoot);

        setLives();

        setButtonToAdvance("Go to next level"); //TODO: delete this later; only here for easier navigation now

        setBackground(myGameSceneType.getBackground());
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

    public void clearLevel(double elapsedTime) throws Exception {
        myScene.setOnMouseMoved(null);
        myRoot.getChildren().remove(myBall);
        myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH / 2 - myMainCharacter.getCharacterImageView().getBoundsInLocal().getWidth() / 2);
        myMainCharacter.getCharacterImageView().setY(myMainCharacter.getCharacterImageView().getY() - 100 * elapsedTime);
        if (myMainCharacter.getCharacterImageView().getBoundsInParent().getMaxY() == 0) {
            GameMain.resetStage(myNextGameSceneType);
        }
    }

    public void setLives() {
        Image heart = new Image(this.getClass().getClassLoader().getResourceAsStream(HEART_IMAGE));
        int xOffset = 95;
        int yOffset = 5;
        for (int i=0; i<FULL_LIVES; i++) {
            ImageView heartImageView = new ImageView(heart);
            heartImageView.setFitWidth(HEART_WIDTH);
            heartImageView.setFitHeight(HEART_HEIGHT);
            heartImageView.setX(xOffset);
            heartImageView.setY(yOffset);
            myRoot.getChildren().add(heartImageView);
            myLives.add(heartImageView);
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
        paddleCollisionCheck();
        wallCollisionCheck();
    }

    private void wallCollisionCheck() throws Exception {
        if (myBall.getX() <= 0 || myBall.getX() >= GameMain.SCENE_WIDTH - myBall.getBoundsInLocal().getWidth()) {
            myBallDirection[0] *= -1;
        }
        if (myBall.getY() <= 0) {
            myBallDirection[1] *= -1;
        } else if (myBall.getY() >= (GameMain.SCENE_HEIGHT)) {
            System.out.println("lives before " + myLives.size());
            if (myLives.size() > 0) {
                myRoot.getChildren().remove(myLives.get(myLives.size()-1));
                myLives.remove(myLives.size()-1);
                System.out.println("lives after " + myLives.size());
                resetBall();
            }

            //TODO: Fix this problem of calling reset stage in step
//            if (myLives.size() == 0) {
//                GameMain.resetStage(GameSceneType.LOSE);
//            }
        }
    }

    private void paddleCollisionCheck() {
        if (myBall.getBoundsInParent().intersects(myMainCharacter.getCharacterImageView().getBoundsInParent()) &&
                myBall.getBoundsInParent().getMaxY() <= myMainCharacter.getCharacterImageView().getBoundsInParent().getMinY()) {
            if (myBallDirection[0] == 0) {
                myBallDirection[0] = -1;
            }
            if (myBall.getBoundsInParent().getCenterX() <= myMainCharacter.getCharacterImageView().getBoundsInParent().getCenterX() - Character.THIRD_DIVISION) {
                myBallDirection[0] = -1;
                myBallDirection[1] = -1;
            } else if (myBall.getBoundsInParent().getCenterX() >= myMainCharacter.getCharacterImageView().getBoundsInParent().getCenterX() + Character.THIRD_DIVISION) {

                myBallDirection[0] = 1;
                myBallDirection[1] = -1;
            } else {
                myBallDirection[1] *= -1;
            }
        }
    }

}

