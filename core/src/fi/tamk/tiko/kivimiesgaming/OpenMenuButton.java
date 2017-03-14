package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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

        screenDarkener.alpha = 0.85f;
        screenDarkener.setX(Vescape.GUI_VIEWPORT_WIDTH);

        screen.getStage().addActor(screenDarkener);
        screen.getStage().addActor(table);
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
