package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * This class builds the warning popup when creating a new game or exiting the game.
 */

public class WarningPopUp {
    /**
     * Screen darkened is used to darken screen outside of popups.
     */
    private ScreenDarkener screenDarkener;
    /**
     * panelBG is the background for popup.
     */
    private ImageActor panelBG;
    /**
     * elements is a group consisting of all the objects in the popup.
     */
    private Group elements;

    /**
     * Class constructor.
     */
    public WarningPopUp(final MyScreen screen, final String buttonName) {
        screenDarkener =
                new ScreenDarkener(screen.getAssetManager().get("black.png", Texture.class), false);
        screenDarkener.enable(true, true);
        elements = new Group();


        panelBG = new ImageActor(
                screen.getAssetManager().get("confirm_box.jpg", Texture.class));
        panelBG.alpha = 1f;

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        panelBG.setSize(panelBG.getSizeY() * (panelTargetW / panelBG.getSizeX()));

        panelBG.setPosition((Vescape.GUI_VIEWPORT_WIDTH - panelBG.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - panelBG.getSizeY()) / 2);

        Label.LabelStyle labelStyle = new Label.LabelStyle(screen.getGame().getButtonFont(),
                new Color(0f, 0f, 0f, 0.9f));
        String warningString = Utilities.splitTextIntoLines(
                screen.getGame().getMyBundle().get(buttonName), 25);
        Label warningText = new Label(warningString, labelStyle);

        warningText.setPosition(panelBG.getX() + (panelBG.getSizeX() - warningText.getWidth()) / 2,
                panelBG.getY() + panelBG.getSizeY() - warningText.getHeight() - 50);

        ImageActor jaButton = new ImageActor(screen.getAssetManager().get("druckknopf_ja.png",
                Texture.class));

        ImageActor neinButton = new ImageActor(screen.getAssetManager().get("druckknopf_nein.png",
                Texture.class));

        ImageActor exitButton = new ImageActor(screen.getAssetManager().get("back_button.png",
                Texture.class));

        jaButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                if (buttonName.equalsIgnoreCase("newGameWarning")) {
                    screen.game.resetScores();
                    if (Vescape.storyStartSeen) {
                        screen.setNextScreen(new RoomSelection(screen.getGame(),
                                screen.assetManager, true));
                    } else {
                        screen.setNextScreen(new StoryStartScreen(screen.getGame(),
                                screen.assetManager));
                        Vescape.storyStartSeen = true;
                    }
                } else if (buttonName.equalsIgnoreCase("exitWarning")) {
                    Gdx.app.exit();
                }

            }
        });

        neinButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                dispose();
            }
        });

        exitButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                dispose();
            }
        });

        jaButton.setSize(150);
        neinButton.setSize(150);
        exitButton.setSize(100);

        int offsetX = 100;
        int offsetY = 50;

        jaButton.setPosition(panelBG.getSizeX() - jaButton.getSizeX() - offsetX,
                panelBG.getTrueY() + offsetY);

        neinButton.setPosition(panelBG.getTrueX() + offsetX,
                panelBG.getTrueY() + offsetY);

        exitButton.setPosition(panelBG.getSizeX() - exitButton.getSizeX(),
                panelBG.getSizeY());

        elements.addActor(panelBG);
        elements.addActor(jaButton);
        elements.addActor(neinButton);
        //elements.addActor(exitButton);
        elements.addActor(warningText);

        Utilities.setGroupOrigin(elements,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);

        elements.setScaleY(0);
        elements.addAction(Actions.scaleTo(1, 1, 0.2f, Interpolation.pow2));

        screen.getStage().addActor(screenDarkener);
        screen.stage.addActor(elements);

        AudioManager.playSound("panel_open.wav");
    }

    /**
     * Disposes the panel
     */
    public void dispose() {
        AudioManager.playSound("panel_close.wav");
        elements.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.scaleTo(1, 0, 0.2f, Interpolation.pow2),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                screenDarkener.enable(false, true);
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
