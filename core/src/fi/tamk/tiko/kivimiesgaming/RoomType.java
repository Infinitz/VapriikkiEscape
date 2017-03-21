package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 11-Mar-17.
 */

public enum RoomType {
    ROCK, POSTAL, TAMMER, TUTORIAL, GAME, MEDIA, NATURE, DOLL, ICEHOCKEY;

    public static String asString(RoomType type) {
        switch (type) {
            case ROCK:
                return "rockMuseum";
            case POSTAL:
                return "postalMuseum";
            case TAMMER:
                return "tammerMuseum";
            case TUTORIAL:
                return "tutorialMuseum";
            case GAME:
                return "gameMuseum";
            case MEDIA:
                return "mediaMuseum";
            case NATURE:
                return "natureMuseum";
            case DOLL:
                return "dollMuseum";
            case ICEHOCKEY:
                return "iceHockeyMuseum";
        }

        return "";
    }
}
