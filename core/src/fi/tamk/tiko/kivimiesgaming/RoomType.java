package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 11-Mar-17.
 */

public enum RoomType {
    ROCK("rockMuseum", true),
    POSTAL("postalMuseum", true),
    TAMMER("tammerMuseum", true),
    TUTORIAL("tutorialMuseum", true),
    GAME("gameMuseum", false),
    MEDIA("mediaMuseum", false),
    NATURE("natureMuseum", false),
    DOLL("dollMuseum", false),
    ICEHOCKEY("iceHockeyMuseum", false);

    private String text;
    private boolean bottomFloor;
    RoomType(String text, boolean bottomFloor) {
        this.text = text;
        this.bottomFloor = bottomFloor;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public boolean isBottomFloor() {
        return bottomFloor;
    }

    public static RoomType typeFromString(String enumAsString) {

        for (RoomType type: values()) {
            if (type.text.equalsIgnoreCase(enumAsString)) {
                return type;
            }
        }

        return null;
    }
}
