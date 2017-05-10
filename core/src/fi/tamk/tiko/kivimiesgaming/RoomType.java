package fi.tamk.tiko.kivimiesgaming;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Contains enums that are used when differentiating the rooms.
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

    /**
     * Saves the text that is given to constructor.
     */
    private String text;

    /**
     * Checks which floor the game is at.
     */
    private boolean bottomFloor;

    /**
     * Class constructor.
     *
     * @param text        Text that is given.
     * @param bottomFloor Checks the floor.
     */
    RoomType(String text, boolean bottomFloor) {
        this.text = text;
        this.bottomFloor = bottomFloor;
    }

    /**
     * Converts to string.
     *
     * @return The string.
     */
    @Override
    public String toString() {
        return this.text;
    }

    /**
     * Checks what floor the game is on.
     *
     * @return True if the game is at first floor.
     */
    public boolean isBottomFloor() {
        return bottomFloor;
    }

    /**
     * Is called when room type is needed.
     *
     * @param enumAsString Contains the enum as string.
     * @return Returns room type.
     */
    public static RoomType typeFromString(String enumAsString) {

        for (RoomType type : values()) {
            if (type.text.equalsIgnoreCase(enumAsString)) {
                return type;
            }
        }

        return null;
    }
}
