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
        this.riddle = Utilities.splitTextIntoLines(riddle, Vescape.MAX_CHARS_PER_LINE);
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
