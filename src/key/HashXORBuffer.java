package key;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.crypto.CryptoUtil;
import com.wx.crypto.hash.PassKey;
import user.cmd.Main;

/**
 * Implementation of a {@link KeyBuffer} that will hash every content fed to it. The key is then reduced from the XOR
 * concatenation of all the hashes.
 *
 * @author canale
 */
public class HashXORBuffer implements KeyBuffer {

    private byte[] bytes;

    @Override
    public void feed(String value) throws CryptoException {
        PassKey passKey =
                new PassKey(value.toCharArray(), PassKey.SHA_ALGORITHM, false);
        byte[] hash = passKey.getHash();

        if (bytes == null) {
            bytes = hash;
        } else {
            assert hash.length == bytes.length;
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] ^= hash[i];
            }
        }
    }

    @Override
    public void loadKeyIn(Crypter crypter) throws CryptoException {
        if (bytes == null) {
            throw new IllegalStateException("This buffer has no content");
        }

        Main.LOG.info("Generated password: " + this);
        crypter.initKey(bytes);
    }

    @Override
    public String toString() {
        return bytes == null ? "No password generated" : CryptoUtil.encodeHex(bytes);
    }
}
