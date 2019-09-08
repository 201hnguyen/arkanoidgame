package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Optional;

public class BackgroundStructure {

    public static final int DOOR_HEIGHT = Brick.BRICK_HEIGHT;
    public static final int TEETH_WIDTH = 60;
    public static final int TEETH_HEIGHT = 100;
    public static final int RIGHT_WALL_X = GameMain.SCENE_WIDTH - TEETH_WIDTH;

    private ImageView myStructureImageView;

    public BackgroundStructure(StructureType structure, Pane root, Optional<Integer> x, Optional<Integer> y, Optional<Integer> rotation) {
        Image structureImage = new Image(this.getClass().getClassLoader().getResourceAsStream(structure.getStructureFileName()));
        myStructureImageView = new ImageView(structureImage);
        if (structure == StructureType.DOOR) {
            setStructureAsDoor(root);
        } else if (structure == StructureType.TEETH) {
            setStructureAsTeeth(root, x.get(), y.get(), rotation.get());
        }
    }

    private void setStructureAsDoor(Pane root) {
        myStructureImageView.setFitHeight(DOOR_HEIGHT);
        myStructureImageView.setX(GameMain.SCENE_WIDTH / 2 - myStructureImageView.getBoundsInLocal().getWidth() / 2);
        root.getChildren().add(myStructureImageView);
    }

    private void setStructureAsTeeth(Pane root, int x, int y, int rotation) {
        myStructureImageView.setFitHeight(TEETH_HEIGHT);
        myStructureImageView.setFitWidth(TEETH_WIDTH);
        myStructureImageView.setX(x);
        myStructureImageView.setY(y);
        myStructureImageView.setRotate(rotation);
        root.getChildren().add(this.getStructureImageView());
    }

    public static ArrayList<BackgroundStructure> addDeadZonesToLevel(GameScene.GameSceneType gameSceneType, Pane root) {
        ArrayList<BackgroundStructure> deadZones = new ArrayList<>();
        if (gameSceneType == GameScene.GameSceneType.LEVEL1) {
            deadZones.add(new BackgroundStructure(StructureType.TEETH, root, Optional.of(RIGHT_WALL_X), Optional.of(200), Optional.of(0)));
        } else if (gameSceneType == GameScene.GameSceneType.LEVEL2) {
            deadZones.add(new BackgroundStructure(StructureType.TEETH, root, Optional.of(RIGHT_WALL_X), Optional.of(200), Optional.of(0)));
            deadZones.add(new BackgroundStructure(StructureType.TEETH, root, Optional.of(0), Optional.of(450), Optional.of(180)));
        } else if (gameSceneType == GameScene.GameSceneType.LEVEL3) {
            deadZones.add(new BackgroundStructure(StructureType.TEETH, root, Optional.of(RIGHT_WALL_X), Optional.of(200), Optional.of(0)));
            deadZones.add(new BackgroundStructure(StructureType.TEETH, root, Optional.of(0), Optional.of(450), Optional.of(180)));
            deadZones.add(new BackgroundStructure(StructureType.TEETH, root, Optional.of(RIGHT_WALL_X), Optional.of(200), Optional.of(0)));
            deadZones.add(new BackgroundStructure(StructureType.TEETH, root, Optional.of(0), Optional.of(100), Optional.of(180)));
        }
        return deadZones;
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
