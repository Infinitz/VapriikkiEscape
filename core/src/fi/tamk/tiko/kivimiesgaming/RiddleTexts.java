package fi.tamk.tiko.kivimiesgaming;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for storing riddle texts(riddle, answer, hint)
 */

public class RiddleTexts {

    /**
     * Language of this text
     */
    public String language;

    /**
     * Riddle
     */
    public String riddle;

    /**
     * Hint for the riddle
     */
    public String hint;

    /**
     * All possible answer alternatives
     */
    private String[] answers;

    /**
     * Constructor for the riddle text
     *
     * @param language Language of this text
     * @param riddle Riddle
     * @param answer All possible answer alternatives
     * @param hint Hint for the riddle
     */
    public RiddleTexts(String language, String riddle, String answer, String hint) {
        this.language = language;
        this.answers = answer.split(Vescape.RIDDLE_ANSWER_SEPARATOR);
        this.riddle = Utilities.splitTextIntoLines(riddle, Vescape.MAX_CHARS_PER_LINE);
        this.hint = Utilities.splitTextIntoLines(hint, Vescape.MAX_CHARS_PER_LINE);
    }

    /**
     * Is the given answer correct
     *
     * @param answer Answer to be checked
     * @return True if answer is correct
     */
    public boolean isCorrectAnswer(String answer) {
        for (int i = 0; i < answers.length; ++i) {
            if (answers[i].equalsIgnoreCase(answer)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns correct answer
     *
     * @return correct answer
     */
    public String getAnswer() {
        return answers[0];
    }
}
