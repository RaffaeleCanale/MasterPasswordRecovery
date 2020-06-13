package user;

import com.wx.console.UserConsoleInterface;
import question.model.Question;
import question.model.AnswerProcessor;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 23/08/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class QuestionsCreator {


    public static List<Question> createQuestions(UserConsoleInterface in) {
        List<Question> questions = new LinkedList<>();

        while (true) {
            String nextLine = in.readLine("Enter the next question (or leave empty to stop): ");

            if (nextLine == null || nextLine.isEmpty()) {
                return questions;

            } else {
                in.getConsole().print("Should the answer to this question respect case? ");
                boolean matchCase = in.inputYesNo();

                AnswerProcessor mode = matchCase ? AnswerProcessor.STRIPPED : AnswerProcessor.STRIPPED_LOWERCASE;

                questions.add(new Question(nextLine, mode));
            }
        }
    }

}
