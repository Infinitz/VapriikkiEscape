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
    private SelectableButton burgerButton;

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

        Table table = new Table();
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
/*

    private float duration = 1.0f;
    private float startValue = 0.0f;
    private float targetValue = 1.0f;
    private float currentValue = 0.0f;
    private float fadeStartAlpha = 0.9f;
    private float fadeDuration = 1.0f;
    private SequenceAction getAction() {
        currentValue = startValue;
        SequenceAction action = new SequenceAction();

        final RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                startValue = MathUtils.lerp(currentValue, targetValue, currentValue / targetValue);
                if (currentValue / targetValue < 0.99f) {
                    action.addAction(run);
                } else {
                    currentValue = targetValue;
                }
            }
        });

        return action;
    }
    */
}
