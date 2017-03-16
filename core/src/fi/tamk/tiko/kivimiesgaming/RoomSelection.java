package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 04-Mar-17.
 */

public class RoomSelection extends MyScreen {

    private Group floor1;
    private Group floor2;

    private SelectableButton changeFloorButton;
    private SelectableButton timeMachineButton;
    private ImageActor bg;
    private RoomPopUp roomPopUp;
    private RoomButton selected;
    private OpenMenuButton burgerButton;

    public RoomSelection(Vescape game) {
        super(game);
        bg = new ImageActor(new Texture("MENU_bg.png"),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        stage.addActor(bg);

        bg.addAction(Actions.scaleBy(0.25f, 0.25f,
                1.75f, Interpolation.pow2Out));

        floor1 = createFloor1();
        stage.addActor(floor1);
        createTimeMachineButton();
        floor2 = createFloor2();
        floor2.setPosition(floor2.getX(), floor2.getY() + Vescape.GUI_VIEWPORT_HEIGHT);
        stage.addActor(floor2);
        createChangeFloorButton();
        burgerButton = new OpenMenuButton(this);
    }

    public void createChangeFloorButton() {
        changeFloorButton = new SelectableButton(new Texture("F1.png"),
                new Texture("F2.png"),
                175);

        changeFloorButton.setPosition(
                Vescape.GUI_VIEWPORT_WIDTH / 2 - changeFloorButton.getWidth() / 2,
                50);

        changeFloorButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SelectableButton temp = ((SelectableButton)actor);
                changeFloor(temp.isSelected());
                temp.select();
            }
        });

        stage.addActor(changeFloorButton);
    }

    public void createTimeMachineButton() {
        timeMachineButton = new SelectableButton(new Texture("timeMachine.png"),
                new Texture("timeMachine.png"),
                250);

        timeMachineButton.setPosition(
                Vescape.GUI_VIEWPORT_WIDTH / 2 - timeMachineButton.getWidth() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT / 2 - timeMachineButton.getHeight() / 2);

        stage.addActor(timeMachineButton);
    }

    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            if (roomPopUp != null) {
                roomPopUp.dispose();
                roomPopUp = null;
            } else {
                burgerButton.togglePanel();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new MainMenu(game));
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

        tammerRoom.setPosition(rect.getX(), postalRoom.getY() + postalRoom.getSizeY() * 11/8);

        tutRoom.setPosition(postalRoom.getX() + postalRoom.getSizeX(),
                tammerRoom.getY() * 9 / 8);

        rockRoom.setPosition(postalRoom.getX() - rockRoom.getSizeX() * 1 / 5,
                tutRoom.getY() + tutRoom.getSizeY() * 6 / 5);

        floor1.addActor(postalRoom.getRoomElements());
        floor1.addActor(tammerRoom.getRoomElements());

        floor1.addActor(tutRoom.getRoomElements());
        floor1.addActor(rockRoom.getRoomElements());

        //Set listeners
        postalRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        tammerRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        tutRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        rockRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        return floor1;
    }

    private Group createFloor2() {
        float baseSize = 250;

        float offsetX = 90;
        float offsetY = 300;

        Rectangle rect = new Rectangle(offsetX, offsetY,
                Vescape.GUI_VIEWPORT_WIDTH - 2 * offsetX,
                Vescape.GUI_VIEWPORT_HEIGHT - 2 * offsetY);


        RoomButton postalRoomUP = new RoomButton(game.getRoomData(RoomType.POSTALUP), baseSize * 0.8f);
        RoomButton gameRoom = new RoomButton(game.getRoomData(RoomType.GAME), baseSize * 1.25f);
        RoomButton iceHockeyRoom = new RoomButton(game.getRoomData(RoomType.ICEHOCKEY),
                baseSize * 1);
        RoomButton mediaRoom = new RoomButton(game.getRoomData(RoomType.MEDIA), baseSize * 1);
        RoomButton dollRoom = new RoomButton(game.getRoomData(RoomType.DOLL), baseSize * 1.5f);
        RoomButton natureRoom = new RoomButton(game.getRoomData(RoomType.NATURE), baseSize * 1.5f);

        Group floor2 = new Group();

        postalRoomUP.setPosition(offsetX * 2, rect.getY());

        gameRoom.setPosition(postalRoomUP.getX() + postalRoomUP.getSizeX() * 3/2,
                postalRoomUP.getY() + postalRoomUP.getSizeY() * 1 / 10);

        iceHockeyRoom.setPosition(postalRoomUP.getX() - 75,
                postalRoomUP.getY() + postalRoomUP.getSizeY() * 6 / 4);

        mediaRoom.setPosition(iceHockeyRoom.getX() + iceHockeyRoom.getSizeX() * 10/3,
                gameRoom.getY() + gameRoom.getSizeY() * 5 / 4);

        dollRoom.setPosition(postalRoomUP.getX(),
                iceHockeyRoom.getY() + iceHockeyRoom.getSizeY() * 6 / 4);

        natureRoom.setPosition(gameRoom.getX(),
                mediaRoom.getY() + mediaRoom.getSizeY() * 5 / 4);

        floor2.addActor(postalRoomUP.getRoomElements());
        floor2.addActor(gameRoom.getRoomElements());

        floor2.addActor(iceHockeyRoom.getRoomElements());
        floor2.addActor(mediaRoom.getRoomElements());
        floor2.addActor(dollRoom.getRoomElements());

        floor2.addActor(natureRoom.getRoomElements());

        postalRoomUP.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        gameRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        iceHockeyRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        mediaRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        dollRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        natureRoom.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectRoom(((RoomButton) actor));
            }
        });

        return floor2;
    }

    private void changeFloor(boolean up) {
        int direction = up ? 1 : -1;
        float duration = 0.5f;
        float movement = Vescape.GUI_VIEWPORT_HEIGHT * direction;

        floor1.addAction(Actions.moveBy(0, movement, duration, Interpolation.pow2));
        floor2.addAction(Actions.moveBy(0, movement, duration, Interpolation.pow2));


        float delta = (bg.getHeight() * bg.getScaleY() - bg.getHeight()) / 2;
        bg.addAction(Actions.moveBy(0,
                direction * (bg.getHeight() * bg.getScaleY() - Vescape.GUI_VIEWPORT_HEIGHT - delta),
                duration, Interpolation.pow2));

        selectRoom(null);
    }

    protected void selectRoom(RoomButton room) {

        if (selected != null) {
            selected.setSelected(false);
        }
        selected = room;
        if (selected != null) {
            selected.setSelected(true);
            roomPopUp = new RoomPopUp(this, room.getRoomData());
        }
    }

    protected void toRoomScene() {
        //new RoomScreen(selected);

    }
}
