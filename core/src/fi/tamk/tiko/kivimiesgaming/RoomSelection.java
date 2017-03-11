package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

/**
 * Created by atter on 04-Mar-17.
 */

public class RoomSelection extends MyScreen {

    private Group floor1;
    private Group floor2;

    private RoomButton selected;

    public RoomSelection(Vescape game) {
        super(game);

        ImageActor bg = new ImageActor(new Texture("MENU_bg.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);
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
                Vescape.GUI_VIEWPORT_WIDTH - 2 * offsetX,
                Vescape.GUI_VIEWPORT_HEIGHT - 2 * offsetY);

        float baseSize = 250;
        RoomButton postalRoom = new RoomButton(game.getRoomData(RoomType.POSTAL), baseSize * 1);
        RoomButton tammerRoom = new RoomButton(game.getRoomData(RoomType.TAMMER), baseSize * 1.15f);
        RoomButton tutRoom = new RoomButton(game.getRoomData(RoomType.TUTORIAL), baseSize * 1);
        RoomButton rockRoom = new RoomButton(game.getRoomData(RoomType.ROCK), baseSize * 1);

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

        //Set listeners
        postalRoom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("LOL");
                selectRoom(((RoomButton)actor));
            }
        });

        tammerRoom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("LOL");
                selectRoom(((RoomButton)actor));
            }
        });

        tutRoom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("LOL");
                selectRoom(((RoomButton)actor));
            }
        });

        rockRoom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("LOL");
                selectRoom(((RoomButton)actor));
            }
        });

        return floor1;
    }

    private Group createFloor2() {
        float baseSize = 200;
        ImageActor postalRoomUP = new RoomButton(game.getRoomData(RoomType.POSTALUP), baseSize * 0.8f);
        ImageActor gameRoom = new RoomButton(game.getRoomData(RoomType.GAME), baseSize * 1);
        ImageActor iceHockeyRoom = new RoomButton(game.getRoomData(RoomType.ICEHOCKEY),
                baseSize * 1);
        ImageActor mediaRoom = new RoomButton(game.getRoomData(RoomType.MEDIA), baseSize * 1);
        ImageActor dollRoom = new RoomButton(game.getRoomData(RoomType.DOLL), baseSize * 1);
        ImageActor natureRoom = new RoomButton(game.getRoomData(RoomType.NATURE), baseSize * 1);

        Group floor2 = new Group();

        float offsetX = 100;
        postalRoomUP.setPosition(offsetX, 0);

        gameRoom.setPosition(postalRoomUP.getX() + postalRoomUP.getSizeX() * 8 / 5,
                postalRoomUP.getSizeY() * 1 / 10);

        iceHockeyRoom.setPosition(0, postalRoomUP.getSizeY() * 7 / 5);

        mediaRoom.setPosition(iceHockeyRoom.getSizeX() * 5 / 2, gameRoom.getSizeY() * 5 / 4);

        dollRoom.setPosition(iceHockeyRoom.getSizeX() + mediaRoom.getSizeX(), postalRoomUP.getSizeY() * 3);

        natureRoom.setPosition(offsetX, postalRoomUP.getSizeY() + iceHockeyRoom.getSizeY() * 3);

        floor2.addActor(postalRoomUP);
        floor2.addActor(gameRoom);

        floor2.addActor(iceHockeyRoom);
        floor2.addActor(mediaRoom);
        floor2.addActor(dollRoom);

        floor2.addActor(natureRoom);

        return floor2;
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

    protected void selectRoom(RoomButton room) {
        if (selected == null)
            return;

        selected.setSelected(false);
        selected = room;
        selected.setSelected(true);
    }

    protected void toRoomScene() {
        //new RoomScreen(selected);
    }
}
