package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class Level {
    private Stage myStage;
    private Scene myScene;
    private Character myMainCharacter;
    private Level myNextLevel;
    private Ball myBall;
    private Group myRoot;
    private ArrayList<Brick> myBricks = new ArrayList<>();
    int[] myBallDirection = {1,-1};


    public Level(Stage stage, String bricksConfigPath, Paint background) throws Exception {
        myStage = stage;
        myScene = setupScene(bricksConfigPath, background);
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

    public Group getRoot() {
        return myRoot;
    }

    public ArrayList<Brick> getBricks() {
        return myBricks;
    }

    public void setNextLevel(Level nextLevel) {
        myNextLevel = nextLevel;
    }

    private Scene setupScene(String bricksConfigPath, Paint background) throws Exception {
        myRoot = new Group();
        setupBricksConfig(bricksConfigPath, myRoot);

        myMainCharacter = new Character(Character.CharacterEnum.HARRY_POTTER);
        myMainCharacter.setCharacterAsPaddle(myRoot);

        myBall = new Ball();
        myBall.resetBall(myMainCharacter);
        myBall.addBallToScreen(myRoot);
        Structure door = new Structure(Structure.StructureEnum.DOOR);
        door.setDoor(myRoot);

        Button button = new Button("Go to next scene"); //TODO: delete this later; only here for easier navigation now
        button.setOnAction(e -> GameMain.resetStage(myNextLevel));
        myRoot.getChildren().add(button);

        Scene scene = new Scene(myRoot, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT, background);
        scene.setOnMouseMoved(e -> handleMouseInput(e.getX()));
        return scene;
    }

    private void setupBricksConfig(String path, Group root) throws Exception {
        File bricksConfig = new File(path);
        Scanner scanner = new Scanner(bricksConfig);
        int yCoordinateForRow = 0;

        while (scanner.hasNextLine()) {
            String rowConfiguration = scanner.nextLine();
            yCoordinateForRow += Brick.BRICK_HEIGHT;
            setRow(root, rowConfiguration, yCoordinateForRow);
        }
        scanner.close();
    }

    private void setRow(Group root, String rowConfiguration, int yCoordinateForRow) {
        int[] rowConfigurationArray = Stream.of(rowConfiguration.split(" " )).mapToInt(Integer::parseInt).toArray();
        int xCoordinateForBrick = 0;
        for (int i=0; i<rowConfigurationArray.length; i++) {
            if (rowConfigurationArray[i] != 0) {
                Brick brick = new Brick(rowConfigurationArray[i]);
                brick.getBrickImageView().setX(xCoordinateForBrick);
                brick.getBrickImageView().setY(yCoordinateForRow);
                myBricks.add(brick);
                root.getChildren().add(brick.getBrickImageView());
            }
            xCoordinateForBrick += Brick.BRICK_WIDTH;
        }
    }

    private void handleMouseInput(double x) {
        //TODO: Set bounds so character can't go out of bounds of window
            myMainCharacter.getCharacterImageView().setX(x);
            if (myMainCharacter.getCharacterImageView().getBoundsInLocal().getMaxX() >= GameMain.SCENE_WIDTH) {
                myMainCharacter.getCharacterImageView().setX(GameMain.SCENE_WIDTH - myMainCharacter.getCharacterImageView().getFitWidth());
            }
    }
}
