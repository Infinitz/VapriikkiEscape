package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 */

public class ScreenDarkener extends ImageActor {

    public ScreenDarkener(Texture texture, boolean enabled) {
        super(texture, Vescape.GUI_VIEWPORT_HEIGHT);
        setTouchable(Touchable.enabled);
        alpha = 0;
        setX(Vescape.GUI_VIEWPORT_WIDTH);
        if (enabled) {
            alpha = 0.8f;
            setPosition(getX() - Vescape.GUI_VIEWPORT_WIDTH, 0);
        }

    }

    public void enable(boolean enabled, boolean animate) {
        if (animate) {
            if (enabled) {
                setPosition(getX() - Vescape.GUI_VIEWPORT_WIDTH, 0);
                addAction(Actions.repeat(10, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                alpha += 0.08f;
                            }
                        })
                ));
            } else {
                addAction(Actions.sequence(
                        Actions.repeat(10, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                alpha -= 0.08f;
                            }
                        })),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                setPosition(getX() + Vescape.GUI_VIEWPORT_WIDTH,
                                        0);
                            }
                        })));
            }
        } else {
            if (enabled) {
                alpha = 0.8f;
                setPosition(getX() - Vescape.GUI_VIEWPORT_WIDTH, 0);
            } else {
                alpha = 0f;
                setPosition(getX() + Vescape.GUI_VIEWPORT_WIDTH, 0);
            }
        }


    }
}
