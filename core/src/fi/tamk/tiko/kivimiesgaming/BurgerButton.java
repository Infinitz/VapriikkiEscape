package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 04-Mar-17.
 */

public class BurgerButton {
    private MyScreen screen;
    private MenuPanel menuPanel;
    private ScreenDarkener screenDarkener;

    private Table table;
    private SelectableButton burgerButton;
    private boolean isOpen = false;

    public BurgerButton(final MyScreen screen) {
        this.screen = screen;

        burgerButton = new SelectableButton(screen.getAssetManager().
                get("menurger.png", Texture.class),
                screen.getAssetManager().
                        get("menurger_pressed.png", Texture.class),
                150);

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


        screenDarkener = new ScreenDarkener(screen.getAssetManager().
                get("black.png", Texture.class));

        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(table);
    }

    public void togglePanel() {
        AudioManager.playSound("button_toggle.wav");
        boolean enabled = !burgerButton.isSelected();
        burgerButton.setSelected(enabled);
        if (enabled) {
            menuPanel = new MenuPanel(screen);
            screenDarkener.enable(true);

        } else {
            menuPanel.dispose();
            screenDarkener.enable(false);
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
