package game;

public enum GameSceneType {
    INTRO ("backgroundintro.jpg"),
    LEVEL1 ("background1.jpg"),
    LEVEL2 ("background2.jpg"),
    LEVEL3 ("background3.jpg"),
    LOSE ("backgroundlose.jpg"),
    WIN ("backgroundwin.jpg");


    private String myBackground;


    GameSceneType(String background) {
        myBackground = background;
    }

    public String getBackground() {
        return myBackground;
    }
}
