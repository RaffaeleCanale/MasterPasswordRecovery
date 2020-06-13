package question.reader;

import com.wx.io.Accessor;
import com.wx.io.file.FileUtil;
import question.model.AnswerProcessor;
import question.model.Question;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static user.cmd.Main.LOG;

/**
 * Created on 16/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class QuestionsWriter {

    public static void writeQuestions(List<Question> questions, Accessor out) throws IOException {
        out.writeInt(questions.size());

        for (Question question : questions) {
            Util.writeEnum(question.getMode(), out);
            out.writeUTF(question.getQuestion());
        }

        out.flush();
    }
    
    public static List<Question> readQuestions(Accessor in) throws IOException {
        int count = in.readInt();
        if (count < 0 || count > 10) {
            throw new IOException("Invalid questions number read: " + count);
        }
        
        List<Question> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            AnswerProcessor mode = Util.readEnum(in, AnswerProcessor.class);
            String question = in.readUTF();
            
            result.add(new Question(question, mode));
        }

        return result;
    }

    public static List<Question> readQuestionsFromTextFile(String filePath) throws IOException {
        File questionsFile = FileUtil.getExistingFile(filePath);

        try (Accessor accessor = new Accessor().setIn(questionsFile)) {
            String[] lines = accessor.readText().split("\n");

            List<Question> result = new ArrayList<>();
            for (String line : lines) {
                result.add(parseLine(line));
            }
            return result;
        }
    }

    private static Question parseLine(String line) throws IOException {
        String[] fields = line.split("-");
        if (fields.length != 2) {
            throw new IOException("There must be exactly one '-' separator in line: " + line);
        }

        String question = fields[0].trim();
        AnswerProcessor type;
        try {
             type = AnswerProcessor.valueOf(fields[1].trim());
        } catch (IllegalArgumentException e) {
            throw new IOException("The question type is invalid: " + fields[1] + "\nMust be one of: " + Arrays.toString(AnswerProcessor.values()), e);
        }
        return new Question(question, type);
    }

}
