package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 04-Mar-17.
 */

public class OpenMenuButton {
    private MyScreen screen;
    private MenuPanel menuPanel;
    private ImageActor screenDarkener;

    public OpenMenuButton (final MyScreen screen) {
        this.screen = screen;

        SelectableButton openMenuButton = new SelectableButton(new Texture("menurger.png"),
                new Texture("menurger_pressed.png"),
                100);

        openMenuButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SelectableButton temp = ((SelectableButton)actor);
                temp.select();
                if (temp.isSelected()) {
                    menuPanel = new MenuPanel(screen);
                    screenDarkener.setPosition(screenDarkener.getX() - Vescape.GUI_VIEWPORT_WIDTH,
                            0);
                } else {
                    menuPanel.dispose();
                    screenDarkener.setPosition(screenDarkener.getX() + Vescape.GUI_VIEWPORT_WIDTH,
                            0);
                }
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().right();
        table.add(openMenuButton).pad(25);


        screenDarkener = new ImageActor(new Texture("black.png"), Vescape.GUI_VIEWPORT_HEIGHT);
        screenDarkener.alpha = 0.5f;
        screenDarkener.setX(Vescape.GUI_VIEWPORT_WIDTH);

        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(table);
    }
}
