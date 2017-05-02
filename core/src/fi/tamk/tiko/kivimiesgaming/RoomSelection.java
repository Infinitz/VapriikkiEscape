package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

/**
 * Created by atter on 04-Mar-17.
 */

public class RoomSelection extends MyScreen {

    private Group floor1;
    private Group floor2;

    private ArrayList<RoomButton> roomButtons;

    private GameProgressBar progressBar;
    private ImageActor changeFloorButtonUp;
    private ImageActor changeFloorButtonDown;
    private boolean isDownStairs = true;
    private SelectableButton timeMachineButton;
    private ImageActor bg;
    private RoomPopUp roomPopUp;
    private RoomButton selected;
    private BurgerButton burgerButton;
    private ArrayList<String> loadedShipMachineTextures;

    private int totalStars = 0;

    public RoomSelection(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
        assetManager.load("floor_navi_up.png", Texture.class);
        assetManager.load("floor_navi_down.png", Texture.class);
        assetManager.load("F2_postal.png", Texture.class);
        assetManager.load("map_lock.png", Texture.class);
        assetManager.load("map_lock_unlocked.png", Texture.class);
        assetManager.load("proggbar_back.png", Texture.class);
        assetManager.load("proggbar_border.png", Texture.class);
        assetManager.load("proggbar_fill.png", Texture.class);
        assetManager.load("proggbar_marker.png", Texture.class);

        assetManager.load("unlock_lock.wav", Sound.class);

        totalStars = 0;
        for (RoomType t : RoomType.values()) {
            game.getRoomData(t).loadTextures();
            totalStars += game.getRoomData(t).highscore;
        }

        Vescape.lastTotalStars = totalStars;

        for (RoomType t : RoomType.values()) {
            boolean locked = totalStars < game.getRoomData(t).starsToUnlock;
            boolean unlockAnimation = game.getRoomData(t).isLocked != locked;
            game.getRoomData(t).isLocked = locked;
            game.getRoomData(t).unlockAnimation = unlockAnimation;
        }

        //Load space part textures
        loadedShipMachineTextures = new ArrayList<String>();
        for (int i = 0; i < game.getMachineParts().length; ++i) {
            if (!game.getMachineParts()[i].unlocked || i == game.getMachineParts().length - 1) {

                loadedShipMachineTextures.add(game.getMachineParts()[i].getPartImagePath());
                loadedShipMachineTextures.add(game.getMachineParts()[i].getShipImagePath());

                if (i > 0) {
                    loadedShipMachineTextures.add(game.getMachineParts()[i - 1].getShipImagePath());
                } else {
                    loadedShipMachineTextures.add("time_machine_parts/time_machine_0.png");
                }
                break;
            }
        }

        for (int i = 0; i < loadedShipMachineTextures.size(); ++i) {
            assetManager.load(loadedShipMachineTextures.get(i), Texture.class);
        }
    }

