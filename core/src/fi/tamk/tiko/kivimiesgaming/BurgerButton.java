package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 04-Mar-17.
 */

public class BurgerButton {
    private MyScreen screen;
    private MenuPanel menuPanel;
    private ImageActor screenDarkener;

    private Table table;
    private SelectableButton burgerButton;
    private boolean isOpen = false;

    public BurgerButton(final MyScreen screen) {
        this.screen = screen;

        burgerButton = new SelectableButton(new Texture("menurger.png"),
                new Texture("menurger_pressed.png"),
                100);

        burgerButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                togglePanel();
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.top().right();
        table.add(burgerButton).pad(25);


        screenDarkener = new ImageActor(new Texture("black.png"), Vescape.GUI_VIEWPORT_HEIGHT);

        screenDarkener.alpha = 0.85f;
        screenDarkener.setX(Vescape.GUI_VIEWPORT_WIDTH);

        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(table);
    }

    public void togglePanel() {
        boolean enabled = !burgerButton.isSelected();
        burgerButton.setSelected(enabled);
        if (enabled) {
            menuPanel = new MenuPanel(screen);
            screenDarkener.setPosition(screenDarkener.getX() - Vescape.GUI_VIEWPORT_WIDTH,
                    0);
        } else {
            menuPanel.dispose();
            screenDarkener.setPosition(screenDarkener.getX() + Vescape.GUI_VIEWPORT_WIDTH,
                    0);
        }

    }

    public boolean isOpen() {
        return burgerButton.isSelected();
    }

    public void reAddElementsToStage() {
        screenDarkener.remove();
        table.remove();
        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(table);
    }
}
