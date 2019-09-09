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

    /**
     * Used to create structures that stay constant throughout the game. Assumes that the image for door,
     * "door.png," and the image for saw teeth, "teeth.png" exists in the resources folder. Thus, these
     * files cannot be removed. For example, this class can be used to create the "door" structure when the
     * level scene just started that allows the Score and Lives to be displayed, as well as allow the character
     * to float through once the level has been cleared; this class can also be used to create the "teeth" that
     * represent the dead zones in the games, of which, if a player hits, they will lose a life, lose points, and their
     * character/paddle will become blurry for the rest of the game.
     * @param structure the type of structure to be created
     * @param xCoordinate the x-coordinate for where the teeth structure should be laid out (leftmost); not needed for door structure
     * @param yCoordinate the y-coordinate for where the teeth structure should be laid out (topmost); not needed for door structure
     * @param rotation the rotation for the teeth structure, depending on which wall it is on; not needed for door structure
     */
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

    /**
     * Gets ImageView for BackgroundStructure, useful for when trying to add this object
     * to the root of the scene.
     * @return ImageView representation of the background structure.
     */
    public ImageView getStructureImageView() {
        return myStructureImageView;
    }

    /**
     * Used to specify what kind of background structure is being created and how it should function;
     * this game only allows for two types of background structures. Also associates the file image
     * with the file name.
     */
    public enum StructureType {
        DOOR ("door.png"),
        TEETH ("teeth.png");

        private String myFileName;

        StructureType(String fileName) {
            myFileName = fileName;
        }

        private String getStructureFileName() {
            return myFileName;
        }
    }
}
