package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by atter on 04-Mar-17.
 */

public class MainMenu extends MyScreen {

    public MainMenu(Vescape game) {
        super(game);

        ImageActor bg = new ImageActor(new Texture("englishFlag.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX(-Vescape.GUI_VIEWPORT_WIDTH / 2);

        /*
        RotateByAction rotateByAction = new RotateByAction();
        rotateByAction.setAmount(10000);
        rotateByAction.setDuration(15);
        rotateByAction.setInterpolation(Interpolation.elasticOut);
        bg.addAction(rotateByAction);
        */
        stage.addActor(bg);

        float movementY = 500;
        ImageActor title = new ImageActor(new Texture("englishFlag.png"), 300);
        title.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - title.getSizeX() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT -  3 * title.getSizeY() / 2 + movementY);

        MoveByAction moveAction = new MoveByAction();
        moveAction.setAmountY(-movementY);
        moveAction.setDuration(1.25f);
        moveAction.setInterpolation(Interpolation.bounceOut);
        title.addAction(moveAction);
        stage.addActor(title);

        new MenuPanel(this);
    }

}
