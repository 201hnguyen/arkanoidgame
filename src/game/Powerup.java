package game;

public class Powerup {
    public enum PowerupType {
        LIGHTNING ("lightningpower.png"),
        POTION ("potionpower.png"),
        HORN ("hornpower.png");

        private String myAssociatedFileName;
        PowerupType(String fileName) {
            myAssociatedFileName = fileName;
        }

        private String getPowerupFileName() {
            return myAssociatedFileName;
        }
    }
}
