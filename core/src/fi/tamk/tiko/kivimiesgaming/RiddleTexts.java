package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 28-Mar-17.
 */

public class RiddleTexts {

    public String language;
    public String riddle;
    private String[] answers;

    public RiddleTexts(String language, String riddle, String answer) {
        this.language = language;
        this.answers = answer.split(Vescape.RIDDLE_ANSWER_SEPARATOR);

        int currentLineStart = 0;
        String riddleText = "";
        while (true) {
            //rivi
            int currentIndex =  currentLineStart + Vescape.MAX_CHARS_PER_LINE;

            if (currentIndex >= riddle.length()) {
                currentIndex = riddle.length() - 1;
                riddleText += riddle.substring(currentLineStart,
                        currentIndex);
                break;
            }

            while (true) {
                if (riddle.charAt(currentIndex) == ' ') {
                    riddleText += riddle.substring(currentLineStart,
                            currentIndex) + "\n";
                    currentLineStart = currentIndex + 1;
                    break;
                } else if (currentIndex == currentLineStart) {

                    currentIndex = currentLineStart + Vescape.MAX_CHARS_PER_LINE - 1;

                    if (currentIndex >= riddle.length()) {
                        currentIndex = riddle.length() - 1;

                        riddleText += riddle.substring(currentLineStart,
                                currentIndex);
                        break;
                    } else {
                        riddleText += riddle.substring(currentLineStart,
                                currentIndex) + "\n";
                        currentLineStart = currentIndex + 1;

                        break;
                    }
                }
                --currentIndex;
            }
        }
        this.riddle = riddleText;
    }

    public boolean isCorrectAnswer(String answer) {
        for (int i = 0; i < answers.length; ++i) {
            if (answers[i].equalsIgnoreCase(answer)) {
                return true;
            }
        }
        return false;
    }
}
