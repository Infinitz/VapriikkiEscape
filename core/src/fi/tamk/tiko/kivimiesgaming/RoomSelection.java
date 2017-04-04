package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private BurgerButton burgerButton;

    public RoomSelection(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
        assetManager.load("F1.png", Texture.class);
        assetManager.load("F2.png", Texture.class);
        assetManager.load("F2_postal.png", Texture.class);
        assetManager.load("map_timeMachine.png", Texture.class);

        for (RoomType t : RoomType.values()) {
            game.getRoomData(t).loadTextures();
        }
    }

    @Override
    protected void onAssetsLoaded() {
        bg = new ImageActor(assetManager.get("MENU_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        stage.addActor(bg);

        floor1 = createFloor1();
        stage.addActor(floor1);
        createTimeMachineButton();
        floor2 = createFloor2();
        floor2.setPosition(floor2.getX(), floor2.getY() + Vescape.GUI_VIEWPORT_HEIGHT);
        stage.addActor(floor2);
        createChangeFloorButton();
        burgerButton = new BurgerButton(this);

        floor1.setScale(0f);
        floor2.setScale(0f);
        timeMachineButton.setScale(0f);

        float animationDelay = 0.25f;
        float animDuration = 1.5f;
        bg.addAction(Actions.sequence(
                Actions.delay(animationDelay),
                Actions.scaleBy(0.25f, 0.25f,
                        animDuration, Interpolation.pow2Out)));

        Vescape.setGroupOrigin(floor1,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);
        Vescape.setGroupOrigin(floor2,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);


        floor1.addAction(Actions.sequence(
                Actions.delay(animationDelay),
                Actions.scaleTo(1f, 1f,
                        animDuration, Interpolation.pow2Out)));
        floor2.addAction(Actions.sequence(
                Actions.delay(animationDelay),
                Actions.scaleTo(1f, 1f,
                        animDuration, Interpolation.pow2Out)));
        timeMachineButton.addAction(Actions.sequence(
                Actions.delay(animationDelay),
                Actions.scaleTo(1f, 1f,
                        animDuration, Interpolation.pow2Out)));

        InputProcessor inputProcessorOne = new SimpleDirectionGestureDetector(
                new SimpleDirectionGestureDetector.DirectionListener() {

                    @Override
                    public void onUp() {
                        if (changeFloorButton.isSelected() && roomPopUp == null ) {
                            changeFloor(true);
                            changeFloorButton.select();
                        }
                    }

                    @Override
                    public void onRight() {

                    }

                    @Override
                    public void onLeft() {

                    }

                    @Override
                    public void onDown() {
                        if (!changeFloorButton.isSelected() && roomPopUp == null) {
                            changeFloor(false);
                            changeFloorButton.select();
                        }
                    }
                });
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputProcessorOne);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    public void createChangeFloorButton() {
        changeFloorButton = new SelectableButton(assetManager.get("F1.png", Texture.class),
                assetManager.get("F2.png", Texture.class), 175);

        changeFloorButton.setPosition(
                Vescape.GUI_VIEWPORT_WIDTH / 2 - changeFloorButton.getWidth() / 2,
                50);

        changeFloorButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SelectableButton temp = ((SelectableButton) actor);
                changeFloor(temp.isSelected());
                temp.select();
            }
        });

        stage.addActor(changeFloorButton);
    }

    public void createTimeMachineButton() {
        timeMachineButton = new SelectableButton(
                assetManager.get("map_timeMachine.png", Texture.class),
                assetManager.get("map_timeMachine.png", Texture.class),
                200);


        timeMachineButton.setPosition(
                Vescape.GUI_VIEWPORT_WIDTH / 2 - timeMachineButton.getWidth() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT / 2 - timeMachineButton.getHeight() / 2);

        stage.addActor(timeMachineButton);
    }

    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            if (selected != null) {
                selectRoom(null);
            } else {
                burgerButton.togglePanel();
            }

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new MainMenu(game, assetManager));
        }
    }

    private Group createFloor1() {

        float offsetX = 100;
        float offsetY = 300;
        Rectangle rect = new Rectangle(offsetX, offsetY,
                Vescape.GUI_VIEWPORT_WIDTH - 2 * offsetX,
                Vescape.GUI_VIEWPORT_HEIGHT - 2 * offsetY);

        float baseSize = 250;
        RoomButton postalRoom = new RoomButton(
                game.getRoomData(RoomType.POSTAL), assetManager, baseSize * 1);
        RoomButton tammerRoom = new RoomButton(
                game.getRoomData(RoomType.TAMMER), assetManager, baseSize * 1.15f);
        RoomButton tutRoom = new RoomButton(
                game.getRoomData(RoomType.TUTORIAL), assetManager, baseSize * 1);
        RoomButton rockRoom = new RoomButton(
                game.getRoomData(RoomType.ROCK), assetManager, baseSize * 1);

        Group floor1 = new Group();

        postalRoom.setPosition(rect.getWidth() / 2 + rect.getX() - postalRoom.getSizeX() / 2,
                rect.getY());

        tammerRoom.setPosition(rect.getX(), postalRoom.getY() + postalRoom.getSizeY() * 11 / 8);

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


        ImageActor postalRoomUP = new ImageActor(assetManager.get("F2_postal.png", Texture.class),
                baseSize * 0.8f);
        postalRoomUP.setTouchable(Touchable.disabled);

        RoomButton gameRoom = new RoomButton(
                game.getRoomData(RoomType.GAME), assetManager, baseSize * 1.25f);
        RoomButton iceHockeyRoom = new RoomButton(
                game.getRoomData(RoomType.ICEHOCKEY), assetManager, baseSize * 1);
        RoomButton mediaRoom = new RoomButton(
                game.getRoomData(RoomType.MEDIA), assetManager, baseSize * 1);
        RoomButton dollRoom = new RoomButton(
                game.getRoomData(RoomType.DOLL), assetManager, baseSize * 1.5f);
        RoomButton natureRoom = new RoomButton(
                game.getRoomData(RoomType.NATURE), assetManager, baseSize * 1.5f);

        Group floor2 = new Group();

        postalRoomUP.setPosition(offsetX * 2, rect.getY());

        gameRoom.setPosition(postalRoomUP.getX() + postalRoomUP.getSizeX() * 3 / 2,
                postalRoomUP.getY() + postalRoomUP.getSizeY() * 1 / 10);

        iceHockeyRoom.setPosition(postalRoomUP.getX() - 75,
                postalRoomUP.getY() + postalRoomUP.getSizeY() * 6 / 4);

        mediaRoom.setPosition(iceHockeyRoom.getX() + iceHockeyRoom.getSizeX() * 10 / 3,
                gameRoom.getY() + gameRoom.getSizeY() * 5 / 4);

        dollRoom.setPosition(postalRoomUP.getX(),
                iceHockeyRoom.getY() + iceHockeyRoom.getSizeY() * 6 / 4);

        natureRoom.setPosition(gameRoom.getX(),
                mediaRoom.getY() + mediaRoom.getSizeY() * 5 / 4);

       // floor2.addActor(postalRoomUP);
        floor2.addActor(gameRoom.getRoomElements());

        floor2.addActor(iceHockeyRoom.getRoomElements());
        floor2.addActor(mediaRoom.getRoomElements());
        floor2.addActor(dollRoom.getRoomElements());

        floor2.addActor(natureRoom.getRoomElements());

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
            roomPopUp.dispose();
            roomPopUp = null;
        }
        selected = room;
        if (selected != null) {
            selected.setSelected(true);
            roomPopUp = new RoomPopUp(this, room.getRoomData());
        }
    }

    @Override
    public TextButton getPanelButton1() {
        String name = getGame().getMyBundle().get("continueButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                burgerButton.togglePanel();
            }
        });

        return button;
    }

    @Override
    public TextButton getPanelButton2() {
        String name = getGame().getMyBundle().get("toMainMenuButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().setScreen(new MainMenu(getGame(), assetManager));
            }
        });

        return button;
    }



    @Override
    public void dispose() {
        assetManager.unload("F1.png");
        assetManager.unload("F2.png");
        assetManager.unload("map_timeMachine.png");
        for (RoomType t : RoomType.values()) {
            game.getRoomData(t).unloadTextures();
        }
        super.dispose();
    }
}
