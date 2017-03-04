package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by atter on 04-Mar-17.
 */

public class MainMenu extends MyScreen {

    public MainMenu(Vescape game) {
        super(game);
        new MenuPanel(this);

        //background
    }

}
