package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomButton extends SelectableButton {

    private RoomData roomData;
    private ImageActor roomIcon;
    private Group roomElements;

    public RoomButton(RoomData data) {
        this(data, data.getTexture().getHeight());
    }

    public RoomButton(RoomData data, float size) {
        super(data.getTexture(), data.getSelectedTex(), size);
        roomData = data;
        roomIcon = new ImageActor(data.getIconTexture(), data.getIconTexture().getHeight());

        roomIcon.setTouchable(Touchable.disabled);

        roomElements = new Group();
        roomElements.addActor(this);
        roomElements.addActor(roomIcon);
    }

    public Group getRoomElements() {
        roomIcon.setPosition(getX() + roomData.iconLocalPosX * getSizeX(),
                getY() + roomData.iconLocalPosY  * getSizeY());
        return roomElements;
    }

    public RoomData getRoomData() {
        return roomData;
    }
}
