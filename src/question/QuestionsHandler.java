package question;

import com.wx.console.UserConsoleInterface;
import com.wx.console.color.Color;
import com.wx.crypto.CryptoException;
import key.KeyBuffer;
import question.model.Question;
import question.model.AnswerProcessor;

import java.util.*;

/**
 *
 * @author canale
 */
public class QuestionsHandler {

    private final List<Question> questions;

    public QuestionsHandler(List<Question> questions) {
        this.questions = Collections.unmodifiableList(
                new LinkedList<>(questions));
    }

    public void feedQuestions(KeyBuffer buffer, UserConsoleInterface in,
            boolean secretMode) throws CryptoException {

        in.getConsole().setColor(Color.Yellow);
        in.getConsole().println("Answers all the following questions. "
                + "Beware of the questions marked by *, they are case sensitive.\n\n");
        in.getConsole().resetStyle();

        Map<Question, String> answers = new HashMap<>();

        for (Question question : questions) {
            answers.put(question,
                    askQuestion(question, in, secretMode));
        }

        if (confirm(in, answers)) {
            for (String answer : answers.values()) {
                buffer.feed(answer);
            }
        }
    }

    private String askQuestion(Question question, UserConsoleInterface in,
                               boolean secretMode) {
        in.getConsole().setBold();
        in.getConsole().setColor(Color.Cyan);
        in.getConsole().println(question.getQuestion());
        in.getConsole().resetStyle();

        String prefix = question.getMode() == AnswerProcessor.STRIPPED_LOWERCASE ? "Answer: " : "Answer*: ";
        String result = secretMode ?
                String.valueOf(in.readPassword(prefix)) :
                in.readLine(prefix);

        in.getConsole().println();
        return question.getMode().process(result);
    }

    private boolean confirm(UserConsoleInterface in, Map<Question, String> answers) {
        in.getConsole().setColor(Color.Yellow);
        in.getConsole().println("\n\nAre you satisfied of your answers?"
                + "\nEnter d to display all answers"
                + "\ne to edit"
                + "\nc to cancel"
                + "\nor anything else to continue");
        in.getConsole().resetStyle();
        int choice = in.inputMultipleChar(true, 'd', 'e', 'c', 'k');

        switch (choice) {
            case 0:
                display(answers, in);
                return confirm(in, answers);
            case 1:
                edit(in, answers);
                return confirm(in, answers);

            case 2:
                return false;
            case 3:
                return true;
            default:
                throw new AssertionError();
        }
    }

    private void edit(UserConsoleInterface in, Map<Question, String> answers) {
        int i = 0;
        for (Question question : questions) {
            in.getConsole().println(i + ") " + question.getQuestion());
            i++;
        }
        in.getConsole().setColor(Color.Yellow);
        in.getConsole().println("Choose a question to edit");
        int index = in.readIntIn(0, questions.size() - 1);
        Question question = questions.get(index);
        String newAnswer = askQuestion(question, in, true);
        answers.put(question, newAnswer);
    }

    private void display(Map<Question, String> answers, UserConsoleInterface in) {
        for (Map.Entry<Question, String> pair : answers.entrySet()) {
            in.getConsole().println(pair.getKey().getQuestion());
            in.getConsole().setColor(Color.Blue);
            in.getConsole().println(pair.getValue() + "\n");
            in.getConsole().resetStyle();
        }
    }


}
