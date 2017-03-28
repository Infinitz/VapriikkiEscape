package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomData {

    public RoomType type;

    private int stars;
    private Texture texture;
    private Texture selectedTex;
    private Texture iconTexture;
    private Texture background;
    public float iconLocalPosX, iconLocalPosY = 0;

    public ArrayList<Riddle> riddles;

    public RoomData(RoomType type, Texture tex, Texture selectedTex, Texture iconTexture,
                    Texture background) {
        this.type = type;
        this.texture = tex;
        this.selectedTex = selectedTex;
        this.iconTexture = iconTexture;
        this.background = background;
        stars = 2; //temporary
        riddles = new ArrayList<Riddle>();
    }

    public Riddle getRandomRiddle() {
        int index = (int)(Math.random() * riddles.size());
        return riddles.get(index);
    }

    public int getStars() {
        return stars;
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
