package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomData {

    public RoomType type;

    public int highscore = 0;
    public int latestScore = 0;
    private Texture texture;
    private Texture selectedTex;
    private Texture iconTexture;
    private Texture background;
    public float iconLocalPosX, iconLocalPosY = 0;

    private boolean[] usedRiddle;
    public ArrayList<Riddle> riddles;


    public RoomData(RoomType type, Texture tex, Texture selectedTex, Texture iconTexture,
                    Texture background) {
        this.type = type;
        this.texture = tex;
        this.selectedTex = selectedTex;
        this.iconTexture = iconTexture;
        this.background = background;
        riddles = new ArrayList<Riddle>();
    }

    public Riddle getRandomRiddle() {
        int index = (int)(Math.random() * riddles.size());
        int tryCount = 0;
        while (usedRiddle[index] && tryCount < 10) {
            index = (int)(Math.random() * riddles.size());
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

    public void loadRiddles() {
        usedRiddle = new boolean[riddles.size()];
        for (int i = 0; i < riddles.size(); ++i) {
            riddles.get(i).load();
            usedRiddle[i] = false;
        }
    }

    public void disposeRiddleImages() {
        for (Riddle r : riddles) {
            r.dispose();
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getSelectedTex() {
        return selectedTex;
    }

    public Texture getIconTexture() {
        return iconTexture;
    }

    public Texture getBackground() {
        return background;
    }

    public RoomType getType() {
        return type;
    }

    public void setIconLocalPosition(float x, float y) {
        iconLocalPosX = x;
        iconLocalPosY = y;
    }
}
