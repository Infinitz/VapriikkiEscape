package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by atter on 22-Mar-17.
 */

public class RoomView extends MyScreen {

    private RoomData roomData;
    private Riddle currentRiddle;

    private Group riddlePanel;
    private Label riddleLabel;

    private Group hintPanel;
    private ImageActor hintPanelBG;
    private Label hintLabel;
    private ImageActor hintButton;

    private BurgerButton burgerButton;
    private ImageActor bg;
    private TextField answerField;
    private TextButton answerButton;
    private ImageActor touchDetector;
    private ImageActor answerFieldBG;
    private ImageActor[] answerResultSlots;
    private ImageActor[] answerResults;

    private Label.LabelStyle labelStyle;
    private Texture riddlePanelTexture;

    private Texture emptyAnswerTex;
    private Texture wrongAnswerTex;
    private Texture correctAnswerTex;
    private Texture perfectAnswerTex;

    private boolean hintUsed = false;
    private boolean keyboardEnabled = false;
    private boolean hintPanelEnabled = false;
    private int currentRiddleCount = 0;
    private int correctAnswerCount = 0;
    private int hintsUsedCount = 0;

    private float hintButtonAnimMovement = 300f;
    private float hintPanelAnimMovement = 700f;

    public RoomView(Vescape game, RoomData roomData, AssetManager assetManager) {
        super(game, assetManager);
        this.roomData = roomData;
        assetManager.load("riddle_answer_box.jpg", Texture.class);
        assetManager.load("riddle_info_box.png", Texture.class);
        /*
        assetManager.load("riddle_info_box_fill.png", Texture.class);
        assetManager.load("riddle_info_box_border.png", Texture.class);
        */
        assetManager.load("riddle_slot_empty.png", Texture.class);
        assetManager.load("riddle_slot_nope.png", Texture.class);
        assetManager.load("riddle_slot_done.png", Texture.class);
        assetManager.load("riddle_slot_done_golden.png", Texture.class);
        assetManager.load("riddle_retry_active.png", Texture.class);
        assetManager.load("riddle_next_active.png", Texture.class);
        assetManager.load("riddle_confirm_box.png", Texture.class);
        assetManager.load("riddle_hint.png", Texture.class);

        roomData.loadRiddles(assetManager);
        roomData.loadTextures();

        labelStyle = new Label.LabelStyle(getGame().getRiddleFont(),
                Color.BLACK);
    }

