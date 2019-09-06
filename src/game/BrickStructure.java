package game;

import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class BrickStructure {
    public static final int FIRST_ROW_OFFSET = 50;

    private Pane myRoot;
    private ArrayList<Brick> myBricks = new ArrayList<>();

    public BrickStructure(String bricksConfigurationPath, Pane root) throws Exception {
        myRoot = root;
        setupBricksConfig(bricksConfigurationPath);
    }

    private void setupBricksConfig(String path) throws Exception {
        File bricksConfig = new File(path);
        Scanner scanner = new Scanner(bricksConfig);
        int yCoordinateForRow = FIRST_ROW_OFFSET;

        while (scanner.hasNextLine()) {
            String rowConfiguration = scanner.nextLine();
            setRow(rowConfiguration, yCoordinateForRow);
            yCoordinateForRow += Brick.BRICK_HEIGHT;
        }
        scanner.close();
    }

    private void setRow(String rowConfiguration, int yCoordinateForRow) {
        int[] rowConfigurationArray = Stream.of(rowConfiguration.split(" " )).mapToInt(Integer::parseInt).toArray();
        int xCoordinateForBrick = 0;
        for (int i=0; i<rowConfigurationArray.length; i++) {
            if (rowConfigurationArray[i] != 0) {
                Brick brick = new Brick(rowConfigurationArray[i]);
                brick.getBrickImageView().setX(xCoordinateForBrick);
                brick.getBrickImageView().setY(yCoordinateForRow);
                myBricks.add(brick);
                myRoot.getChildren().add(brick.getBrickImageView());
            }
            xCoordinateForBrick += Brick.BRICK_WIDTH;
        }
    }

    public void  reconfigureBricksBasedOnHits(Ball ball) {
        Brick brickToDownsize = null;
        for (Brick brick : myBricks) {
            if (ball.getBallImageView().getBoundsInParent().intersects(brick.getBrickImageView().getBoundsInParent())) {
                brickToDownsize = brick;
            }
        }
        if (brickToDownsize != null) {
            reflectBall(brickToDownsize, ball);
            downsizeBrick(brickToDownsize);
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

    private void downsizeBrick(Brick brickToDownsize) {
        brickToDownsize.decreaseHitsRemaining();
        if (brickToDownsize.getHitsRemaining() == 0) {
            myRoot.getChildren().remove(brickToDownsize.getBrickImageView());
            myBricks.remove(brickToDownsize);
        }
    }

    public int getBricksRemaining() {
        return myBricks.size();
    }

}
