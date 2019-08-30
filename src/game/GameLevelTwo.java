package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameLevelTwo implements GameLevel {
    public static final Paint SCENE_BACKGROUND = Color.BLACK;

    private Scene myLevelScene;
    private Scene myNextScene;
    private Group myRoot;
    private ImageView myHarryPotter;

    public GameLevelTwo() {
        setUpScene();
        myLevelScene = new Scene (myRoot, SCENE_WIDTH, SCENE_HEIGHT, SCENE_BACKGROUND);
    }

    public void setNextScene(Scene nextScene) {
        myNextScene = nextScene;
    }

    public Scene getLevelScene() {
        return myLevelScene;
    }

    private void setUpScene() {
        myRoot = new Group();
        Button button = new Button("Go to scene 1");
        button.setOnAction(e -> GameMain.getStage().setScene(myNextScene));
        myRoot.getChildren().add(button);

        myHarryPotter = getHarryPotter();
        myRoot.getChildren().add(myHarryPotter);



    }
}

