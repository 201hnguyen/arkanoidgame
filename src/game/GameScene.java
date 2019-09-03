package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameScene {
    private final int FIRST_ROW_OFFSET = 50;
    private Stage myStage;
    private Scene myScene;
    private Character myMainCharacter;
    private GameScene myNextGameScene;
    private Ball myBall;
    private Pane myRoot;
    private ArrayList<Brick> myBricks = new ArrayList<>();


    public GameScene(Stage stage, String bricksConfigPath, String backgroundPath) throws Exception {
        myStage = stage;
        myScene = setupScene(bricksConfigPath, backgroundPath);
    }

    public Scene getScene() {
        return myScene;
    }

    public Character getMainCharacter() {
        return myMainCharacter;
    }

    public Ball getBall() {
        return myBall;
    }

    public Pane getRoot() {
        return myRoot;
    }

    public ArrayList<Brick> getBricks() {
        return myBricks;
    }

    public void setNextGameScene(GameScene nextGameScene) {
        myNextGameScene = nextGameScene;
    }

    private Scene setupScene(String bricksConfigPath, String backgroundPath) throws Exception {
        myRoot = new Pane();
        setupBricksConfig(bricksConfigPath);

        myMainCharacter = new Character(Character.CharacterEnum.HARRY_POTTER);
        myMainCharacter.setCharacterAsPaddle(myRoot);

        myBall = new Ball();
        myBall.resetBall(myMainCharacter);
        myBall.addBallToScreen(myRoot);
        Structure door = new Structure(Structure.StructureEnum.DOOR);
        door.setDoor(myRoot);

        Button button = new Button("Go to next scene"); //TODO: delete this later; only here for easier navigation now
        button.setOnAction(e -> GameMain.resetStage(myNextGameScene));
        myRoot.getChildren().add(button);

        setBackground(backgroundPath);

        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT);
        scene.setOnMouseMoved(e -> handleMouseInput(e.getX()));
        return scene;
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
        for (Brick brick : myBricks) {
            myRoot.getChildren().add(brick.getBrickImageView());
        }
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
            }
            xCoordinateForBrick += Brick.BRICK_WIDTH;
        }
    }

    public void bounceOffBricks() {
        Brick brickToDownsize = null;
        for (Brick brick : myBricks) {
            if (myBall.getBallImageView().getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                brickToDownsize = brick;
            }
        }
        if (brickToDownsize != null) {
            brickToDownsize.decreaseHitsRemaining();
            if (brickToDownsize.getHitsRemaining() == 0) {
                myRoot.getChildren().remove(brickToDownsize.getBrickImageView());
                myBricks.remove(brickToDownsize);
            }
            if (myBall.getBallImageView().getBoundsInParent().getMaxY() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinY()
                    || myBall.getBallImageView().getBoundsInParent().getMinY() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxY()) {
                myBall.getBallDirection()[1] *= -1;
            }
            if (myBall.getBallImageView().getBoundsInParent().getMaxX() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinX()
                    || myBall.getBallImageView().getBoundsInParent().getMinX() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxX()) {
                myBall.getBallDirection()[0] *= -1;
            }
        }
    }

    private void handleMouseInput (double x) {
        myMainCharacter.getCharacterImageView().setX(x);
        if (myMainCharacter.getCharacterImageView().getBoundsInLocal().getMaxX() >= GameMain.SCENE_WIDTH) {
            myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH - myMainCharacter.getCharacterImageView().getFitWidth());
        }
    }
}