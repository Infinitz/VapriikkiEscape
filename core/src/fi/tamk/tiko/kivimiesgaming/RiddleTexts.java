package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 28-Mar-17.
 */

public class RiddleTexts {

    public String language;
    public String riddle;
    public String answer;

    public RiddleTexts(String language, String riddle, String answer) {
        this.language = language;
        this.answer = answer;

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
}
