package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for menu panel used in hamburger menus and main menu
 */

public class MenuPanel {
    /**
     * Current screen
     */
    private MyScreen screen;

    /**
     * Used for text buttons layout
     */
    private Table menuTable;

    /**
     * Used for other buttons layout
     */
    private Table motherTable;

    /**
     * Change language button
     */
    private Button flagButton;

    /**
     * Button style for finnish
     */
    private Button.ButtonStyle finnishStyle;

    /**
     * Button style for english
     */
    private Button.ButtonStyle englishStyle;

    /**
     * Toggle sound button
     */
    private Button soundButton;

    /**
     * Style for sound enabled
     */
    private Button.ButtonStyle soundEnabledStyle;

    /**
     * Style for sound disabled
     */
    private Button.ButtonStyle soundDisabledStyle;

    /**
     * Toggle music button
     */
    private Button musicButton;

    /**
     * Style for music enabled
     */
    private Button.ButtonStyle musicEnabledStyle;

    /**
     * Style for music disabled
     */
    private Button.ButtonStyle musicDisabledStyle;

    /**
     * Constructor of the menu panel
     *
     * @param screen Current screen
     */
    public MenuPanel(MyScreen screen) {
        this.screen = screen;
        createMenuButtons();
        createOtherButtons();
    }

    /**
     * Creates the menu buttons
     */
    private void createMenuButtons() {
        menuTable = new Table();

        TextButton playButton = screen.getPanelButton1();
        TextButton exitButton = screen.getPanelButton2();

        menuTable.setFillParent(true);
        menuTable.defaults()
                .width(Vescape.GUI_VIEWPORT_WIDTH / 2)
                .height(175)
                .space(50);
        menuTable.add(playButton);
        menuTable.row();
        menuTable.add(exitButton);
        menuTable.setY(menuTable.getY() - 100);
        screen.getStage().addActor(menuTable);
    }

    /**
     * Creates settings buttons
     */
    private void createOtherButtons() {
        AssetManager assets = screen.getAssetManager();
        TextureRegionDrawable finnishImage = new TextureRegionDrawable(
                new TextureRegion(assets.get("MENU_flag_fi.png", Texture.class)));

        TextureRegionDrawable englishImage = new TextureRegionDrawable(
                new TextureRegion(assets.get("MENU_flag_en.png", Texture.class)));

        TextureRegionDrawable soundEnabledImage = new TextureRegionDrawable(
                new TextureRegion(assets.get("MENU_sound_on.png", Texture.class)));

        TextureRegionDrawable soundDisabledImage = new TextureRegionDrawable(
                new TextureRegion(assets.get("MENU_sound_off.png", Texture.class)));

        TextureRegionDrawable musicEnabledImage = new TextureRegionDrawable(
                new TextureRegion(assets.get("MENU_music_on.png", Texture.class)));

        TextureRegionDrawable musicDisabledImage = new TextureRegionDrawable(
                new TextureRegion(assets.get("MENU_music_off.png", Texture.class)));

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
            flagButton = new Button(finnishStyle);
        } else {
            flagButton = new Button(englishStyle);
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
                    flagButton.setStyle(englishStyle);
                    screen.getGame().setEnglish();
                } else {
                    flagButton.setStyle(finnishStyle);
                    screen.getGame().setFinnish();
                }
                screen.getGame().saveSettings();
                menuTable.clearChildren();
                createMenuButtons();
                AudioManager.playSound("button_toggle.wav");
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
                screen.getGame().saveSettings();
                AudioManager.playSound("button_toggle.wav");

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
                screen.getGame().saveSettings();
                AudioManager.playSound("button_toggle.wav");
            }
        });

    }

    /**
     * Removes elements from stage
     */
    public void dispose() {
        menuTable.remove();
        motherTable.remove();
    }
}