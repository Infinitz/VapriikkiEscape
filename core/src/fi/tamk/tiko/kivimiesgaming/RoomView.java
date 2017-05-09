package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by atter on 22-Mar-17.
 */

public class RoomView extends MyScreen {

    protected int riddlesInRoom = 0;

    private RoomData roomData;
    private Riddle currentRiddle;

    private Group riddlePanel;
    private Label riddleLabel;

    private Group hintPanel;
    private ImageActor hintPanelBG;
    private Label hintLabel;
    private Group hintGroup;

    private BurgerButton burgerButton;
    private ImageActor bg;
    private TextField answerField;
    private TextButton answerButton;
    private ImageActor touchDetector;
    private ImageActor answerFieldBG;
    private ImageActor[] answerResultSlots;
    private ImageActor[] answerResults;
    private ImageActor roomIcon;

    private Label.LabelStyle labelStyle;
    private Texture riddlePanelTextureBg;
    private Texture riddlePanelTextureFill;

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
    private boolean roomFinished = false;

    private float hintButtonAnimMovement = 300f;
    private float hintPanelAnimMovement = 700f;

    public RoomView(Vescape game, RoomData roomData, AssetManager assetManager) {
        super(game, assetManager);
        this.roomData = roomData;
        assetManager.load("riddle_answer_box.jpg", Texture.class);
        assetManager.load("riddle_info_box_border.png", Texture.class);
        assetManager.load("riddle_info_box_fill.png", Texture.class);

        assetManager.load("riddle_slot_empty.png", Texture.class);
        assetManager.load("riddle_slot_nope.png", Texture.class);
        assetManager.load("riddle_slot_done.png", Texture.class);
        assetManager.load("riddle_slot_done_golden.png", Texture.class);
        assetManager.load("riddle_retry_active.png", Texture.class);
        assetManager.load("riddle_next_active.png", Texture.class);
        assetManager.load("riddle_confirm_box.png", Texture.class);
        assetManager.load("riddle_hint.png", Texture.class);
        assetManager.load("hint_glow.png", Texture.class);

        assetManager.load("star_enter.wav", Sound.class);
        assetManager.load("star_hit.wav", Sound.class);

        assetManager.load("answer_perfect.wav", Sound.class);

        assetManager.load("answer_result_hit.wav", Sound.class);
        roomData.loadRiddles(assetManager);
        roomData.loadTextures();

        labelStyle = new Label.LabelStyle(getGame().getRiddleFont(),
                Color.BLACK);

        riddlesInRoom = Vescape.TOTAL_RIDDLES_ROOM;
    }

