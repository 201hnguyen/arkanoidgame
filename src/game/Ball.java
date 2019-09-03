//package game;
//
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.Pane;
//
//public class Ball {
//    public static final String BALL_IMAGE = "ball.png";
//    public static final int BALL_SIZE = 30;
//    public static final int BALL_SPEED = 500;
//
//
//    private ImageView myBallImageView;
//
//    public Ball(Pane root, Character paddle) {
//        Image ballImage =  new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
//        myBallImageView = new ImageView(ballImage);
//        myBallImageView.setFitHeight(BALL_SIZE);
//        myBallImageView.setFitWidth(BALL_SIZE);
//        resetBall(paddle);
//        root.getChildren().add(myBallImageView);
//    }
//
//
//
//    public ImageView getBallImageView() {
//        return myBallImageView;
//    }
//
//    public void resetBall(Character paddle) {
//        this.getBallImageView().setX(paddle.getCharacterImageView().getBoundsInParent().getCenterX() - BALL_SIZE / 2);
//        this.getBallImageView().setY(paddle.getCharacterImageView().getBoundsInParent().getMinY() - BALL_SIZE);
//        myBallDirection[0] = 1;
//        myBallDirection[1] = 1;
//    }
//}
