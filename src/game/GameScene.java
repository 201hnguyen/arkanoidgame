package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class GameScene {
    public static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    public static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    public static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";

    private Scene myScene;
    private GameSceneType myGameSceneType;
    private GameSceneType myNextGameSceneType;
    private Pane myRoot;
    private Ball myBall;
    private Character myMainCharacter;
    private BrickStructure myBrickStructure;
    private GameStatus myGameStatus;
    private ArrayList<Powerup> myPresentPowerups = new ArrayList<>();

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

        myScene.setOnKeyPressed(e -> {
            try {
                handleKeyInput(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void handleKeyInput(KeyEvent e) throws Exception {
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
        }
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
        myBall = new Ball(myRoot, myMainCharacter);
        BackgroundStructure door = new BackgroundStructure(BackgroundStructure.StructureEnum.DOOR);
        door.setStructureAsDoor(myRoot);
        myGameStatus = new GameStatus(myRoot);

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

    private void movePaddleOnMouseInput (double x) {
        myMainCharacter.getCharacterImageView().setX(x);
        if (myMainCharacter.getCharacterImageView().getBoundsInLocal().getMaxX() >= GameMain.SCENE_WIDTH) {
            myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH - myMainCharacter.getCharacterImageView().getFitWidth());
        }
    }

    public void clearLevel(double elapsedTime) throws Exception {
        myScene.setOnMouseMoved(null);
        myRoot.getChildren().remove(myBall.getBallImageView());
        myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH / 2 - myMainCharacter.getCharacterImageView().getBoundsInLocal().getWidth() / 2);
        myMainCharacter.getCharacterImageView().setY(myMainCharacter.getCharacterImageView().getY() - 100 * elapsedTime);
        if (myMainCharacter.getCharacterImageView().getBoundsInParent().getMaxY() == 0) {
            GameMain.resetStage(myNextGameSceneType);
        }
    }

    public void loseLevel() throws Exception {
//            System.out.println("Before stage reset");
            GameMain.resetStage(GameSceneType.LOSE);
    }

    public Scene getScene() {
        return myScene;
    }

    public BrickStructure getBrickStructure() {
        return myBrickStructure;
    }

    public Pane getRoot() {
        return myRoot;
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

    public void setPowerup(Brick brickWithPowerup) {
        brickWithPowerup.getPowerup().revealPowerup(brickWithPowerup, myRoot);
        myPresentPowerups.add(brickWithPowerup.getPowerup());
        brickWithPowerup.getPowerup().setPowerBrickCoordinate(brickWithPowerup.getBrickImageView().getBoundsInParent().getCenterX(),
                brickWithPowerup.getBrickImageView().getBoundsInParent().getMinY());
    }

    public ArrayList<Powerup> getPresentPowerups() {
        return myPresentPowerups;
    }

    public GameSceneType getGameSceneType() {
        return myGameSceneType;
    }
}