    @Override
    protected void onAssetsLoaded() {
        bg = new ImageActor(assetManager.get("MENU_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        stage.addActor(bg);

        progressBar = new GameProgressBar(0, 0, 140, getAssetManager(), getGame());
        progressBar.setX(Vescape.GUI_VIEWPORT_WIDTH / 2 - progressBar.getWidth() / 2);
        progressBar.setY(Vescape.GUI_VIEWPORT_HEIGHT - progressBar.getHeight() - 50);

        roomButtons = new ArrayList<RoomButton>();
        floor1 = createFloor1();
        stage.addActor(floor1);
        createTimeMachineButton();
        floor2 = createFloor2();
        floor2.setPosition(floor2.getX(), floor2.getY() + Vescape.GUI_VIEWPORT_HEIGHT);
        stage.addActor(floor2);
        createChangeFloorButtons();
        stage.addActor(progressBar.getGroup());
        burgerButton = new BurgerButton(this);



        Vescape.setGroupOrigin(floor1,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);
        Vescape.setGroupOrigin(floor2,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);




        InputProcessor inputProcessorOne = new SimpleDirectionGestureDetector(
                new SimpleDirectionGestureDetector.DirectionListener() {

                    @Override
                    public void onUp() {
                        if (!isDownStairs) {
                            changeFloor(true);
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
                        if (isDownStairs) {
                            changeFloor(false);
                        }
                    }
                });
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputProcessorOne);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);


        if (!newMachinePart()) {
            transition();
        }
    }

    public void createChangeFloorButtons() {
        changeFloorButtonUp = new ImageActor(assetManager.get("floor_navi_up.png",
                Texture.class),
                150);

        changeFloorButtonDown = new ImageActor(assetManager.get("floor_navi_down.png",
                Texture.class),
                150);

        changeFloorButtonUp.setPosition(
                2 * Vescape.GUI_VIEWPORT_WIDTH / 3 - changeFloorButtonUp.getWidth() / 2,
                50);

        changeFloorButtonDown.setPosition(
                Vescape.GUI_VIEWPORT_WIDTH / 3 - changeFloorButtonDown.getWidth() / 2,
                30);

        changeFloorButtonDown.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isDownStairs) {
                    changeFloor(true);
                }
            }
        });

        changeFloorButtonDown.alpha = 0.5f;
        changeFloorButtonDown.setTouchable(Touchable.disabled);

        changeFloorButtonUp.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isDownStairs) {
                    changeFloor(false);
                }
            }
        });

        stage.addActor(changeFloorButtonDown);
        stage.addActor(changeFloorButtonUp);
    }

    public void createTimeMachineButton() {
        String timeMachineTexPath = "";
        for (int i = 0; i < game.getMachineParts().length; ++i) {
            if (!game.getMachineParts()[i].unlocked) {
                if (i > 0) {
                    timeMachineTexPath = game.getMachineParts()[i - 1].getShipImagePath();
                } else {
                    timeMachineTexPath = "time_machine_parts/time_machine_0.png";
                }
                break;
            } else if (i == game.getMachineParts().length - 1) {
                timeMachineTexPath = game.getMachineParts()[i].getShipImagePath();
            }
        }

        timeMachineButton = new SelectableButton(
                assetManager.get(timeMachineTexPath, Texture.class),
                assetManager.get(timeMachineTexPath, Texture.class),
                200);


        timeMachineButton.setPosition(
                Vescape.GUI_VIEWPORT_WIDTH / 2 - timeMachineButton.getWidth() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT / 2 - timeMachineButton.getHeight() / 2);

        stage.addActor(timeMachineButton);
    }

    @Override
    protected void update(float dt) {
        if (!assetsLoaded)
            return;

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

    private void transition() {
        float animationDelay = 0.25f;
        float animDuration = 1f;

        floor1.setScale(0f);
        floor2.setScale(0f);
        timeMachineButton.setScale(0f);

        bg.addAction(Actions.sequence(
                Actions.delay(animationDelay),
                Actions.scaleBy(0.25f, 0.25f,
                        animDuration, Interpolation.pow2Out)));

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
    }

    //Check if new machinepart is awarded
    private boolean newMachinePart() {
        for (int i = 0; i < game.getMachineParts().length; ++i) {
            if (totalStars < game.getMachineParts()[i].getStarsToUnlock()) {
                break;
            }

            if (!game.getMachineParts()[i].unlocked) {
                final ScreenDarkener screenDarkener = new ScreenDarkener(
                        assetManager.get("black.png", Texture.class));
                stage.addActor(screenDarkener);
                screenDarkener.enable(true);

                final MachinePart machinePart = game.getMachineParts()[i];
                final ImageActor partActor = new ImageActor(
                        assetManager.get(machinePart.getPartImagePath(), Texture.class),
                        150);
                partActor.originCenter = false;
                partActor.setPosition(2 * Vescape.GUI_VIEWPORT_WIDTH / 3 - partActor.getSizeX() / 2,
                        Vescape.GUI_VIEWPORT_HEIGHT / 4 - partActor.getSizeY() / 2);
                timeMachineButton.remove();

                stage.addActor(timeMachineButton);
                stage.addActor(partActor);
                timeMachineButton.originCenter = false;
                timeMachineButton.addAction(Actions.sequence(
                        Actions.scaleTo(2, 2, 0.5f, Interpolation.pow2),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                screenDarkener.setClickListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        //Part To Machine Actions
                                        partActor.addAction(Actions.sequence(
                                                Actions.parallel(
                                                        Actions.moveTo(
                                                                timeMachineButton.getX() +
                                                                        timeMachineButton.getTrueX() *
                                                                                machinePart.offsetX * 2,
                                                                timeMachineButton.getY() +
                                                                        timeMachineButton.getTrueY() *
                                                                                machinePart.offsetY * 2,
                                                                1.25f, Interpolation.pow2),
                                                        Actions.scaleTo(0.9f, 0.9f, 1f, Interpolation.pow2)
                                                ),
                                                Actions.run(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        partActor.remove();

                                                        timeMachineButton.setTex(
                                                                assetManager.get(machinePart.getShipImagePath(),
                                                                        Texture.class));

                                                        timeMachineButton.addAction(Actions.scaleTo(1, 1, 0.5f));
                                                        screenDarkener.enable(false);
                                                        burgerButton.reAddElementsToStage();
                                                        for (int i = 0; i < roomButtons.size(); ++i) {
                                                            roomButtons.get(i).unlockAnimation();
                                                        }
                                                    }
                                                })
                                        ));

                                    }
                                });
                            }
                        })

                ));

                game.getMachineParts()[i].unlocked = true;
                return true;
            }
        }
        return false;
    }

    private Group createFloor1() {

        float offsetX = 100;
        float offsetY = 300;
        Rectangle rect = new Rectangle(offsetX, offsetY,
                Vescape.GUI_VIEWPORT_WIDTH - 2 * offsetX,
                Vescape.GUI_VIEWPORT_HEIGHT - 2 * offsetY);

        RoomData data;
        float baseSize = 250;

        data = game.getRoomData(RoomType.POSTAL);
        RoomButton postalRoom = new RoomButton(
                data, assetManager, baseSize * 1);

        data = game.getRoomData(RoomType.TAMMER);
        RoomButton tammerRoom = new RoomButton(
                data, assetManager, baseSize * 1.15f);

        data = game.getRoomData(RoomType.TUTORIAL);
        RoomButton tutRoom = new RoomButton(
                data, assetManager, baseSize * 1);

        data = game.getRoomData(RoomType.ROCK);
        RoomButton rockRoom = new RoomButton(
                data, assetManager, baseSize * 1);

        Group floor1 = new Group();

        roomButtons.add(postalRoom);
        roomButtons.add(tammerRoom);
        roomButtons.add(tutRoom);
        roomButtons.add(rockRoom);

        postalRoom.setPosition(rect.getWidth() / 2 + rect.getX() - postalRoom.getSizeX() / 2,
                rect.getY());

        tammerRoom.setPosition(rect.getX(), postalRoom.getY() + postalRoom.getSizeY() * 11 / 8);

        tutRoom.setPosition(postalRoom.getX() + postalRoom.getSizeX(),
                tammerRoom.getY() * 9 / 8);

        rockRoom.setPosition(postalRoom.getX() - rockRoom.getSizeX() * 1 / 5,
                tutRoom.getY() + tutRoom.getSizeY() * 6 / 5);

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

        floor1.addActor(postalRoom.getRoomElements());
        floor1.addActor(tammerRoom.getRoomElements());

        floor1.addActor(tutRoom.getRoomElements());
        floor1.addActor(rockRoom.getRoomElements());

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

        RoomData data;
        data = game.getRoomData(RoomType.GAME);
        RoomButton gameRoom = new RoomButton(
                data, assetManager, baseSize * 1.25f);

        data = game.getRoomData(RoomType.ICEHOCKEY);
        RoomButton iceHockeyRoom = new RoomButton(
                data, assetManager, baseSize * 1);

        data = game.getRoomData(RoomType.MEDIA);
        RoomButton mediaRoom = new RoomButton(
                data, assetManager, baseSize * 1);

        data = game.getRoomData(RoomType.DOLL);
        RoomButton dollRoom = new RoomButton(
                data, assetManager, baseSize * 1.5f);

        data = game.getRoomData(RoomType.NATURE);
        RoomButton natureRoom = new RoomButton(
                data, assetManager, baseSize * 1.5f);

        Group floor2 = new Group();

        roomButtons.add(gameRoom);
        roomButtons.add(iceHockeyRoom);
        roomButtons.add(mediaRoom);
        roomButtons.add(dollRoom);
        roomButtons.add(natureRoom);

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

        // floor2.addActor(postalRoomUP);
        floor2.addActor(gameRoom.getRoomElements());

        floor2.addActor(iceHockeyRoom.getRoomElements());
        floor2.addActor(mediaRoom.getRoomElements());
        floor2.addActor(dollRoom.getRoomElements());

        floor2.addActor(natureRoom.getRoomElements());

        return floor2;
    }

    private void changeFloor(boolean down) {
        isDownStairs = down;
        int direction = down ? 1 : -1;
        float duration = 0.5f;
        final float movement = Vescape.GUI_VIEWPORT_HEIGHT * direction;

        floor1.addAction(Actions.moveBy(0, movement, duration, Interpolation.pow2));
        floor2.addAction(Actions.moveBy(0, movement, duration, Interpolation.pow2));

        timeMachineButton.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, movement, duration, Interpolation.pow2),
                        Actions.scaleTo(0, 0, duration),
                        Actions.rotateBy(180, duration)
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        timeMachineButton.moveBy(0, -movement);
                    }
                }),
                Actions.parallel(
                        Actions.scaleTo(1, 1, duration / 2, Interpolation.pow2),
                        Actions.rotateBy(180, duration / 3, Interpolation.pow2))
        ));

        float delta = (bg.getHeight() * bg.getScaleY() - bg.getHeight()) / 2;
        bg.addAction(Actions.moveBy(0,
                direction * (bg.getHeight() * bg.getScaleY() - Vescape.GUI_VIEWPORT_HEIGHT - delta),
                duration, Interpolation.pow2));

        if (down) {
            changeFloorButtonDown.alpha = 0.5f;
            changeFloorButtonUp.alpha = 1f;
            changeFloorButtonDown.setTouchable(Touchable.disabled);
            changeFloorButtonUp.setTouchable(Touchable.enabled);
        } else {
            changeFloorButtonUp.alpha = 0.5f;
            changeFloorButtonDown.alpha = 1f;
            changeFloorButtonDown.setTouchable(Touchable.enabled);
            changeFloorButtonUp.setTouchable(Touchable.disabled);
        }

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
                AudioManager.playSound("button_press.wav");
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
                AudioManager.playSound("button_press.wav");
                getGame().setScreen(new MainMenu(getGame(), assetManager));
            }
        });

        return button;
    }



    @Override
    public void dispose() {
        assetManager.unload("floor_navi_up.png");
        assetManager.unload("floor_navi_down.png");

        assetManager.unload("map_lock.png");
        assetManager.unload("map_lock_unlocked.png");
        assetManager.unload("unlock_lock.wav");

        for (int i = 0; i < loadedShipMachineTextures.size(); ++i) {
            assetManager.unload(loadedShipMachineTextures.get(i));
        }
        for (RoomType t : RoomType.values()) {
            game.getRoomData(t).unloadTextures();
        }
        super.dispose();
    }
}
