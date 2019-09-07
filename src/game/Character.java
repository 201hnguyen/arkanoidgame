package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Character {
    public static final int CHARACTER_HEIGHT = 150;
    public static final int CHARACTER_WIDTH = 200;
    public static final int DOUBLE_CHARACTER_WIDTH = 350;
    public static final int THIRD_DIVISION = 50;

    private ImageView myCurrentCharacterImageView;
    private ImageView mySingleCharacterImageView;
    private ImageView myDoubleCharacterImageView;


    public Character(CharacterEnum characterName) {
        Image characterImage = new Image(this.getClass().getClassLoader().getResourceAsStream(characterName.getCharacterFileName()));
        mySingleCharacterImageView = new ImageView(characterImage);
        mySingleCharacterImageView.setFitWidth(CHARACTER_WIDTH);
        mySingleCharacterImageView.setFitHeight(CHARACTER_HEIGHT);

        Image doubleCharacterImage = new Image(this.getClass().getClassLoader().getResourceAsStream(CharacterEnum.DOUBLE_CHARACTER.getCharacterFileName()));
        myDoubleCharacterImageView = new ImageView(doubleCharacterImage);
        myDoubleCharacterImageView.setFitWidth(DOUBLE_CHARACTER_WIDTH);
        myDoubleCharacterImageView.setFitHeight(CHARACTER_HEIGHT);
    }

    public void setCharacterAsPaddle(Pane root) {
        myCurrentCharacterImageView = mySingleCharacterImageView;
        myCurrentCharacterImageView.setX(GameMain.SCENE_WIDTH / 2 - CHARACTER_WIDTH / 2);
        myCurrentCharacterImageView.setY(GameMain.SCENE_HEIGHT - CHARACTER_HEIGHT);
        root.getChildren().add(myCurrentCharacterImageView);
    }

    public void changeCharacter(ImageView desiredCharacterImageView, Pane root) {
        double currentX = myCurrentCharacterImageView.getX();
        double currentY = myCurrentCharacterImageView.getY();
        root.getChildren().remove(myCurrentCharacterImageView);
        myCurrentCharacterImageView = desiredCharacterImageView;
        myCurrentCharacterImageView.setX(currentX);
        myCurrentCharacterImageView.setY(currentY);
        root.getChildren().add(myCurrentCharacterImageView);
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

    public enum CharacterEnum {
        HARRY_POTTER ("harrypotter.png"),
        DOUBLE_CHARACTER ("doublecharacter.png");

        private String myAssociatedFileName;
        CharacterEnum(String fileName) {
            myAssociatedFileName = fileName;
        }

        public String getCharacterFileName() {
            return myAssociatedFileName;
        }
    }

}
