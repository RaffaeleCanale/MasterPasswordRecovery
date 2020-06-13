package util;

import com.wx.console.UserConsoleInterface;
import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.io.Accessor;
import crypter.CrypterFactory;
import crypter.CrypterInfo;
import key.KeyBuffer;
import question.QuestionsHandler;
import question.model.Question;
import question.reader.QuestionsWriter;
import user.cmd.Main;

import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * Created on 16/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class FileEncrypt {


    private final File encryptedFile;

    private final CrypterInfo crypterInfo;
    private final List<Question> questions;

    private final Crypter metadataCrypter;
    private final Crypter contentCrypter;

    public FileEncrypt(File encryptedFile, CrypterInfo crypterInfo, List<Question> questions, KeyBuffer keyBuffer) throws CryptoException {
        this.encryptedFile = Objects.requireNonNull(encryptedFile);
        this.crypterInfo = Objects.requireNonNull(crypterInfo);
        this.questions = Objects.requireNonNull(questions);

        metadataCrypter = CrypterFactory.initDefaultCrypter();
        contentCrypter = crypterInfo.initCrypter(keyBuffer);
    }


    public void write(File input) throws IOException, CryptoException {
        ByteArrayOutputStream metadataBytes = new ByteArrayOutputStream();
        try (Accessor metadataAccessor = new Accessor().setOutCrypter(metadataBytes, metadataCrypter, true)) {
            CrypterFactory.writeCrypterInfo(crypterInfo, metadataAccessor);
            QuestionsWriter.writeQuestions(questions, metadataAccessor);
        }

        Main.LOG.fine("Writing metadata (" + metadataBytes.size() + " bytes)");
        try (FileOutputStream out = new FileOutputStream(encryptedFile)) {
            Accessor headerAccessor = new Accessor().setOut(out);
            headerAccessor.writeDouble(Main.VERSION);
            headerAccessor.writeInt(metadataBytes.size());
            headerAccessor.flush();

            metadataBytes.writeTo(out);

            Accessor contentAccessor = new Accessor()
                    .setIn(input)
                    .setOutCrypter(out, contentCrypter, true);
            contentAccessor.pourInOut();
            contentAccessor.close();
        }
    }
}
