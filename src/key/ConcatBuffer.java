package key;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.crypto.CryptoUtil;
import com.wx.crypto.cipher.AESCrypter;

/**
 * Implementation of a {@link KeyBuffer} that generate keys using the concatenation of all content fed.
 *
 * @author canale
 */
public class ConcatBuffer implements KeyBuffer {

    private static final byte[] SALT = "af4qfq3ccsaer44afsdg4f".getBytes();

    private final StringBuilder builder = new StringBuilder();

    @Override
    public void feed(String value) throws CryptoException {
        builder.append(value);
    }

    @Override
    public void loadKeyIn(Crypter crypter) throws CryptoException {
        if (builder.length() == 0) {
            throw new IllegalStateException("This buffer has no content");
        }
        crypter.generateKey(builder.toString().toCharArray(), SALT);
    }

    @Override
    public String toString() {
        try {
            AESCrypter crypter = new AESCrypter();
            crypter.generateKey(builder.toString().toCharArray(), SALT);

            return CryptoUtil.encodeHex(crypter.getKey());
        } catch (CryptoException e) {
            return "Failed to setup key";
        }
    }
}
