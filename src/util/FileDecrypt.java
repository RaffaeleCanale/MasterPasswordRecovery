package util;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.io.Accessor;
import com.wx.io.CounterInputStream;
import crypter.CrypterFactory;
import crypter.CrypterInfo;
import key.KeyBuffer;
import question.model.Question;
import question.reader.QuestionsWriter;
import user.cmd.Main;

import java.io.*;
import java.util.List;

import static user.cmd.Main.LOG;
import static user.cmd.Main.VERSION;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=MasterPasswordRecovery">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 04.09.16.
 */
public class FileDecrypt {

    private final File encryptedFile;
    private final Crypter metadataCrypter;

    private long dataIndex = -1;
    private CrypterInfo crypterInfo;
    private List<Question> questions;

    public FileDecrypt(File encryptedFile) throws CryptoException {
        this.encryptedFile = encryptedFile;
        this.metadataCrypter = CrypterFactory.initDefaultCrypter();
    }

    public void decodeMetadata() throws IOException, CryptoException {

        byte[] metadata;
        try (Accessor accessor = new Accessor().setIn(encryptedFile)) {
            double version = accessor.readDouble();
            LOG.info("File version: " + version);

            if (version != VERSION) {
                LOG.warning("Version does not match (current version: " + VERSION);
            }

            int metadataSize = accessor.readInt();
            metadata = new byte[metadataSize];

            accessor.readFully(metadata);
            dataIndex = 12 + metadataSize;
            LOG.fine("Metadata read (" + metadataSize + " bytes)");
        }


        try (Accessor accessor = new Accessor().setInCrypter(new ByteArrayInputStream(metadata), metadataCrypter, false)) {
            crypterInfo = CrypterFactory.readCrypterInfo(accessor);
            LOG.info("File crypter info: " + crypterInfo);
            questions = QuestionsWriter.readQuestions(accessor);
            LOG.info("Decoded " + questions.size() + " questions");
        }
    }

    public void decodeData(File output, KeyBuffer key) throws IOException, CryptoException {
        Crypter dataCrypter = crypterInfo.initCrypter(key);

        try (Accessor accessor = new Accessor()
                .setInCrypter(initDataStream(), dataCrypter, false)
                .setOut(output)) {
            accessor.pourInOut();
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public CrypterInfo getCrypterInfo() {
        return crypterInfo;
    }

    private InputStream initDataStream() throws IOException {
        if (dataIndex < 0) {
            throw new IllegalStateException("Metadata not decoded");
        }

        FileInputStream in = new FileInputStream(encryptedFile);
        long skipped = in.skip(dataIndex);
        if (skipped != dataIndex) {
            throw new IOException("Failed to skip metadata");
        }

        return in;
    }


}
