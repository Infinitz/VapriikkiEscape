package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by atter on 16-Mar-17.
 */

public class RoomPopUp {

    private ImageActor screenDarkener;
    private ImageActor panelBG;
    public RoomPopUp(MyScreen screen, RoomData data) {

        screenDarkener = new ImageActor(new Texture("black.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        screenDarkener.alpha = 0.85f;

        panelBG = new ImageActor(new Texture("riddle_info_box_fill.png"));
        panelBG.alpha = 0.7f;

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        panelBG.setSize(panelBG.getSizeY() * (panelTargetW / panelBG.getSizeX()));
        panelBG.setPosition((Vescape.GUI_VIEWPORT_WIDTH - panelBG.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - panelBG.getSizeY()) / 2);
        panelBG.setScaleY(0);
        panelBG.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.pow2));

        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(panelBG);
    }


    public void dispose() {
        panelBG.addAction(Actions.sequence(
                Actions.scaleTo(1, 0, 0.3f, Interpolation.pow2),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        screenDarkener.remove();
                        panelBG.remove();
                    }
                })
        ));
    }
}
