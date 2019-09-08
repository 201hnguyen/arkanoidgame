package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Character {
    public static final int CHARACTER_HEIGHT = 150;
    public static final int SINGLE_CHARACTER_WIDTH = 200;
    public static final int DOUBLE_CHARACTER_WIDTH = 350;
    public static final int THIRD_DIVISION = 50;
    public static final String SINGLE_CHARACTER_IMAGE_PATH = "charactersingle.png";
    public static final String DOUBLE_CHARACTER_IMAGE_PATH = "characterdouble.png";
    public static final String ARMY_CHARACTER_IMAGE_PATH = "characterarmy.png";

    private ImageView myCurrentCharacterImageView;
    private ImageView mySingleCharacterImageView;
    private ImageView myDoubleCharacterImageView;
    private ImageView myDumbledoresArmyImageView;


    public Character(Pane root) {
        mySingleCharacterImageView = createCharacter(SINGLE_CHARACTER_IMAGE_PATH, SINGLE_CHARACTER_WIDTH);

        myDoubleCharacterImageView = createCharacter(DOUBLE_CHARACTER_IMAGE_PATH, DOUBLE_CHARACTER_WIDTH);

        myDumbledoresArmyImageView = createCharacter(ARMY_CHARACTER_IMAGE_PATH, GameMain.SCENE_WIDTH);

        myCurrentCharacterImageView = mySingleCharacterImageView;
        setCurrentCharacterAsPaddle(root);
    }

    private ImageView createCharacter(String characterImagePath, int characterWidth) {
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(characterImagePath));
        ImageView characterImageView = new ImageView(image);
        characterImageView.setFitWidth(characterWidth);
        characterImageView.setFitHeight(CHARACTER_HEIGHT);
        return characterImageView;
    }

    private void setCurrentCharacterAsPaddle(Pane root) {
        myCurrentCharacterImageView.setX(GameMain.SCENE_WIDTH / 2 - SINGLE_CHARACTER_WIDTH / 2);
        myCurrentCharacterImageView.setY(GameMain.SCENE_HEIGHT - CHARACTER_HEIGHT);
        root.getChildren().add(myCurrentCharacterImageView);
    }

    public void changePosition(int value) {
        myCurrentCharacterImageView.setY(myCurrentCharacterImageView.getY() - value);
        if (myCurrentCharacterImageView.getY() >= GameMain.SCENE_HEIGHT - myCurrentCharacterImageView.getFitHeight()) {
            myCurrentCharacterImageView.setY(GameMain.SCENE_HEIGHT - myCurrentCharacterImageView.getFitHeight());
        }
    }

    public void changeCharacter(ImageView desiredCharacterImageView, Pane root) {
        double x = myCurrentCharacterImageView.getX();
        double y = myCurrentCharacterImageView.getY();
        root.getChildren().remove(myCurrentCharacterImageView);

        myCurrentCharacterImageView = desiredCharacterImageView;
        if (myCurrentCharacterImageView == myDumbledoresArmyImageView) {
            myCurrentCharacterImageView.setX(0);
        } else {
            myCurrentCharacterImageView.setX(x);
        }
        myCurrentCharacterImageView.setY(y);
        root.getChildren().add(myCurrentCharacterImageView);
    }

    public void movePaddleOnMouseInput (double x) {
        myCurrentCharacterImageView.setX(x);
        if (myCurrentCharacterImageView.getBoundsInLocal().getMaxX() >= GameMain.SCENE_WIDTH) {
            myCurrentCharacterImageView.setX(GameMain.SCENE_WIDTH - myCurrentCharacterImageView.getFitWidth());
        }
    }

    public ImageView getCharacterImageView() {
        return myCurrentCharacterImageView;
    }

    public ImageView getSingleCharacterImageView() {
        return mySingleCharacterImageView;
    }

    public ImageView getDoubleCharacterImageView() {
        return myDoubleCharacterImageView;
    }

    public ImageView getDumbledoresArmyImageView() {
        return myDumbledoresArmyImageView;
    }
}
