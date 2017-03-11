package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomButton extends SelectableButton {

    private RoomData roomData;

    public RoomButton(RoomData data) {
        this(data, data.getTexture().getHeight());
    }

    public RoomButton(RoomData data, float size) {
        super(data.getTexture(), data.getSelectedTex(), size);
        roomData = data;
    }

    public RoomData getRoomData() {
        return roomData;
    }
}
