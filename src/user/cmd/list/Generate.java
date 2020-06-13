package user.cmd.list;

import com.wx.action.IoAction;
import com.wx.action.arg.ArgumentsSupplier;
import com.wx.console.UserConsoleInterface;
import com.wx.console.color.Color;
import com.wx.crypto.CryptoException;
import crypter.CrypterInfo;
import key.KeyBuffer;
import question.QuestionsHandler;
import question.model.Question;
import question.reader.QuestionsWriter;
import user.cmd.Main;
import util.Util;

import java.io.IOException;
import java.util.List;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=MasterPasswordRecovery">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 04.09.16.
 */
public class Generate implements IoAction {


    @Override
    public void executeIo(UserConsoleInterface in, ArgumentsSupplier args) throws IOException {
        if (!args.supplyBoolean()) {
            Main.disableLogger();
        }
        List<Question> questions = QuestionsWriter.readQuestionsFromTextFile(args.supplyString());
        CrypterInfo crypterInfo = Util.initCrypterInfo(args);

        KeyBuffer keyBuffer = crypterInfo.createKeyBuffer();
        QuestionsHandler handler = new QuestionsHandler(questions);
        try {
            handler.feedQuestions(keyBuffer, in, true);

            in.getConsole().print("Generated password: ");
            in.getConsole().setColor(Color.Red);
            in.getConsole().println(keyBuffer);
            in.getConsole().resetColor();
        } catch (CryptoException e) {
            throw new IOException(e);
        }
    }
}
