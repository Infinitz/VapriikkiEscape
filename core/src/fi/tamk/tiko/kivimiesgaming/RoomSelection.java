package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by atter on 04-Mar-17.
 */

public class RoomSelection extends MyScreen {

    private Group floor1;
    private Group floor2;



    public RoomSelection(Vescape game) {
        super(game);

        ImageActor bg = new ImageActor(new Texture("MENU_bg.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        //bg.setX(-Vescape.GUI_VIEWPORT_WIDTH / 2);
        stage.addActor(bg);

        floor1 = createFloor1();
        floor2 = createFloor2();
        floor2.setPosition(floor2.getX(), floor2.getY() + Vescape.GUI_VIEWPORT_HEIGHT);
        stage.addActor(floor1);
        stage.addActor(floor2);

        new OpenMenuButton(this);
    }

    boolean gaa = false;
    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(new MainMenu(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            changeFloor(gaa);
            gaa = !gaa;
        }
    }

    private Group createFloor1() {

        float offsetX = 100;
        float offsetY = 300;
        Rectangle rect = new Rectangle(offsetX, offsetY,
                Vescape.GUI_VIEWPORT_WIDTH -  2 * offsetX,
                Vescape.GUI_VIEWPORT_HEIGHT -  2 * offsetY);

        float baseSize = 250;
        ImageActor postalRoom = new RoomButton(game.getRoomData(RoomType.POSTAL), baseSize * 1);
        ImageActor tammerRoom = new RoomButton(game.getRoomData(RoomType.TAMMER), baseSize * 1.15f);
        ImageActor tutRoom = new RoomButton(game.getRoomData(RoomType.TUTORIAL), baseSize * 1);
        ImageActor rockRoom = new RoomButton(game.getRoomData(RoomType.ROCK), baseSize * 1);

        Group floor1 = new Group();

        postalRoom.setPosition(rect.getWidth() / 2 + rect.getX() - postalRoom.getSizeX() / 2,
                rect.getY());

        tammerRoom.setPosition(rect.getX(), postalRoom.getY() + postalRoom.getSizeY());

        tutRoom.setPosition(postalRoom.getX() + postalRoom.getSizeX(),
                tammerRoom.getY() + tammerRoom.getSizeY() * 2 / 3);

        rockRoom.setPosition(postalRoom.getX() - rockRoom.getSizeX() * 1 / 5,
                tutRoom.getY() + tutRoom.getSizeY() * 6 / 5);

        floor1.addActor(postalRoom);
        floor1.addActor(tammerRoom);

        floor1.addActor(tutRoom);
        floor1.addActor(rockRoom);

        /*floor1.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - 3 * postalRoom.getSizeX() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT / 2 - 3 * size / 2);*/

        return floor1;
    }

    private Group createFloor2() {
        float baseSize = 200;
        ImageActor postalRoomUP = new RoomButton(game.getRoomData(RoomType.POSTALUP), baseSize * 1);
        ImageActor gameRoom = new RoomButton(game.getRoomData(RoomType.GAME), baseSize * 1);
        ImageActor room3 = new ImageActor(new Texture("englishFlag.png"), baseSize);
        ImageActor room4 = new ImageActor(new Texture("englishFlag.png"), baseSize);
        ImageActor room5 = new ImageActor(new Texture("englishFlag.png"), baseSize);
        ImageActor room6 = new ImageActor(new Texture("englishFlag.png"), baseSize);
        ImageActor room7 = new ImageActor(new Texture("englishFlag.png"), baseSize);

        Group rooms = new Group();

        float offsetX = 100;
        postalRoomUP.setPosition(offsetX, 0);
        gameRoom.setPosition(postalRoomUP.getX() + postalRoomUP.getSizeX(), 0);

        room3.setPosition(0, postalRoomUP.getSizeY());
        room4.setPosition(room3.getSizeX(), postalRoomUP.getSizeY());
        room5.setPosition(room3.getSizeX() + room4.getSizeX(), postalRoomUP.getSizeY());

        room6.setPosition(offsetX, postalRoomUP.getSizeY() + room3.getSizeY());
        room7.setPosition(room6.getSizeX() + offsetX, postalRoomUP.getSizeY() + room3.getSizeY());

        rooms.addActor(postalRoomUP);
        rooms.addActor(gameRoom);

        rooms.addActor(room3);
        rooms.addActor(room4);
        rooms.addActor(room5);

        rooms.addActor(room6);
        rooms.addActor(room7);

        return rooms;
    }

    private void changeFloor(boolean up) {
        int direction = up ? 1 : -1;
        float duration = 0.5f;
        float movement = Vescape.GUI_VIEWPORT_HEIGHT * direction;

        MoveByAction moveF1 = new MoveByAction();
        MoveByAction moveF2 = new MoveByAction();

        moveF1.setAmountY(movement);
        moveF2.setAmountY(movement);

        moveF1.setDuration(duration);
        moveF2.setDuration(duration);

        moveF1.setInterpolation(Interpolation.pow2);
        moveF2.setInterpolation(Interpolation.pow2);
        floor1.addAction(moveF1);
        floor2.addAction(moveF2);
    }
}
