package game;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Character {
    public static final int CHARACTER_HEIGHT = 150;
    public static final int CHARACTER_WIDTH = 200;

    private ImageView myCharacterImageView;

    public Character(CharacterEnum characterName) {
        Image characterImage = new Image(this.getClass().getClassLoader().getResourceAsStream(characterName.getCharacterFileName()));
        myCharacterImageView = new ImageView(characterImage);
        myCharacterImageView.setFitWidth(CHARACTER_WIDTH);
        myCharacterImageView.setFitHeight(CHARACTER_HEIGHT);
    }

    public void setCharacterAsPaddle(Group root) {
        this.getCharacterImageView().setX(GameMain.SCENE_WIDTH / 2 - this.getCharacterImageView().getBoundsInLocal().getWidth() / 2);
        this.getCharacterImageView().setY(GameMain.SCENE_HEIGHT - this.getCharacterImageView().getBoundsInLocal().getHeight());
        root.getChildren().add(this.getCharacterImageView());
    }

    public ImageView getCharacterImageView() {
        return myCharacterImageView;
    }

    public enum CharacterEnum {
        HARRY_POTTER ("harrypotter.png");

        private String myAssociatedFileName;
        CharacterEnum(String fileName) {
            myAssociatedFileName = fileName;
        }

        public String getCharacterFileName() {
            return myAssociatedFileName;
        }
    }


}
