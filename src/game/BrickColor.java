package game;

public enum BrickColor {
    NONE ("transparentbrick.png"),
    YELLOW ("yellowbrick.png"),
    RED ("redbrick.png"),
    PURPLE ("purplebrick.png");

    private String myFileName;
    BrickColor(String fileName) {
        myFileName = fileName;
    }

    public String getFileName() {
        return myFileName;
    }
}
