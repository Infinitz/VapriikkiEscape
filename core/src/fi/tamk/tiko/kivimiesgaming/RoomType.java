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

    public String asString() {
        return this.text;
    }

    public RoomType typeFromString(String enumAsString) {
        return valueOf(enumAsString);
    }
}
