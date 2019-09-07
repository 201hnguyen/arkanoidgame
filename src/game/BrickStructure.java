package game;

import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

public class BrickStructure {
    public static final int FIRST_ROW_OFFSET = 50;

    private Pane myRoot;
//    private ArrayList<Brick> myBricks = new ArrayList<>();
    private HashMap<Brick, Integer> myBricks = new HashMap();

    public BrickStructure(String bricksConfigurationPath, Pane root) {
        myRoot = root;
        setupBricksConfig(bricksConfigurationPath);
    }

    private void setupBricksConfig(String path) {
        File bricksConfig = new File(path);

        try {
            Scanner scanner = new Scanner(bricksConfig);

            int yCoordinateForRow = FIRST_ROW_OFFSET;

            while (scanner.hasNextLine()) {
                String rowConfiguration = scanner.nextLine();
                setRow(rowConfiguration, yCoordinateForRow);
                yCoordinateForRow += Brick.BRICK_HEIGHT;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception");
        }
    }

    private void setRow(String rowConfiguration, int yCoordinateForRow) {
        int[] rowConfigurationArray = Stream.of(rowConfiguration.split(" " )).mapToInt(Integer::parseInt).toArray();
        int xCoordinateForBrick = 0;
        int row = 0;
        for (int i=0; i<rowConfigurationArray.length; i++) {
            if (rowConfigurationArray[i] != 0) {
                Brick brick = new Brick(rowConfigurationArray[i]);
                brick.getBrickImageView().setX(xCoordinateForBrick);
                brick.getBrickImageView().setY(yCoordinateForRow);
                myBricks.put(brick, row);
                myRoot.getChildren().add(brick.getBrickImageView());
            }
            row++;
            xCoordinateForBrick += Brick.BRICK_WIDTH;
        }
    }

    public void reconfigureBricksBasedOnHits(GameScene gameScene) {
        Brick brickToDownsize = null;
        double[] brickToDownsizeCoordinates = {0,0};
        for (Brick brick : myBricks.keySet()) {
            if (gameScene.getBall().getBallImageView().getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                brickToDownsize = brick;
            }
        }
        if (brickToDownsize != null) {
            brickToDownsizeCoordinates[0] = brickToDownsize.getBrickImageView().getBoundsInParent().getCenterX();
            brickToDownsizeCoordinates[1] = brickToDownsize.getBrickImageView().getBoundsInParent().getCenterY();
            reflectBall(brickToDownsize, gameScene.getBall());
            downsizeBrick(brickToDownsize, gameScene);
        }
    }

    private void reflectBall(Brick brickToDownsize, Ball ball) {
        boolean ballComesFromTop = ball.getBallImageView().getBoundsInParent().getCenterY() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinY();
        boolean ballComesFromBottom = ball.getBallImageView().getBoundsInParent().getCenterY() >= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxY();
        boolean ballComesFromLeft = ball.getBallImageView().getBoundsInParent().getCenterX() <= brickToDownsize.getBrickImageView().getBoundsInParent().getMinX();
        boolean ballComesFromRight = ball.getBallImageView().getBoundsInParent().getCenterX() >= brickToDownsize.getBrickImageView().getBoundsInParent().getMaxX();

        if (ballComesFromTop && ballComesFromLeft || ballComesFromTop && ballComesFromRight ||
                ballComesFromBottom && ballComesFromLeft || ballComesFromBottom && ballComesFromRight) {
            ball.setXDirection(ball.getXDirection() * -1);
            ball.setYDirection(ball.getYDirection() * -1);
        } else if (ballComesFromBottom || ballComesFromTop) {
            ball.setYDirection(ball.getYDirection() * -1);
        } else if (ballComesFromLeft || ballComesFromRight) {
            ball.setXDirection(ball.getXDirection() * -1);
        }
    }

    private void downsizeBrick(Brick brickToDownsize, GameScene gameScene) {
        brickToDownsize.decreaseHitsRemaining();
        if (brickToDownsize.getHitsRemaining() == 0) {
            gameScene.getRoot().getChildren().remove(brickToDownsize.getBrickImageView());
            myBricks.remove(brickToDownsize);

            if (brickToDownsize.getBrickId() == 4 || brickToDownsize.getBrickId() == 5 || brickToDownsize.getBrickId() == 6) {
                gameScene.setPowerup(brickToDownsize);
            }
        }
    }

    public int getBricksRemaining() {
        return myBricks.size();
    }

    public HashMap<Brick, Integer> getBricks() {
        return myBricks;
    }

    public void removeBrick(Brick brick) {
        myBricks.remove(brick);
    }

}
