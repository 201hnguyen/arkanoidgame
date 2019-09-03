package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Structure {

    public static final int DOOR_HEIGHT = Brick.BRICK_HEIGHT - 5;

    private ImageView myStructureImageView;

    public Structure(StructureEnum structure) {
        Image structureImage = new Image(this.getClass().getClassLoader().getResourceAsStream(structure.getStructureFileName()));
        myStructureImageView = new ImageView(structureImage);
    }

    public ImageView getStructureImageView() {
        return myStructureImageView;
    }

    public void setStructureAsDoor(Pane root) {
        this.getStructureImageView().setFitHeight(DOOR_HEIGHT);
        this.getStructureImageView().setX(GameMain.SCENE_WIDTH / 2 - this.getStructureImageView().getBoundsInLocal().getWidth() / 2);
        root.getChildren().add(this.getStructureImageView());
    }

    public enum StructureEnum {
        DOOR ("door.png");

        private String myAssociatedFileName;
        StructureEnum(String fileName) {
            myAssociatedFileName = fileName;
        }

        public String getStructureFileName() {
            return myAssociatedFileName;
        }
    }
}
