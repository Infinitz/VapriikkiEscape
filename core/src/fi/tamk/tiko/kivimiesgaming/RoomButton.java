package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

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
        roomIcon = new ImageActor(data.getIconTexture(), 200);

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

    @Override
    protected void onSelect(boolean selected) {
        int direction = selected ? 1 : -1;

        if (selected) {
            roomIcon.addAction(Actions.parallel(
                    Actions.moveBy(0, direction * 50, 0.4f, Interpolation.pow2),
                    Actions.scaleTo(1.3f, 1.3f, 0.3f)
            ));
        } else {
            roomIcon.addAction(Actions.parallel(
                    Actions.moveBy(0, direction * 50,
                            0.8f, Interpolation.bounceOut),
                    Actions.scaleTo(1f, 1f, 0.15f)
            ));
        }

    }
}
