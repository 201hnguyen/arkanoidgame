package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameMain extends Application {
    private static Stage myStage;
    private static GameLevel myLevelOne;
    private static GameLevel myLevelTwo;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        myStage = primaryStage;

        myLevelOne = new GameLevelOne();
        myLevelTwo = new GameLevelTwo();

        myLevelOne.setNextScene(myLevelTwo.getLevelScene());
        myLevelTwo.setNextScene(myLevelOne.getLevelScene());

        Scene levelOneScene = myLevelOne.getLevelScene();

        myStage.setScene(levelOneScene);
        myStage.setTitle("Game");
        myStage.show();
    }


    public static Stage getStage() {
        return myStage;
    }
}

