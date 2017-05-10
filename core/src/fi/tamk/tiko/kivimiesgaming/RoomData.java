package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Contains all the data of the room.
 */

public class RoomData {

    /**
     * The room type information.
     */
    public RoomType type;

    /**
     * The current highscore for the room.
     */
    public int highscore = 0;

    /**
     * The latest score which shows in the popup.
     */
    public int latestScore = 0;

    /**
     * How many stars are needed to unlock next part.
     */
    public int starsToUnlock = 0;

    /**
     * Checks if the room is locked.
     */
    public boolean isLocked = true;

    /**
     * Checks if the unlock animation is needed.
     */
    public boolean unlockAnimation = false;

    /**
     * For loading and unloading assets.
     */
    private AssetManager assetManager;

    /**
     * Path for locked texture.
     */
    private String lockedTexPath;

    /**
     * Path for unlocked texture.
     */
    private String unlockedTexPath;

    /**
     * Path for icon texture.
     */
    private String iconTextureP;

    /**
     * Path for background.
     */
    private String backgroundP;

    /**
     * Sets icon positions.
     */
    public float iconLocalPosX, iconLocalPosY = 0;

    /**
     * Array of riddles that are used so they don't show up again.
     */
    private boolean[] usedRiddle;

    /**
     * List of riddles.
     */
    public ArrayList<Riddle> riddles;

    /**
     * Specific riddle for last riddle of the room.
     */
    public Riddle lastRiddle;

    /**
     * Class constructor.
     *
     * @param type          Room type.
     * @param starsToUnlock How many stars to unlock room.
     * @param texP          Texture path.
     * @param selectedTexP  Selected texture path.
     * @param iconTextureP  Icon texture path.
     * @param backgroundP   Background texture path.
     * @param assetManager  For loading and unloading assets.
     */
    public RoomData(RoomType type, int starsToUnlock, String texP,
                    String selectedTexP, String iconTextureP,
                    String backgroundP, AssetManager assetManager) {
        this.starsToUnlock = starsToUnlock;
        this.assetManager = assetManager;
        this.type = type;
        this.lockedTexPath = texP;
        this.unlockedTexPath = selectedTexP;
        this.iconTextureP = iconTextureP;
        this.backgroundP = backgroundP;
        riddles = new ArrayList<Riddle>();
    }

    /**
     * Gets random riddle from the pool as long as it has not been used.
     *
     * @return Returns the riddle.
     */
    public Riddle getRandomRiddle() {
        int index = (int) (Math.random() * riddles.size());
        int tryCount = 0;
        while (usedRiddle[index] && tryCount < 10) {
            index = (int) (Math.random() * riddles.size());
            ++tryCount;
        }
        if (tryCount > 10) {
            boolean unUsedFound = false;
            for (int i = 0; i < riddles.size(); ++i) {
                if (!usedRiddle[i]) {
                    index = i;
                    unUsedFound = true;
                    break;
                }
            }
            if (!unUsedFound) {
                for (int i = 0; i < riddles.size(); ++i) {
                    usedRiddle[i] = false;
                }
                return getRandomRiddle();
            }
        }

        usedRiddle[index] = true;
        return riddles.get(index);
    }

    /**
     * Loads the textures.
     */
    public void loadTextures() {
        assetManager.load(lockedTexPath, Texture.class);
        assetManager.load(unlockedTexPath, Texture.class);
        assetManager.load(iconTextureP, Texture.class);
        assetManager.load(backgroundP, Texture.class);
    }

    /**
     * Unloads the textures.
     */
    public void unloadTextures() {
        assetManager.unload(lockedTexPath);
        assetManager.unload(unlockedTexPath);
        assetManager.unload(iconTextureP);
        assetManager.unload(backgroundP);
    }

    /**
     * Loads the riddles images.
     *
     * @param assets Used for loading.
     */
    public void loadRiddles(AssetManager assets) {
        usedRiddle = new boolean[riddles.size()];
        for (int i = 0; i < riddles.size(); ++i) {
            assets.load(riddles.get(i).imagePath, Texture.class);
            usedRiddle[i] = false;
        }
        if (lastRiddle != null) {
            assets.load(lastRiddle.imagePath, Texture.class);
        }
    }

    /**
     * Unloads riddle images.
     *
     * @param assets Used for unloading.
     */
    public void unloadRiddleImages(AssetManager assets) {
        for (Riddle r : riddles) {
            assets.unload(r.imagePath);
        }
        if (lastRiddle != null) {
            assets.unload(lastRiddle.imagePath);
        }
    }

    /**
     * Getter for locked texture.
     *
     * @return Locked texture.
     */
    public Texture getLockedTexture() {
        return assetManager.get(lockedTexPath, Texture.class);
    }

    /**
     * Getter for unlocked texture.
     *
     * @return Unlocked texture.
     */
    public Texture getUnlockedTexture() {
        return assetManager.get(unlockedTexPath, Texture.class);
    }

    /**
     * Getter for icon texture.
     *
     * @return Icon texture.
     */
    public Texture getIconTexture() {
        return assetManager.get(iconTextureP, Texture.class);
    }

    /**
     * Getter for backgroun texture.
     *
     * @return Background texture.
     */
    public Texture getBackground() {
        return assetManager.get(backgroundP, Texture.class);
    }

    /**
     * Sets icon positions.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public void setIconLocalPosition(float x, float y) {
        iconLocalPosX = x;
        iconLocalPosY = y;
    }
}
