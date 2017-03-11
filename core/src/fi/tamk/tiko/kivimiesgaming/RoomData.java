package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomData {

    public RoomType type;

    private int stars;
    private Texture texture;
    private Texture selectedTex;

    public RoomData(RoomType type, Texture tex, Texture selectedTex, int stars) {
        this.type = type;
        this.texture = tex;
        this.selectedTex = selectedTex;
        this.stars = stars;
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

    public RoomType getType() {
        return type;
    }
}
