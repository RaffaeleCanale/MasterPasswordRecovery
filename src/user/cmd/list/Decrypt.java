package user.cmd.list;

import com.wx.action.IoAction;
import com.wx.action.arg.ArgumentsSupplier;
import com.wx.console.UserConsoleInterface;
import com.wx.crypto.CryptoException;
import com.wx.io.file.FileUtil;
import key.KeyBuffer;
import question.QuestionsHandler;
import user.cmd.Main;
import util.FileDecrypt;

import java.io.File;
import java.io.IOException;

import static user.cmd.Main.LOG;

/**
 * Created on 23/08/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Decrypt implements IoAction {

    @Override
    public void executeIo(UserConsoleInterface in, ArgumentsSupplier args) throws IOException {
        if (!args.supplyBoolean()) {
            Main.disableLogger();
        }
        File encryptedFile = FileUtil.getExistingFile(args.supplyString());

        if (!encryptedFile.getName().endsWith(Main.ENCRYPTED_EXTENSION)) {
            throw new IllegalArgumentException("Only file with the extension '" + Main.ENCRYPTED_EXTENSION + "' are supported");
        }

        File clearFile = getClearFile(encryptedFile.getName());

        try {
            FileDecrypt file = new FileDecrypt(encryptedFile);
            file.decodeMetadata();

            QuestionsHandler handler = new QuestionsHandler(file.getQuestions());
            KeyBuffer key = file.getCrypterInfo().createKeyBuffer();

            in.getConsole().println("This program does not record the answers of any question. It generates a password from the aggregation of the answers and tries to use it to decode the file");
            handler.feedQuestions(key, in, true);

            file.decodeData(clearFile, key);
            LOG.info("File decoded to " + clearFile.getAbsolutePath());
        } catch (CryptoException e) {
            throw new IOException(e);
        }
    }


    private File getClearFile(String fileName) {
        String strippedPath = fileName.substring(0, fileName.length() - Main.ENCRYPTED_EXTENSION.length());

        String extension = FileUtil.fileExtension(strippedPath);
        String name = FileUtil.nameWithoutExtension(strippedPath);

        return FileUtil.getFreshFile(new File(System.getProperty("user.dir")), name, extension);
    }

}