    @Override
    public void onStart() {
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

        riddlePanelTextureBg = assetManager.get("riddle_info_box_border.png", Texture.class);
        riddlePanelTextureFill = assetManager.get("riddle_info_box_fill.png", Texture.class);

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
        stage.setKeyboardFocus(answerField);

        roomIcon = new ImageActor(roomData.getIconTexture(), 150f);
        roomIcon.setPosition(0, game.GUI_VIEWPORT_HEIGHT - roomIcon.getSizeY() - 25);

        answerResultSlots = new ImageActor[riddlesInRoom];
        answerResults = new ImageActor[riddlesInRoom];
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
        answerButton.setDisabled(true);
        answerButton.setColor(0.2f, 0f, 0.2f, 0.5f);
        answerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound("button_press.wav");
                if (keyboardEnabled) {
                    enableKeyboard(false);
                }
                answer(answerField.getText());
                answerField.setText("");
                answerButton.setDisabled(true);
                answerButton.setColor(0.2f, 0f, 0.2f, 0.5f);
            }
        });
        answerButton.setSize(Vescape.GUI_VIEWPORT_WIDTH / 2, 175);
        answerButton.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - answerButton.getWidth() / 2,
                0);

        // Just for testing
        TextButton rightAnswerButton = new TextButton("",
                getGame().getTextButtonStyle());
        rightAnswerButton.setSize(Vescape.GUI_VIEWPORT_WIDTH / 4, 175);
        rightAnswerButton.setPosition(0, 0);
        rightAnswerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                answer(currentRiddle.getRiddle(
                        getGame().getMyBundle().getLocale().getLanguage()).getAnswer());
            }
        });


        answerField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (textField.getText().length() == 0) {
                    answerButton.setDisabled(true);
                    answerButton.setColor(0.2f, 0f, 0.2f, 0.5f);
                } else {
                    answerButton.setDisabled(false);
                    answerButton.setColor(1, 1, 1, 1f);
                }
            }
        });

        hintPanel = new Group();
        hintPanelBG = new ImageActor(
                assetManager.get("riddle_confirm_box.png", Texture.class));
        hintPanelBG.setSize(hintPanelBG.getSizeY() *
                (Vescape.GUI_VIEWPORT_WIDTH / hintPanelBG.getSizeX()));
        hintPanelBG.setY(Vescape.GUI_VIEWPORT_HEIGHT / 2 - hintPanelBG.getSizeY() / 2 - 320);
        hintPanel.setY(hintPanel.getY() - hintPanelAnimMovement);
        hintPanel.addActor(hintPanelBG);

        hintGroup = new Group();
        ImageActor hintButton = new ImageActor(assetManager.get("riddle_hint.png", Texture.class), 150f);
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

        ImageActor hintGlow = new ImageActor(
                assetManager.get("hint_glow.png", Texture.class), hintButton.getSizeY() * 1.5f);

        hintGlow.setPosition(
                hintButton.getX() - hintGlow.getSizeX() / 2  + hintButton.getSizeX() / 2,
                hintButton.getY() - hintGlow.getSizeY() / 2 + hintButton.getSizeY() / 2 + 25
        );

        hintGroup.addActor(hintButton);
        hintGroup.addActor(hintGlow);
        hintGlow.addAction(Actions.forever(
                Actions.parallel(
                        Actions.sequence(
                                Actions.scaleTo(1.1f, 1.1f, 1),
                                Actions.scaleTo(0.8f, 0.8f, 1)
                        ),
                        Actions.rotateBy(120, 2)
                )


        ));

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

        hintGroup.setX(hintButtonAnimMovement);

        answerField.setPosition(answerField.getX(), answerField.getY() - deltaY);
        answerField.addAction(Actions.moveBy(0, deltaY, animLength, Interpolation.pow2));

        answerButton.setPosition(answerButton.getX(), answerButton.getY() - deltaY);
        answerButton.addAction(Actions.moveBy(0, deltaY, animLength, Interpolation.pow2));



        stage.addActor(rightAnswerButton);
        stage.addActor(answerFieldBG);
        stage.addActor(answerButton);
        burgerButton = new BurgerButton(this);
        stage.addActor(roomIcon);
        stage.addActor(answerField);
        stage.addActor(hintGroup);
        stage.addActor(touchDetector);
        createNewPanelWithAnimation(animLength);

    }

    public void answer(String playerAnswer) {
        playerAnswer = playerAnswer.trim();
        boolean correctAnswer = currentRiddle.getRiddle(
                getGame().getMyBundle().getLocale().getLanguage()).isCorrectAnswer(playerAnswer);

        if (correctAnswer) {
            ++currentRiddleCount;
            correctAnswerCount++;
            if (hintUsed) {
                hintsUsedCount++;
                //AudioManager.playSound("answer_right.wav");
                answerResults[currentRiddleCount - 1] =
                        new ImageActor(correctAnswerTex, answerResultSlots[0].getSizeY());
            } else {
                AudioManager.playSound("answer_perfect.wav");
                answerResults[currentRiddleCount - 1] =
                        new ImageActor(perfectAnswerTex, answerResultSlots[0].getSizeY());
            }
        } else if (!hintUsed){
            hintGroup.addAction(
                    Actions.moveBy(-hintButtonAnimMovement, 0, 0.5f, Interpolation.pow2));
            hintUsed = true;

            return;
        } else {
            ++currentRiddleCount;
            answerResults[currentRiddleCount - 1] =
                    new ImageActor(wrongAnswerTex, answerResultSlots[0].getSizeY());
        }

        if (hintUsed) {
            hintGroup.addAction(
                    Actions.moveBy(hintButtonAnimMovement, 0, 0.5f, Interpolation.pow2));
        }


        answerField.setDisabled(true);
        answerField.setText("");

        answerResults[currentRiddleCount - 1].setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2,
                3 * Vescape.GUI_VIEWPORT_HEIGHT / 2);
        answerResults[currentRiddleCount - 1].setScale(10f);
        getStage().addActor(answerResults[currentRiddleCount - 1]);
        answerResults[currentRiddleCount - 1].addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(
                                Vescape.GUI_VIEWPORT_WIDTH / 2 -
                                        answerResults[currentRiddleCount - 1].getSizeX() / 2,
                                Vescape.GUI_VIEWPORT_HEIGHT / 2 -
                                        answerResults[currentRiddleCount - 1].getSizeY() / 2, 0.3f,
                                Interpolation.pow2),
                        Actions.scaleTo(3.5f, 3.5f, 0.6f, Interpolation.bounceOut),
                        Actions.rotateTo(((float)Math.random() - 0.5f) * 25f, 0.35f),
                        Actions.sequence(
                                Actions.delay(0.25f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        AudioManager.playSound("answer_result_hit.wav");
                                    }
                                })
                                )
                ),
                Actions.delay(0.25f),

                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        nextRiddle();
                    }
                }),
                Actions.parallel(
                        Actions.moveTo(answerResultSlots[currentRiddleCount - 1].getX(),
                                answerResultSlots[currentRiddleCount - 1].getY(),
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
                                if (currentRiddleCount == riddlesInRoom) {
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
                        if (currentRiddleCount == riddlesInRoom) {
                            roomCompleted();
                        }
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
        hintGroup.remove();
        stage.addActor(hintGroup);
        roomIcon.remove();
        stage.addActor(roomIcon);
        burgerButton.reAddElementsToStage();
    }

    private void createNewRiddlePanel() {
        if (currentRiddleCount == riddlesInRoom - 1 && roomData.lastRiddle != null) {
            currentRiddle = roomData.lastRiddle;
        } else {
            currentRiddle = roomData.getRandomRiddle();
        }

        ImageActor riddlePanelBg = new ImageActor(riddlePanelTextureBg);
        ImageActor riddlePanelFill = new ImageActor(riddlePanelTextureFill);

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH - 20;
        riddlePanelBg.setSize(riddlePanelBg.getSizeY() * (panelTargetW / riddlePanelBg.getSizeX()));
        riddlePanelBg.setPosition((Vescape.GUI_VIEWPORT_WIDTH - riddlePanelBg.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - riddlePanelBg.getSizeY()) - 130);
        riddlePanelFill.setSize(riddlePanelBg.getSizeY());
        riddlePanelFill.setPosition(riddlePanelBg.getX(), riddlePanelBg.getY());
        riddlePanelFill.alpha = 0.8f;

        final ImageActor riddleImage = new ImageActor(
                assetManager.get(currentRiddle.imagePath, Texture.class),
                2 * riddlePanelBg.getSizeX() / 3);

        riddleImage.setPosition(
                riddlePanelBg.getX() + (riddlePanelBg.getSizeX() - riddleImage.getWidth()) / 2,
                riddlePanelBg.getY() + riddlePanelBg.getSizeY() - riddleImage.getHeight() - 35);

        riddleImage.setClickListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                riddleImage.remove();
                final float animationDuration = 0.25f;
                final ImageActor temp = new ImageActor(assetManager.get("black.png", Texture.class),
                        Vescape.GUI_VIEWPORT_HEIGHT);
                temp.alpha = 0f;
                riddlePanel.addActor(riddleImage);
                stage.addActor(temp);
                riddleImage.addAction(
                        Actions.parallel(
                                Actions.scaleTo(
                                    Vescape.GUI_VIEWPORT_WIDTH / riddleImage.getSizeX(),
                                    Vescape.GUI_VIEWPORT_WIDTH / riddleImage.getSizeX(),
                                    animationDuration,
                                    Interpolation.pow2),
                                Actions.moveBy(0, -200, animationDuration, Interpolation.pow2)
                                ));

                temp.setClickListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        riddleImage.addAction(
                                Actions.parallel(
                                        Actions.scaleTo(
                                            1,
                                            1,
                                            animationDuration,
                                            Interpolation.pow2),
                                        Actions.moveBy(
                                                0,
                                                200,
                                                animationDuration,
                                                Interpolation.pow2)
                                        ));

                        temp.remove();
                    }
                });
            }
        });

        riddleLabel = new Label(
                currentRiddle.getRiddle(getGame().getMyBundle().getLocale().getLanguage()).riddle,
                labelStyle);
        riddleLabel.setPosition(
                riddlePanelBg.getX() + 40,
                riddleImage.getY() - riddleLabel.getHeight() - 30);

        Group g = new Group();
        g.addActor(riddlePanelFill);
        g.addActor(riddlePanelBg);
        g.addActor(riddleImage);
        g.addActor(riddleLabel);
        riddlePanel = g;
    }

    @Override
    protected void update(float dt) {
        if (nextScreen != null)
            return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            if (roomFinished) {
                setNextScreen(new RoomSelection(
                        getGame(), assetManager, roomData.type.isBottomFloor()));
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
                AudioManager.playSound("button_press.wav");
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
                AudioManager.playSound("button_press.wav");
                setNextScreen(new RoomSelection(getGame(), assetManager,
                        roomData.type.isBottomFloor()));
            }
        });

        return button;
    }

    @Override
    public void dispose() {
        super.dispose();
        roomData.unloadRiddleImages(assetManager);
        assetManager.unload("riddle_slot_empty.png");
        assetManager.unload("riddle_slot_nope.png");
        assetManager.unload("riddle_slot_done.png");
        assetManager.unload("riddle_slot_done_golden.png");
        assetManager.unload("hint_glow.png");
        assetManager.unload("riddle_info_box_border.png");
        assetManager.unload("riddle_info_box_fill.png");

        assetManager.unload("star_enter.wav");
        assetManager.unload("star_hit.wav");
        assetManager.unload("answer_perfect.wav");
        assetManager.unload("answer_result_hit.wav");
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

            for (int i = 0; i < riddlesInRoom; ++i) {
                answerResultSlots[i].remove();
                stage.addActor(answerResultSlots[i]);
                if (answerResults[i] != null) {
                    answerResults[i].remove();
                    stage.addActor(answerResults[i]);
                }

            }

            burgerButton.reAddElementsToStage();
            touchDetector.remove();
            stage.addActor(touchDetector);

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
        hintGroup.addAction(Actions.moveBy(0, movement,
                animTime, Interpolation.pow2));
    }

    private void roomCompleted() {
        float rawScore = (float) correctAnswerCount - hintsUsedCount * Vescape.HINT_PENALTY + 0.33f;
        int score = Math.round((rawScore / riddlesInRoom) * 3);
        roomData.latestScore = score;
        roomFinished = true;
        new RoomFinishedPopUp(this, roomData);
        getGame().saveScores();
    }

    private void updateTexts() {
        answerButton.setText(getGame().getMyBundle().get("answerButton"));

        String updatedRiddleText = currentRiddle.getRiddle(
                getGame().getMyBundle().getLocale().getLanguage()).riddle;

        if (riddleLabel != null &&
                !riddleLabel.getText().toString().equalsIgnoreCase(updatedRiddleText)) {

            float oldX = riddleLabel.getX();
            float oldY = riddleLabel.getY();
            float oldH = riddleLabel.getHeight();
            riddleLabel.remove();
            riddleLabel = new Label(updatedRiddleText, labelStyle);
            riddleLabel.setPosition(
                    oldX,
                    oldY - riddleLabel.getHeight() + oldH );
            riddlePanel.addActor(riddleLabel);

        }
    }

    private void closeHintPanel() {
        enabledHintPanel(false);
    }

}
