package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class which creates hamburger button
 */

public class BurgerButton {

    /**
     * Current screen of the game
     */
    private MyScreen screen;

    /**
     * Panel which has burger menu buttons
     */
    private MenuPanel menuPanel;

    /**
     * Screen darkener that is used to darken the background
     */
    private ScreenDarkener screenDarkener;

    /**
     * Table that is used for dining
     */
    private Table table;

    /**
     * The actual hamburger button
     */
    private SelectableButton burgerButton;

    /**
     * Constructor of the class
     *
     * @param screen Current screen of the game
     */
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
                get("black.png", Texture.class), false);

        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(table);
    }

    /**
     * Toggles menu panel on/off depending on its current state
     */
    public void togglePanel() {
        AudioManager.playSound("button_toggle.wav");
        boolean enabled = !burgerButton.isSelected();
        burgerButton.setSelected(enabled);
        if (enabled) {
            menuPanel = new MenuPanel(screen);
            screenDarkener.enable(true, false);

        } else {
            menuPanel.dispose();
            screenDarkener.enable(false, true);
        }

    }

    /**
     * Returns true if panel is open
     *
     * @return true if panel is open
     */
    public boolean isOpen() {
        return burgerButton.isSelected();
    }

    /**
     * Removes elements from the table and adds them back so they will be on top of the stage
     */
    public void reAddElementsToStage() {
        screenDarkener.remove();
        table.remove();
        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(table);
    }
}
