package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 16-Mar-17.
 */

public class RoomPopUp {

    private ImageActor screenDarkener;
    private ImageActor panelBG;
    private Group elements;


    public RoomPopUp(final MyScreen screen, RoomData data) {

        screenDarkener = new ImageActor(new Texture("black.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        screenDarkener.alpha = 0.85f;

        screenDarkener.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((RoomSelection)screen).selectRoom(null);
            }
        });

        elements = new Group();



        panelBG = new ImageActor(new Texture("riddle_info_box_fill.png"));
        panelBG.alpha = 0.7f;

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        panelBG.setSize(panelBG.getSizeY() * (panelTargetW / panelBG.getSizeX()));
        panelBG.setPosition((Vescape.GUI_VIEWPORT_WIDTH - panelBG.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - panelBG.getSizeY()) / 2);

        Label.LabelStyle labelStyle = new Label.LabelStyle(screen.getGame().getFontBig(),
                Color.BLACK);
        Label roomName = new Label(
                screen.getGame().getMyBundle().get(data.getTypeAsString()),
                labelStyle);
        roomName.setPosition(panelBG.getX() + (panelBG.getSizeX() - roomName.getWidth()) / 2,
                panelBG.getY() + panelBG.getSizeY() - roomName.getHeight() - 25);


        //Create animated stars for testing
        //Create room icon

        elements.addActor(panelBG);
        elements.addActor(roomName);

        Vescape.setGroupOrigin(elements,
                Vescape.GUI_VIEWPORT_WIDTH / 2, Vescape.GUI_VIEWPORT_HEIGHT / 2);

        elements.setScaleY(0);
        elements.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.pow2));

        screen.getStage().addActor(screenDarkener);
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