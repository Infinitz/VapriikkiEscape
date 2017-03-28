package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 11-Mar-17.
 */

public enum RoomType {
    ROCK("rockMuseum"),
    POSTAL("postalMuseum"),
    TAMMER("tammerMuseum"),
    TUTORIAL("tutorialMuseum"),
    GAME("gameMuseum"),
    MEDIA("mediaMuseum"),
    NATURE("natureMuseum"),
    DOLL("dollMuseum"),
    ICEHOCKEY("iceHockeyMuseum");

    private String text;

    RoomType(String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return this.text;
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
