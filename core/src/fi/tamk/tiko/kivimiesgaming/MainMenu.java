package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for main menu screen
 */

public class MainMenu extends MyScreen {

    /**
     * Background image actor
     */
    ImageActor bg;

    /**
     * Constuctor of the main menu
     *
     * @param game Reference to instance of the main classs of the game
     * @param assetManager Asset manager for managing assets
     */
    public MainMenu(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
        assetManager.load("menu_logo.png", Texture.class);
    }

    /**
     * Initializes the mainmenu screen
     */
    @Override
    public void onStart() {
        bg = new ImageActor(assetManager.get("MENU_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        stage.addActor(bg);
        new MenuPanel(this);

        float movementY = -200;
        float movementX = 600;
        ImageActor title = new ImageActor(assetManager.get("menu_logo.png", Texture.class), 300);
        title.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - title.getSizeX() / 2 + movementX,
                Vescape.GUI_VIEWPORT_HEIGHT -  4 * title.getSizeY() / 3 + movementY);
        /*title.addAction(Actions.sequence(
                Actions.delay(0.2f),
                Actions.moveBy(0, -movementY, 1.5f, Interpolation.bounceOut)));*/

        title.setColor(1, 1, 1, 0f);
        title.setScale(0f);
        float dur = 0.8f;
        title.addAction(Actions.parallel(
                Actions.scaleTo(1, 1, dur, Interpolation.pow2),
                Actions.moveBy(-movementX, -movementY, dur, Interpolation.pow2),
                Actions.fadeIn(dur)));

        stage.addActor(title);
    }

    /**
     * Updates the mainmenu each frame
     *
     * @param dt Time between previous and current frame
     */
    @Override
    protected void update(float dt) {
        if (nextScreen != null)
            return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    /**
     * Create and return new game button
     *
     * @return new game button
     */
    @Override
    public TextButton getPanelButton1() {
        createNewGameButton();
        String name = getGame().getMyBundle().get("newGameButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                if (Vescape.storyStartSeen) {
                    new WarningPopUp(MainMenu.this, "newGameWarning");
                } else {
                    setNextScreen(new StoryStartScreen(getGame(),
                            assetManager));
                    Vescape.storyStartSeen = true;
                }
            }});
        return button;
    }

    /**
     * Create and return exit game button
     *
     * @return exit game button
     */
    @Override
    public TextButton getPanelButton2() {
        String name = getGame().getMyBundle().get("exitButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                new WarningPopUp(MainMenu.this, "exitWarning");
            }
        });

        return button;
    }

    /**
     * Unload the unneeded assets
     */
    @Override
    public void dispose() {
        assetManager.unload("menu_logo.png");
        super.dispose();
    }

    /**
     * Create continue button
     */
    private void createNewGameButton() {
        String name = getGame().getMyBundle().get("playButton");

        TextButton createNewGameButton = new TextButton(name,
                getGame().getTextButtonStyle());

        createNewGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                if (Vescape.storyStartSeen) {
                    setNextScreen(new RoomSelection(getGame(), assetManager, true));
                } else {
                    Vescape.storyStartSeen = true;
                    setNextScreen(new StoryStartScreen(getGame(), assetManager));
                }
            }
        });

        createNewGameButton.setSize(Vescape.GUI_VIEWPORT_WIDTH / 2, 175);

        createNewGameButton.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - createNewGameButton.getWidth() / 2,
                950);


        if (Vescape.lastTotalStars == 0 && !Vescape.storyStartSeen) {
            createNewGameButton.setDisabled(true);
            createNewGameButton.setColor(0.2f, 0f, 0.2f, 0.0f);
        }
        stage.addActor(createNewGameButton);

    }
}
