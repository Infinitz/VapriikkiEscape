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
    private ScreenDarkener screenDarkener;
    private ImageActor panelBG;
    private Group elements;
    private Texture continueButtonTexture;
    private Texture replayButtonTexture;

    public RoomFinishedPopUp(final MyScreen screen, final RoomData data) {
        continueButtonTexture = screen.getAssetManager().get(
                "riddle_next_active.png", Texture.class);
        replayButtonTexture = screen.getAssetManager().get(
                "riddle_retry_active.png", Texture.class);

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
        roomIcon.setPosition(panelBG.getX() + (panelBG.getSizeX() - roomIcon.getSizeX()) / 2,
                roomName.getY() - roomIcon.getSizeY() - 150);

        ImageActor continueButton;
        ImageActor replayButton;

        continueButton = new ImageActor(continueButtonTexture, 150);

        continueButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                screen.getGame().setScreen(new RoomSelection(
                        screen.getGame(), screen.getAssetManager()));
            }
        });

        replayButton = new ImageActor(replayButtonTexture, 150);

        replayButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                if (data.type == RoomType.TUTORIAL) {
                    screen.getGame().setScreen(new TutorialRoomView(
                            screen.getGame(), data, screen.getAssetManager()));
                } else {
                    screen.getGame().setScreen(new RoomView(
                            screen.getGame(), data, screen.getAssetManager()));
                }
            }
        });

        float offsetX = 50f;
        float offsetY = 25f;
        float buttonSize = 200f;

        replayButton.setPosition(panelBG.getX() + offsetX,
                panelBG.getY() + offsetY);
        replayButton.setSize(buttonSize);

        continueButton.setPosition(panelBG.getSizeX() - 2 * offsetX - continueButton.getSizeX(),
                panelBG.getY() + offsetY);
        continueButton.setSize(buttonSize);


        GameProgressBar progressBar = new GameProgressBar(0, 0, 140, screen.getAssetManager(),
                screen.getGame());
        progressBar.setX(Vescape.GUI_VIEWPORT_WIDTH / 2 - progressBar.getWidth() / 2);
        progressBar.setY(panelBG.getY() - progressBar.getHeight() - 50);


        elements.addActor(panelBG);
        elements.addActor(replayButton);
        elements.addActor(continueButton);
        elements.addActor(roomName);
        elements.addActor(roomIcon);
        elements.addActor(progressBar.getGroup());

        Vescape.setGroupOrigin(elements,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);

        int stars = data.latestScore;

        Stars s = new Stars(roomIcon.getX() + roomIcon.getSizeX() / 2,
                roomIcon.getY(), 2.5f, stars, true, screen.assetManager);
        s.addStarsToGroup(elements);

        elements.setScaleY(0);
        elements.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.pow2));

        if(data.latestScore > data.highscore) {
            int temp = data.latestScore - data.highscore;
            data.highscore = data.latestScore;
            progressBar.animatedStepProgress(temp);
            Vescape.lastTotalStars += temp;
        }

        screen.getStage().addActor(screenDarkener);
        screenDarkener.setTouchable(Touchable.enabled);
        screen.getStage().addActor(elements);
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
