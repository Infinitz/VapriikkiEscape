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
 * Created by risto on 3.4.2017.
 */

public class RoomFinishedPopUp {
    private ImageActor screenDarkener;
    private ImageActor panelBG;
    private Group elements;
    private Texture continueButtonTexture;
    private Texture replayButtonTexture;

    public RoomFinishedPopUp(final MyScreen screen, final RoomData data) {
        continueButtonTexture = new Texture("riddle_next_active.png");
        replayButtonTexture = new Texture("riddle_retry_active.png");

        screenDarkener = new ImageActor(new Texture("black.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        screenDarkener.alpha = 0.85f;

        elements = new Group();


        panelBG = new ImageActor(new Texture("map_room_info_box.png"));
        panelBG.alpha = 1f;

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        panelBG.setSize(panelBG.getSizeY() * (panelTargetW / panelBG.getSizeX()));
        panelBG.setPosition((Vescape.GUI_VIEWPORT_WIDTH - panelBG.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - panelBG.getSizeY()) / 2);

        Label.LabelStyle labelStyle = new Label.LabelStyle(screen.getGame().getFontBig(),
                Color.BLACK);
        Label roomName = new Label(
                screen.getGame().getMyBundle().get(data.type.toString()),
                labelStyle);
        roomName.setPosition(panelBG.getX() + (panelBG.getSizeX() - roomName.getWidth()) / 2,
                panelBG.getY() + panelBG.getSizeY() - roomName.getHeight() - 50);


        ImageActor roomIcon = new ImageActor(data.getIconTexture(), 350);
        roomIcon.setPosition(panelBG.getX() + (panelBG.getSizeX() - roomIcon.getSizeX()) / 2,
                roomName.getY() - roomIcon.getSizeY() - 150);

        ImageActor continueButton;
        ImageActor replayButton;

        continueButton = new ImageActor(continueButtonTexture, 150);

        continueButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.getGame().setScreen(new RoomSelection(screen.getGame()));
            }
        });

        replayButton = new ImageActor(replayButtonTexture, 150);

        replayButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.getGame().setScreen(new RoomView(screen.getGame(), data));
            }
        });

        float offsetX = 15f;
        float offsetY = 15f;
        float buttonW = (panelBG.getSizeX() - 3 * offsetX) / 2;
        float buttonH = 150f;
        replayButton.setPosition(panelBG.getX() + offsetX,
                panelBG.getY() + offsetY);
        replayButton.setWidth(buttonW);
        replayButton.setHeight(buttonH);

        continueButton.setPosition(panelBG.getX() + 2 * offsetX + buttonW,
                panelBG.getY() + offsetY);
        continueButton.setWidth(buttonW);
        continueButton.setHeight(buttonH);

        elements.addActor(panelBG);
        elements.addActor(replayButton);
        elements.addActor(continueButton);
        elements.addActor(roomName);
        elements.addActor(roomIcon);


        Vescape.setGroupOrigin(elements,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);

        int stars = data.latestScore;

        Stars s = new Stars(roomIcon.getX() + roomIcon.getSizeX() / 2,
                roomIcon.getY(), 2.5f, stars, true);
        s.addStarsToGroup(elements);

        elements.setScaleY(0);
        elements.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.pow2));

        screen.getStage().addActor(screenDarkener);
        screenDarkener.setTouchable(Touchable.enabled);
        screen.getStage().addActor(elements);
    }

    public void dispose() {
        elements.addAction(Actions.sequence(
                Actions.scaleTo(1, 0, 0.3f, Interpolation.pow2),
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
