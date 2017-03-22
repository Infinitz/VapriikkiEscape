package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by atter on 04-Mar-17.
 */

public class MenuPanel {
    private MyScreen screen;

    private Table menuTable;
    private Table motherTable;

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
        menuTable = new Table();

        TextButton playButton = screen.getPanelButton1();
        TextButton exitButton = screen.getPanelButton2();

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
                        new Texture("MENU_flag_fi.png")));

        TextureRegionDrawable englishImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("MENU_flag_en.png")));

        TextureRegionDrawable soundEnabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("MENU_sound_on.png")));
        TextureRegionDrawable soundDisabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("MENU_sound_off.png")));

        TextureRegionDrawable musicEnabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("MENU_music_on.png")));
        TextureRegionDrawable musicDisabledImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("MENU_music_off.png")));


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

        motherTable = new Table();
        motherTable.setFillParent(true);
        motherTable.bottom();
        motherTable.add(soundButton)
                .pad(25).padBottom(100)
                .width(buttonSize * soundAspectRatio)
                .height(buttonSize);
        motherTable.add(musicButton)
                .pad(25).padBottom(100)
                .width(buttonSize * musicAspectRatio)
                .height(buttonSize);
        motherTable.add(flagButton)
                .pad(25).padBottom(100)
                .width(buttonSize * flagAspectRatio)
                .height(buttonSize);

        screen.getStage().addActor(motherTable);

        flagButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (screen.getGame().getMyBundle().get("language").equals("finnish")) {
                    flagButton.setStyle(finnishStyle);
                    screen.getGame().setEnglish();
                } else {
                    flagButton.setStyle(englishStyle);
                    screen.getGame().setFinnish();
                }
                menuTable.clearChildren();
                createMenuButtons();
            }
        });

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.enableSounds(!AudioManager.isSoundsEnabled());
                if (AudioManager.isSoundsEnabled()) {
                    soundButton.setStyle(soundEnabledStyle);

                } else {
                    soundButton.setStyle(soundDisabledStyle);
                }


            }
        });

        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.enableMusic(!AudioManager.isMusicEnabled());
                if (AudioManager.isMusicEnabled()) {
                    musicButton.setStyle(musicEnabledStyle);
                } else {
                    musicButton.setStyle(musicDisabledStyle);
                }


            }
        });

    }

    public void dispose() {
        menuTable.remove();
        motherTable.remove();
    }
}