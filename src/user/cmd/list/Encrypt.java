package user.cmd.list;

import com.wx.action.IoAction;
import com.wx.action.arg.ArgumentsSupplier;
import com.wx.console.UserConsoleInterface;
import com.wx.crypto.CryptoException;
import com.wx.io.file.FileUtil;
import crypter.CrypterInfo;
import key.KeyBuffer;
import question.QuestionsHandler;
import question.model.Question;
import question.reader.QuestionsWriter;
import user.cmd.Main;
import util.FileEncrypt;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created on 23/08/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Encrypt implements IoAction {

    @Override
    public void executeIo(UserConsoleInterface in, ArgumentsSupplier args) throws IOException {
        if (!args.supplyBoolean()) {
            Main.disableLogger();
        }
        File inputFile = FileUtil.getExistingFile(args.supplyString());
        List<Question> questions = QuestionsWriter.readQuestionsFromTextFile(args.supplyString());
        CrypterInfo crypterInfo = Util.initCrypterInfo(args);

        File outputFile = FileUtil.getFreshFile(new File(System.getProperty("user.dir")), inputFile.getName(), Main.ENCRYPTED_EXTENSION);



        KeyBuffer keyBuffer = crypterInfo.createKeyBuffer();
        QuestionsHandler handler = new QuestionsHandler(questions);
        try {
            handler.feedQuestions(keyBuffer, in, true);


            FileEncrypt encryptedFile = new FileEncrypt(outputFile, crypterInfo, questions, keyBuffer);
            encryptedFile.write(inputFile);

        } catch (CryptoException e) {
            throw new IOException(e);
        }
    }
}
