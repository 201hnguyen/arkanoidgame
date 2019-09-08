package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class BackgroundStructure {

    public static final int DOOR_HEIGHT = Brick.BRICK_HEIGHT;

    private ImageView myStructureImageView;

    public BackgroundStructure(StructureType structure, Pane root) {
        Image structureImage = new Image(this.getClass().getClassLoader().getResourceAsStream(structure.getStructureFileName()));
        myStructureImageView = new ImageView(structureImage);
        if (structure == StructureType.DOOR) {
            setStructureAsDoor(root);
        }
    }

    public void setStructureAsDoor(Pane root) {
        this.getStructureImageView().setFitHeight(DOOR_HEIGHT);
        this.getStructureImageView().setX(GameMain.SCENE_WIDTH / 2 - this.getStructureImageView().getBoundsInLocal().getWidth() / 2);
        root.getChildren().add(this.getStructureImageView());
    }

    public ImageView getStructureImageView() {
        return myStructureImageView;
    }

    public enum StructureType {
        DOOR ("door.png");
        private String myFileName;
        StructureType(String fileName) {
            myFileName = fileName;
        }
        public String getStructureFileName() {
            return myFileName;
        }
    }
}
