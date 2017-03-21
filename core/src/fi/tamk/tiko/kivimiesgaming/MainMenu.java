package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
    public MainMenu(Vescape game) {
        super(game);

        ImageActor bg = new ImageActor(new Texture("MENU_bg.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        stage.addActor(bg);

        float movementY = 500;
        ImageActor title = new ImageActor(new Texture("menu_logo.png"), 300);
        title.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - title.getSizeX() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT -  3 * title.getSizeY() / 2 + movementY);

        SequenceAction seq = Actions.sequence(
                Actions.delay(0.4f),
                Actions.moveBy(0, -movementY, 1.25f, Interpolation.bounceOut));

        title.addAction(seq);
        stage.addActor(title);

        new MenuPanel(this);
    }

    @Override
    public TextButton getPanelButton1() {
        String name = getGame().getMyBundle().get("playButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().setScreen(new RoomSelection(getGame()));
            }
        });

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
                Gdx.app.exit();
            }
        });

        return button;
    }
}
