package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by atter on 04-Mar-17.
 */

public class MainMenu extends MyScreen {
    ImageActor bg;
    private WarningPopUp warningPopUp;

    public MainMenu(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
        assetManager.load("menu_logo.png", Texture.class);
    }

    @Override
    public void onStart() {
        bg = new ImageActor(assetManager.get("MENU_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        stage.addActor(bg);
        new MenuPanel(this);

        float movementY = 500;
        ImageActor title = new ImageActor(assetManager.get("menu_logo.png", Texture.class), 300);
        title.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - title.getSizeX() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT -  4 * title.getSizeY() / 3 + movementY);
        title.addAction(Actions.sequence(
                Actions.delay(0.2f),
                Actions.moveBy(0, -movementY, 1.5f, Interpolation.bounceOut)));

        stage.addActor(title);
    }

    @Override
    protected void update(float dt) {
        if (nextScreen != null)
            return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

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
                    warningPopUp = new WarningPopUp(MainMenu.this, "newGameWarning");
                } else {
                    setNextScreen(new StoryStartScreen(getGame(),
                            assetManager));
                    Vescape.storyStartSeen = true;
                }
            }});
        return button;
    }


    @Override
    public TextButton getPanelButton2() {
        String name = getGame().getMyBundle().get("exitButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                warningPopUp = new WarningPopUp(MainMenu.this, "exitWarning");
            }
        });

        return button;
    }

    @Override
    public void dispose() {
        assetManager.unload("menu_logo.png");
        super.dispose();
    }

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
