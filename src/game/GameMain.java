package game;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameMain extends Application {
    public static final Paint LEVEL_ONE_BACKGROUND = Color.DARKGRAY.darker().darker();
    public static final Paint LEVEL_TWO_BACKGROUND = Color.SADDLEBROWN.darker().darker().darker().darker();
    public static final Paint LEVEL_THREE_BACKGROUND = Color.DARKBLUE.darker().darker();
    public static final String LEVEL_ONE_BRICKS_CONFIG_PATH = "resources/level1.txt";
    public static final String LEVEL_TWO_BRICKS_CONFIG_PATH = "resources/level2.txt";
    public static final String LEVEL_THREE_BRICKS_CONFIG_PATH = "resources/level3.txt";
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 800;
    public static final int BRICK_WIDTH = 100;
    public static final int BRICK_HEIGHT = 50;

    private static Stage myStage;
    private static Scene myLevelOneScene;
    private static Scene myLevelTwoScene;
    private static Scene myLevelThreeScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        myStage = stage;
        myLevelOneScene = setupLevelOne();
        myLevelTwoScene = setupLevelTwo();
        myLevelThreeScene = setupLevelThree();

        stage.setScene(myLevelOneScene);
        stage.setTitle("Game");
        stage.show();
    }

    private Scene setupLevelOne() throws Exception {
        Group levelOneRoot = new Group();
        setupBricksConfig(LEVEL_ONE_BRICKS_CONFIG_PATH, levelOneRoot);
        setupGameElements(levelOneRoot);

        Button button = new Button("Go to scene 2");
        button.setOnAction(e -> myStage.setScene(myLevelTwoScene));
        levelOneRoot.getChildren().add(button);

        Scene levelOneScene = new Scene(levelOneRoot, SCENE_WIDTH, SCENE_HEIGHT, LEVEL_ONE_BACKGROUND);
        return levelOneScene;
    }

    private Scene setupLevelTwo() throws Exception{
        Group levelTwoRoot = new Group();

        setupBricksConfig(LEVEL_TWO_BRICKS_CONFIG_PATH, levelTwoRoot);
        setupGameElements(levelTwoRoot);

        Button button = new Button("Go to scene 3");
        button.setOnAction(e -> myStage.setScene(myLevelThreeScene));
        levelTwoRoot.getChildren().add(button);

        Scene levelTwoScene = new Scene(levelTwoRoot, SCENE_WIDTH, SCENE_HEIGHT, LEVEL_TWO_BACKGROUND);
        return levelTwoScene;
    }

    private Scene setupLevelThree() throws Exception {
        Group levelThreeRoot = new Group();

        setupBricksConfig(LEVEL_THREE_BRICKS_CONFIG_PATH, levelThreeRoot);
        setupGameElements(levelThreeRoot);

        Button button = new Button("Go to scene 1");
        button.setOnAction(e -> myStage.setScene(myLevelOneScene));
        levelThreeRoot.getChildren().add(button);

        Scene levelThreeScene = new Scene(levelThreeRoot, SCENE_WIDTH, SCENE_HEIGHT, LEVEL_THREE_BACKGROUND);
        return levelThreeScene;
    }

    private void setupBricksConfig(String path, Group root) throws Exception {
        File bricksConfig = new File(path);
        Scanner scanner = new Scanner(bricksConfig);
        int yCoordinateForRow = 0;

        while (scanner.hasNextLine()) {
            String rowConfiguration = scanner.nextLine();
            yCoordinateForRow += BRICK_HEIGHT;
            setRow(root, rowConfiguration, yCoordinateForRow);
        }
        scanner.close();
    }

    private void setRow(Group root, String rowConfiguration, int yCoordinateForRow) {
        int[] rowConfigurationArray = Stream.of(rowConfiguration.split(" " )).mapToInt(Integer::parseInt).toArray();
        int xCoordinateForBrick = 0;
        for (int i=0; i<rowConfigurationArray.length; i++) {
            Brick brick = new Brick(rowConfigurationArray[i]);
            brick.getBricksImageView().setX(xCoordinateForBrick);
            brick.getBricksImageView().setY(yCoordinateForRow);
            xCoordinateForBrick += BRICK_WIDTH;
            root.getChildren().add(brick.getBricksImageView());
        }
    }

    private void setupGameElements(Group root) {
        Character harryPotter = new Character(Character.CharacterEnum.HARRY_POTTER);
        harryPotter.setCharacterAsPaddle(root);
        Ball mainBall = new Ball();
        mainBall.resetBall(harryPotter);
        mainBall.addBallToScreen(root);
        Structure door = new Structure(Structure.StructureEnum.DOOR);
        door.setDoor(root);
    }

}

