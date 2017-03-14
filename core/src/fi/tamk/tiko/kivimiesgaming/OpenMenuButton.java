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

    public OpenMenuButton (final MyScreen screen) {
        this.screen = screen;

        /*TextureRegionDrawable image = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("menurger.png")));
        TextureRegionDrawable imagePressed = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("menurger_pressed.png")));


        Button.ButtonStyle style = new Button.ButtonStyle(image, imagePressed, image);

        Button button = new Button(style);

        */



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
                } else {
                    menuPanel.dispose();
                }
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().right();
        table.add(openMenuButton).pad(25);
        screen.getStage().addActor(table);

    }
}
