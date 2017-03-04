package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by atter on 04-Mar-17.
 */

public class MenuPanel {
    private MyScreen screen;

    private Button flagButton;
    private Button.ButtonStyle finnishStyle;
    private Button.ButtonStyle englishStyle;

    private Button soundButton;
    private Button.ButtonStyle soundEnabledStyle;
    private Button.ButtonStyle soundDisabledStyle;

    private Button musicButton;
    private Button.ButtonStyle musicEnabledStyle;
    private Button.ButtonStyle musicDisabledStyle;

    public MenuPanel (MyScreen screen) {
        this.screen = screen;
        createMenuButtons();
        createOtherButtons();
    }


    private void createMenuButtons() {
        Table menuTable = new Table();

        TextButton playButton = new TextButton(screen.getGame().getMyBundle().get("playButton"),
                screen.getGame().getSkin());
        TextButton exitButton = new TextButton(screen.getGame().getMyBundle().get("exitButton"),
                screen.getGame().getSkin());

        menuTable.setFillParent(true);
        menuTable.defaults()
                .width(Vescape.GUI_VIEWPORT_WIDTH / 2)
                .height(175)
                .space(25);
        menuTable.add(playButton);
        menuTable.row();
        menuTable.add(exitButton);

        screen.getStage().addActor(menuTable);
    }

    private void createOtherButtons() {

        TextureRegionDrawable finnishImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("englishFlag.png")));

        TextureRegionDrawable englishImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("englishFlag.png")));

        TextureRegionDrawable soundEnabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("englishFlag.png")));
        TextureRegionDrawable soundDisabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("englishFlag.png")));

        TextureRegionDrawable musicEnabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("englishFlag.png")));
        TextureRegionDrawable musicDisabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("englishFlag.png")));


        finnishStyle = new Button.ButtonStyle(finnishImage, finnishImage, finnishImage);
        englishStyle = new Button.ButtonStyle(englishImage, englishImage, englishImage);
        soundEnabledStyle =
                new Button.ButtonStyle(soundEnabledImage, soundEnabledImage, soundEnabledImage);
        soundDisabledStyle =
                new Button.ButtonStyle(soundDisabledImage, soundDisabledImage, soundDisabledImage);
        musicEnabledStyle =
                new Button.ButtonStyle(musicEnabledImage, musicEnabledImage, musicEnabledImage);
        musicDisabledStyle =
                new Button.ButtonStyle(musicDisabledImage, musicDisabledImage, musicDisabledImage);


        if (screen.getGame().getMyBundle().get("language").equals("finnish")) {
            flagButton = new Button(englishStyle);
        } else {
            flagButton = new Button(finnishStyle);
        }

        if (AudioManager.isSoundsEnabled()) {
            soundButton = new Button(soundEnabledStyle);
        } else {
            soundButton = new Button(soundDisabledStyle);
        }

        if (AudioManager.isMusicEnabled()) {
            musicButton = new Button(musicEnabledStyle);
        } else {
            musicButton = new Button(musicDisabledStyle);
        }


        float flagAspectRatio = flagButton.getWidth() / flagButton.getHeight();
        float soundAspectRatio = soundButton.getWidth() / soundButton.getHeight();
        float musicAspectRatio = musicButton.getWidth() / musicButton.getHeight();
        float buttonSize = 200;

        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.add(soundButton).pad(25).padBottom(100).width(buttonSize * soundAspectRatio).height(buttonSize);
        table.add(musicButton).pad(25).padBottom(100).width(buttonSize * musicAspectRatio).height(buttonSize);
        table.add(flagButton).pad(25).padBottom(100).width(buttonSize * flagAspectRatio).height(buttonSize);

        screen.getStage().addActor(table);

        flagButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (screen.getGame().getMyBundle().get("language").equals("finnish")) {
                    flagButton.setStyle(englishStyle);
                    screen.getGame().setEnglish();
                } else {
                    flagButton.setStyle(finnishStyle);
                    screen.getGame().setFinnish();
                }
            }
        });

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (AudioManager.isSoundsEnabled()) {
                    soundButton.setStyle(soundDisabledStyle);

                } else {
                    soundButton.setStyle(soundEnabledStyle);
                }
                AudioManager.enableSounds(!AudioManager.isSoundsEnabled());
            }
        });

        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (AudioManager.isMusicEnabled()) {
                    soundButton.setStyle(soundDisabledStyle);
                } else {
                    soundButton.setStyle(soundEnabledStyle);
                }
                AudioManager.enableMusic(!AudioManager.isMusicEnabled());
            }
        });

    }
}
