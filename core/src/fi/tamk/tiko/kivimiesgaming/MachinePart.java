package fi.tamk.tiko.kivimiesgaming;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for storing data about machine part
 */

public class MachinePart {

    /**
     * Is the machine part unlocked yet
     */
    public boolean unlocked;

    /**
     * How many stars is needed to unlock this part
     */
    private int starsToUnlock;

    /**
     * Path to the part's image
     */
    private String partImagePath;

    /**
     * Path to the ship state image
     */
    private String shipImagePath;

    /**
     * Where the part is located in relation of the ship
     */
    public float offsetX, offsetY;

    /**
     * Constructor of the machine part
     *
     * @param starsToUnlock How many stars is needed to unlock this part
     * @param totalStars Current games total stars earned
     * @param partImagePath Path to the part's image
     * @param shipImagePath Path to the ship state image
     * @param offsetX X component of where the part is located in relation of the ship
     * @param offsetY Y component of where the part is located in relation of the ship
     */
    public MachinePart(int starsToUnlock, int totalStars, String partImagePath,
                       String shipImagePath, float offsetX, float offsetY) {
        this.starsToUnlock = starsToUnlock;
        this.unlocked = starsToUnlock <= totalStars;
        this.partImagePath = partImagePath;
        this.shipImagePath = shipImagePath;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Getter for the shipImagePath
     *
     * @return shipImagePath
     */
    public String getShipImagePath() {
        return shipImagePath;
    }

    /**
     * Getter for the partImagePath
     *
     * @return partImagePath
     */
    public String getPartImagePath() {
        return partImagePath;
    }

    /**
     * Getter for the starsToUnlock
     *
     * @return starsToUnlock
     */
    public int getStarsToUnlock() {
        return starsToUnlock;
    }
}
