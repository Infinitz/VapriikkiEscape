package fi.tamk.tiko.kivimiesgaming;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 */

public class MachinePart {
    public boolean unlocked;

    private int starsToUnlock;
    private String partImagePath;
    private String shipImagePath;
    public float offsetX, offsetY;

    public MachinePart(int starsToUnlock, int totalStars, String partImagePath,
                       String shipImagePath, float offsetX, float offsetY) {
        this.starsToUnlock = starsToUnlock;
        this.unlocked = starsToUnlock <= totalStars;
        this.partImagePath = partImagePath;
        this.shipImagePath = shipImagePath;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public String getShipImagePath() {
        return shipImagePath;
    }

    public String getPartImagePath() {
        return partImagePath;
    }

    public int getStarsToUnlock() {
        return starsToUnlock;
    }
}
