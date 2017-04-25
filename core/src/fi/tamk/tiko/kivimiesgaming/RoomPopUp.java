package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 16-Mar-17.
 */

public class RoomPopUp {

    private ScreenDarkener screenDarkener;
    private ImageActor panelBG;
    private Group elements;

    public RoomPopUp(final MyScreen screen, final RoomData data) {
        screenDarkener =
                new ScreenDarkener(screen.getAssetManager().get("black.png", Texture.class));
        screenDarkener.enable(true);
        elements = new Group();


        panelBG = new ImageActor(
                screen.getAssetManager().get("map_room_info_box.png", Texture.class));
        panelBG.alpha = 1f;

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        panelBG.setSize(panelBG.getSizeY() * (panelTargetW / panelBG.getSizeX()));
        panelBG.setPosition((Vescape.GUI_VIEWPORT_WIDTH - panelBG.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - panelBG.getSizeY()) / 2);

        Label.LabelStyle labelStyle = new Label.LabelStyle(screen.getGame().getFontBig(),
                new Color(1f, 1f, 1f, 0.9f));
        String roomNameString = Utilities.splitTextIntoLines(
                screen.getGame().getMyBundle().get(data.type.toString()).toUpperCase(), 19);
        Label roomName = new Label(roomNameString, labelStyle);
        roomName.setPosition(panelBG.getX() + (panelBG.getSizeX() - roomName.getWidth()) / 2,
                panelBG.getY() + panelBG.getSizeY() - roomName.getHeight() - 50);


        ImageActor roomIcon = new ImageActor(data.getIconTexture(), 350);
        roomIcon.setTouchable(Touchable.disabled);
        roomIcon.setPosition(panelBG.getX() + (panelBG.getSizeX() - roomIcon.getSizeX()) / 2,
                panelBG.getY() + panelBG.getSizeY() - roomIcon.getSizeY() - 275);


        TextButton enterRoomButton;
        TextButton closeButton;

        enterRoomButton = new TextButton(
                screen.getGame().getMyBundle().get("enterRoomButton"),
                screen.getGame().getTextButtonStyle());

        enterRoomButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                screen.getGame().setScreen(new RoomView(
                        screen.getGame(), data, screen.getAssetManager()));
            }
        });

        closeButton = new TextButton(screen.getGame().getMyBundle().get("closeButton"),
                screen.getGame().getTextButtonStyle());

        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                ((RoomSelection)screen).selectRoom(null);
            }
        });

        screenDarkener.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((RoomSelection) screen).selectRoom(null);
            }
        });

        float offsetX = 15f;
        float offsetY = 15f;
        float buttonW = (panelBG.getSizeX() - 3 * offsetX) / 2;
        float buttonH = 150f;
        closeButton.setPosition(panelBG.getX() + offsetX,
                panelBG.getY() + offsetY);
        closeButton.setWidth(buttonW);
        closeButton.setHeight(buttonH);

        enterRoomButton.setPosition(panelBG.getX() + 2 * offsetX + buttonW,
                panelBG.getY() + offsetY);
        enterRoomButton.setWidth(buttonW);
        enterRoomButton.setHeight(buttonH);

        elements.addActor(panelBG);
        elements.addActor(closeButton);
        elements.addActor(enterRoomButton);
        elements.addActor(roomName);
        elements.addActor(roomIcon);


        Vescape.setGroupOrigin(elements,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);

        int stars = data.highscore;

        Stars s = new Stars(roomIcon.getX() + roomIcon.getSizeX() / 2,
                roomIcon.getY(), 2.5f, stars, false, screen.assetManager);
        s.addStarsToGroup(elements);

        elements.setScaleY(0);
        elements.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.pow2));

        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(elements);

        panelBG.setTouchable(Touchable.enabled);

        AudioManager.playSound("panel_open.wav");
    }

    public void dispose() {
        AudioManager.playSound("panel_close.wav");
        elements.addAction(Actions.sequence(
                Actions.parallel(
                Actions.scaleTo(1, 0, 0.3f, Interpolation.pow2),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                screenDarkener.enable(false);
                            }
                        })),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        screenDarkener.remove();
                        elements.remove();
                    }
                })
        ));
    }
}