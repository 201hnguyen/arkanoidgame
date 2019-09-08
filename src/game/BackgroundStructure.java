package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Optional;

public class BackgroundStructure {

    public static final int DOOR_HEIGHT = Brick.BRICK_HEIGHT;
    public static final int TEETH_WIDTH = 60;
    public static final int TEETH_HEIGHT = 100;
    public static final int RIGHT_WALL_COORDINATE_FOR_TEETH = GameMain.SCENE_WIDTH - TEETH_WIDTH;

    private ImageView myStructureImageView;

    public BackgroundStructure(StructureType structure, Optional<Integer> xCoordinate, Optional<Integer> yCoordinate, Optional<Integer> rotation) {
        Image structureImage = new Image(this.getClass().getClassLoader().getResourceAsStream(structure.getStructureFileName()));
        myStructureImageView = new ImageView(structureImage);
        if (structure == StructureType.DOOR) {
            setStructureAsDoor();
        } else {
            setStructureAsTeeth(xCoordinate.get(), yCoordinate.get(), rotation.get());
        }
    }

    private void setStructureAsDoor() {
        myStructureImageView.setFitHeight(DOOR_HEIGHT);
        myStructureImageView.setX(0);
    }

    private void setStructureAsTeeth(int x, int y, int rotation) {
        myStructureImageView.setFitHeight(TEETH_HEIGHT);
        myStructureImageView.setFitWidth(TEETH_WIDTH);
        myStructureImageView.setX(x);
        myStructureImageView.setY(y);
        myStructureImageView.setRotate(rotation);
    }

    public ImageView getStructureImageView() {
        return myStructureImageView;
    }

    public enum StructureType {
        DOOR ("door.png"),
        TEETH ("teeth.png");

        private String myFileName;

        StructureType(String fileName) {
            myFileName = fileName;
        }

        public String getStructureFileName() {
            return myFileName;
        }
    }
}
