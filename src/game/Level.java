package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.util.Scanner;
import java.util.stream.Stream;

public class Level {
    private Stage myStage;
    private Scene myScene;
    private Character myMainCharacter;
    private Level myNextLevel;
    private Ball myBall;

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

    public void setNextLevel(Level nextLevel) {
        myNextLevel = nextLevel;
    }

    private Scene setupScene(String bricksConfigPath, Paint background) throws Exception {
        Group root = new Group();
        setupBricksConfig(bricksConfigPath, root);

        myMainCharacter = new Character(Character.CharacterEnum.HARRY_POTTER);
        myMainCharacter.setCharacterAsPaddle(root);

        myBall = new Ball();
        myBall.resetBall(myMainCharacter);
        myBall.addBallToScreen(root);
        Structure door = new Structure(Structure.StructureEnum.DOOR);
        door.setDoor(root);

        Button button = new Button("Go to next scene"); //TODO: delete this later; only here for easier navigation now
        button.setOnAction(e -> GameMain.resetStage(myNextLevel));
        root.getChildren().add(button);

        Scene scene = new Scene(root, GameMain.SCENE_WIDTH, GameMain.SCENE_HEIGHT, background);
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
            Brick brick = new Brick(rowConfigurationArray[i]);
            brick.getBricksImageView().setX(xCoordinateForBrick);
            brick.getBricksImageView().setY(yCoordinateForRow);
            xCoordinateForBrick += Brick.BRICK_WIDTH;
            root.getChildren().add(brick.getBricksImageView());
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