    @Override
    protected void onAssetsLoaded() {
        touchDetector = new ImageActor(assetManager.get("black.png", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);
        touchDetector.setPosition(Vescape.GUI_VIEWPORT_WIDTH, 0);
        touchDetector.alpha = 0f;
        touchDetector.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (keyboardEnabled) {
                    enableKeyboard(false);
                } else {
                    closeHintPanel();
                }

            }
        });

        riddlePanelTexture = assetManager.get("riddle_info_box.png", Texture.class);

        emptyAnswerTex = assetManager.get("riddle_slot_empty.png", Texture.class);
        wrongAnswerTex = assetManager.get("riddle_slot_nope.png", Texture.class);
        correctAnswerTex = assetManager.get("riddle_slot_done.png", Texture.class);
        perfectAnswerTex = assetManager.get("riddle_slot_done_golden.png", Texture.class);


        bg = new ImageActor(roomData.getBackground(),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        answerFieldBG = new ImageActor(
                assetManager.get("riddle_answer_box.jpg", Texture.class));
        answerFieldBG.setSize(answerFieldBG.getSizeY() *
                (Vescape.GUI_VIEWPORT_WIDTH / answerFieldBG.getSizeX()));

        answerFieldBG.setPosition(0, 200);
        float answerFieldPadding = 30f;
        answerField = new TextField("", getGame().getTextFieldStyle());
        answerField.setPosition(answerFieldPadding, 180);
        answerField.setSize(answerFieldBG.getSizeX() - 2 * answerFieldPadding, 200);
        answerField.setAlignment(Align.center);
        answerField.setMaxLength(Vescape.MAX_CHARS_IN_ANSWER);

        answerField.setOnscreenKeyboard(new TextField.OnscreenKeyboard() {

            @Override
            public void show(boolean visible) {
                enableKeyboard(true);
            }
        });

        answerResultSlots = new ImageActor[Vescape.TOTAL_RIDDLES_ROOM];
        answerResults = new ImageActor[Vescape.TOTAL_RIDDLES_ROOM];
        float answerSlotSpace = 20;
        for (int i = 0; i < answerResultSlots.length; ++i) {
            answerResultSlots[i] = new ImageActor(emptyAnswerTex, 100);
            float centerX = Vescape.GUI_VIEWPORT_WIDTH / 2 - answerResultSlots[i].getSizeX() / 2;
            float x = centerX +
                    (-(answerResultSlots.length / 2f) + 0.5f + i) * (answerResultSlots[i].getSizeX()
                            + answerSlotSpace);

            float y = Vescape.GUI_VIEWPORT_HEIGHT - answerResultSlots[i].getSizeY() - 10;
            answerResultSlots[i].setPosition(x, y);
        }

        answerButton = new TextButton(getGame().getMyBundle().get("answerButton"),
                getGame().getTextButtonStyle());

        answerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (keyboardEnabled) {
                    enableKeyboard(false);
                }
                answer(answerField.getText());
            }
        });
        answerButton.setSize(Vescape.GUI_VIEWPORT_WIDTH / 2, 175);
        answerButton.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - answerButton.getWidth() / 2,
                0);


        hintPanel = new Group();
        hintPanelBG = new ImageActor(
                assetManager.get("riddle_confirm_box.png", Texture.class));
        hintPanelBG.setSize(hintPanelBG.getSizeY() *
                (Vescape.GUI_VIEWPORT_WIDTH / hintPanelBG.getSizeX()));
        hintPanelBG.setY(Vescape.GUI_VIEWPORT_HEIGHT / 2 - hintPanelBG.getSizeY() / 2 - 320);
        hintPanel.setY(hintPanel.getY() - hintPanelAnimMovement);
        hintPanel.addActor(hintPanelBG);

        hintButton = new ImageActor(assetManager.get("riddle_hint.png", Texture.class), 150f);
        hintButton.setPosition(
                Vescape.GUI_VIEWPORT_WIDTH - hintButton.getSizeX() - 25,
                answerFieldBG.getY() + answerFieldBG.getSizeY()
        );

        hintButton.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                enabledHintPanel(true);
            }
        });

        float animLength = 0.85f;
        float deltaY = 450f;
        stage.addActor(bg);
        for (int i = 0; i < answerResultSlots.length; ++i) {
            stage.addActor(answerResultSlots[i]);
            answerResultSlots[i].setPosition(answerResultSlots[i].getX(), answerResultSlots[i].getY() + deltaY);
            answerResultSlots[i].addAction(Actions.moveBy(0, -deltaY, animLength, Interpolation.pow2));
        }
        answerFieldBG.setPosition(answerFieldBG.getX(), answerFieldBG.getY() - deltaY);
        answerFieldBG.addAction(Actions.moveBy(0, deltaY, animLength, Interpolation.pow2));

        hintButton.setPosition(hintButton.getX() + hintButtonAnimMovement, hintButton.getY());

        answerField.setPosition(answerField.getX(), answerField.getY() - deltaY);
        answerField.addAction(Actions.moveBy(0, deltaY, animLength, Interpolation.pow2));

        answerButton.setPosition(answerButton.getX(), answerButton.getY() - deltaY);
        answerButton.addAction(Actions.moveBy(0, deltaY, animLength, Interpolation.pow2));



        stage.addActor(answerFieldBG);
        stage.addActor(answerButton);
        burgerButton = new BurgerButton(this);
        stage.addActor(answerField);
        stage.addActor(hintButton);
        stage.addActor(touchDetector);
        createNewPanelWithAnimation(animLength);

    }

    @Override
    public void onStart() {
    }


    public void answer(String playerAnswer) {
        playerAnswer = playerAnswer.trim();
        boolean correctAnswer = currentRiddle.getRiddle(
                getGame().getMyBundle().getLocale().getLanguage()).isCorrectAnswer(playerAnswer);

        if (correctAnswer) {
            correctAnswerCount++;
            if (hintUsed) {
                answerResults[currentRiddleCount] =
                        new ImageActor(correctAnswerTex, answerResultSlots[0].getSizeY());
            } else {
                answerResults[currentRiddleCount] =
                        new ImageActor(perfectAnswerTex, answerResultSlots[0].getSizeY());
            }
        } else if (!hintUsed){
            enabledHintPanel(true);
            return;
        } else {
            answerResults[currentRiddleCount] =
                    new ImageActor(wrongAnswerTex, answerResultSlots[0].getSizeY());
        }

        if (hintUsed) {
            hintButton.addAction(
                    Actions.moveBy(hintButtonAnimMovement, 0, 0.5f, Interpolation.pow2));
        }


        answerField.setDisabled(true);
        answerField.setText("");
        answerButton.setDisabled(true);

        answerResults[currentRiddleCount].setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2,
                3 * Vescape.GUI_VIEWPORT_HEIGHT / 2);
        answerResults[currentRiddleCount].setScale(10f);
        getStage().addActor(answerResults[currentRiddleCount]);
        answerResults[currentRiddleCount].addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(
                                Vescape.GUI_VIEWPORT_WIDTH / 2 -
                                        answerResults[currentRiddleCount].getSizeX() / 2,
                                Vescape.GUI_VIEWPORT_HEIGHT / 2 -
                                        answerResults[currentRiddleCount].getSizeY() / 2, 0.3f,
                                Interpolation.pow2),
                        Actions.scaleTo(3.5f, 3.5f, 0.6f, Interpolation.bounceOut),
                        Actions.rotateTo(((float)Math.random() - 0.5f) * 25f, 0.35f)
                ),
                Actions.delay(0.25f),

                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        nextRiddle();
                    }
                }),
                Actions.parallel(
                        Actions.moveTo(answerResultSlots[currentRiddleCount].getX(),
                                answerResultSlots[currentRiddleCount].getY(),
                                0.75f, Interpolation.pow2),
                        Actions.scaleTo(1, 1, 0.5f),
                        Actions.rotateTo(0, 0.5f)
                )
        ));

    }

    private void nextRiddle() {
        hintUsed = false;

        final float animLength = 0.85f;
        final Group oldRiddle = riddlePanel;
        oldRiddle.addAction(Actions.sequence(
                Actions.delay(0.1f),
                Actions.parallel(
                        Actions.scaleTo(0.7f, 0.7f, animLength),
                        Actions.moveBy(-1000f, 0, animLength, Interpolation.pow2),
                        Actions.rotateBy(90, animLength),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (currentRiddleCount == Vescape.TOTAL_RIDDLES_ROOM - 1) {
                                    return;
                                }
                                createNewPanelWithAnimation(animLength);
                            }
                        })
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        oldRiddle.remove();
                        answerField.setDisabled(false);
                        answerButton.setDisabled(false);
                        if (currentRiddleCount == Vescape.TOTAL_RIDDLES_ROOM - 1) {
                            roomCompleted();
                        }
                        ++currentRiddleCount;
                    }
                })
        ));
    }

    private void createNewPanelWithAnimation(float animLength) {
        createNewRiddlePanel();
        riddlePanel.setRotation(-45);
        riddlePanel.setScale(0.5f);
        riddlePanel.setX(1000);
        riddlePanel.addAction(Actions.parallel(
                Actions.scaleTo(1f, 1f, animLength, Interpolation.pow2),
                Actions.rotateBy(45, animLength, Interpolation.pow2),
                Actions.moveBy(-1000f, 0, animLength, Interpolation.pow2)));
        getStage().addActor(riddlePanel);
        burgerButton.reAddElementsToStage();
    }

    private void createNewRiddlePanel() {
        currentRiddle = roomData.getRandomRiddle();

        ImageActor riddlePanelBg = new ImageActor(riddlePanelTexture);

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH - 50;
        riddlePanelBg.setSize(riddlePanelBg.getSizeY() * (panelTargetW / riddlePanelBg.getSizeX()));
        riddlePanelBg.setPosition((Vescape.GUI_VIEWPORT_WIDTH - riddlePanelBg.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - riddlePanelBg.getSizeY()) - 130);

        ImageActor riddleImage = new ImageActor(
                assetManager.get(currentRiddle.imagePath, Texture.class),
                2 * riddlePanelBg.getSizeX() / 3);

        riddleImage.setPosition(
                riddlePanelBg.getX() + (riddlePanelBg.getSizeX() - riddleImage.getWidth()) / 2,
                riddlePanelBg.getY() + riddlePanelBg.getSizeY() - riddleImage.getHeight() - 35);

        riddleLabel = new Label(
                currentRiddle.getRiddle(getGame().getMyBundle().getLocale().getLanguage()).riddle,
                labelStyle);
        riddleLabel.setPosition(
                riddlePanelBg.getX() + 30,
                riddleImage.getY() - riddleLabel.getHeight() - 30);

        Group g = new Group();
        g.addActor(riddlePanelBg);
        g.addActor(riddleImage);
        g.addActor(riddleLabel);
        riddlePanel = g;
    }

    @Override
    protected void update(float dt) {
        if (!assetsLoaded)
            return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            if (currentRiddleCount == Vescape.TOTAL_RIDDLES_ROOM) {
                getGame().setScreen(new RoomSelection(
                        getGame(), assetManager));
            } else if (keyboardEnabled) {
                enableKeyboard(false);
            } else if (hintPanelEnabled) {
                closeHintPanel();
            } else {
                burgerButton.togglePanel();
            }


        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            enableKeyboard(false);
        }

        if (burgerButton.isOpen()) {
            updateTexts();
        }
        answerField.setCursorPosition(answerField.getText().length());
    }

    @Override
    public TextButton getPanelButton1() {
        String name = getGame().getMyBundle().get("continueButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                burgerButton.togglePanel();
            }
        });

        return button;
    }

    @Override
    public TextButton getPanelButton2() {
        String name = getGame().getMyBundle().get("exitRoomButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().setScreen(new RoomSelection(getGame(), assetManager));
            }
        });

        return button;
    }

    @Override
    public void dispose() {
        super.dispose();
        roomData.unloadRiddleImages(assetManager);
        assetManager.unload("riddle_info_box.png");
        assetManager.unload("riddle_slot_empty.png");
        assetManager.unload("riddle_slot_nope.png");
        assetManager.unload("riddle_slot_done.png");
        assetManager.unload("riddle_slot_done_golden.png");
    }

    private void enabledHintPanel(boolean enabled) {
        hintPanelEnabled = enabled;
        if (enabled) {

            if (hintLabel != null) {
                hintLabel.remove();
            }
            hintLabel = new Label(currentRiddle.getRiddle(
                    getGame().getMyBundle().getLocale().getLanguage()).hint, labelStyle);
            hintLabel.setPosition(
                    hintPanelBG.getX() + hintPanelBG.getSizeX() / 2 - hintLabel.getWidth() / 2,
                    hintPanelBG.getY() + hintPanelBG.getSizeY() / 2 - hintLabel.getHeight() / 4);
            hintPanel.addActor(hintLabel);
            hintPanel.remove();
            stage.addActor(hintPanel);
            hintPanel.addAction(Actions.moveBy(0, hintPanelAnimMovement, 0.5f, Interpolation.pow2));
            touchDetector.remove();
            stage.addActor(touchDetector);
            touchDetector.setPosition(0, 0);

        } else {
            if (!hintUsed) {
                hintButton.addAction(
                        Actions.moveBy(-hintButtonAnimMovement, 0, 0.5f, Interpolation.pow2));
            }
            hintPanel.addAction(
                    Actions.moveBy(0, -hintPanelAnimMovement, 0.5f, Interpolation.pow2));
            touchDetector.setPosition(Vescape.GUI_VIEWPORT_WIDTH, 0);
        }
    }

    private void enableKeyboard(boolean enabled) {
        Gdx.input.setOnscreenKeyboardVisible(enabled);

        if (keyboardEnabled == enabled)
            return;

        keyboardEnabled = enabled;

        if (enabled) {
            touchDetector.setPosition(0, 0);
            for (int i = 0; i < Vescape.TOTAL_RIDDLES_ROOM; ++i) {
                answerResultSlots[i].remove();
                stage.addActor(answerResultSlots[i]);
                if (answerResults[i] != null) {
                    answerResults[i].remove();
                    stage.addActor(answerResults[i]);
                }

            }

            burgerButton.reAddElementsToStage();
        } else {
            touchDetector.setPosition(Vescape.GUI_VIEWPORT_WIDTH, 0);
        }

        int direction = enabled ? 1 : -1;
        float movement = direction * (
                Vescape.GUI_VIEWPORT_HEIGHT / 2 - answerButton.getHeight() - 150);
        float animTime = 0.3f;

        answerField.addAction(Actions.moveBy(0, movement,
                animTime, Interpolation.pow2));
        answerFieldBG.addAction(Actions.moveBy(0, movement,
                animTime, Interpolation.pow2));
        riddlePanel.addAction(Actions.moveBy(0, movement,
                animTime, Interpolation.pow2));
        hintButton.addAction(Actions.moveBy(0, movement,
                animTime, Interpolation.pow2));
    }

    private void roomCompleted() {
        int score = correctAnswerCount;
        float rawScore = (float) correctAnswerCount - hintsUsedCount * Vescape.HINT_PENALTY;
        //score = (int)Math.round((rawScore / Vescape.TOTAL_RIDDLES_ROOM) * 3);
        if (score == 3 && hintsUsedCount > 0) {
            --score;
        }
        roomData.latestScore = score;
        if (roomData.latestScore > roomData.highscore) {
            roomData.highscore = roomData.latestScore;
        }

        new RoomFinishedPopUp(this, roomData);
    }

    private void updateTexts() {
        answerButton.setText(getGame().getMyBundle().get("answerButton"));
        if (riddleLabel != null) {
            riddleLabel.setText(currentRiddle.getRiddle(
                    getGame().getMyBundle().getLocale().getLanguage()).riddle);
        }
    }

    private void closeHintPanel() {
        enabledHintPanel(false);
        if (!hintUsed) {
            hintUsed = true;
            hintsUsedCount++;
        }
    }

}
