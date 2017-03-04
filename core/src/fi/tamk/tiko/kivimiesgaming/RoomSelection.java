package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by atter on 04-Mar-17.
 */

public class RoomSelection extends MyScreen {



    public RoomSelection(Vescape game) {
        super(game);

        ImageActor bg = new ImageActor(new Texture("englishFlag.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX(-Vescape.GUI_VIEWPORT_WIDTH / 2);
        stage.addActor(bg);

        float size = 200;
        Table roomsF2 = new Table();
        ImageActor room1 = new ImageActor(new Texture("englishFlag.png"), size);
        ImageActor room2 = new ImageActor(new Texture("englishFlag.png"), size);
        ImageActor room3 = new ImageActor(new Texture("englishFlag.png"), size);
        ImageActor room4 = new ImageActor(new Texture("englishFlag.png"), size);
        ImageActor room5 = new ImageActor(new Texture("englishFlag.png"), size);
        ImageActor room6 = new ImageActor(new Texture("englishFlag.png"), size);
        ImageActor room7 = new ImageActor(new Texture("englishFlag.png"), size);

        Group rooms = new Group();

        float offsetX = 100;
        room1.setPosition(offsetX, 0);
        room2.setPosition(room1.getX() + room1.getSizeX(), 0);

        room3.setPosition(0, room1.getSizeY());
        room4.setPosition(room3.getSizeX(), room1.getSizeY());
        room5.setPosition(room3.getSizeX() + room4.getSizeX(), room1.getSizeY());

        room6.setPosition(offsetX, room1.getSizeY() + room3.getSizeY());
        room7.setPosition(room6.getSizeX() + offsetX, room1.getSizeY() + room3.getSizeY());

        rooms.addActor(room1);
        rooms.addActor(room2);

        rooms.addActor(room3);
        rooms.addActor(room4);
        rooms.addActor(room5);

        rooms.addActor(room6);
        rooms.addActor(room7);
        /*
        roomsF2.setFillParent(true);
        roomsF2.add(room1);
        roomsF2.add(room2);
        roomsF2.row();
        roomsF2.add(room3);
        roomsF2.add(room4);
        roomsF2.add(room5);
        roomsF2.row();
        roomsF2.add(room6).padLeft(275);
        roomsF2.add(room7).padLeft(125);
        roomsF2.setDebug(true);
        */

        rooms.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - 3 * room1.getSizeX() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT / 2 - 3 * size / 2);
        stage.addActor(rooms);

        new OpenMenuButton(this);
    }


}
