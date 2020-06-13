package crypter;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.crypto.cipher.AESCrypter;
import com.wx.crypto.cipher.BlowfishCrypter;
import com.wx.crypto.cipher.DESCrypter;
import com.wx.crypto.hash.PassKey;
import com.wx.crypto.symmetric.SymmetricCrypter;
import key.ConcatBuffer;
import key.HashXORBuffer;
import key.KeyBuffer;

/**
 * Container of all the parameters of a {@code Crypter}
 *
 * Created on 16/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CrypterInfo {

    public enum Method {
        AES,
        DES,
        BlowFish,
        Symmetric
    }
    public enum KeyBufferMethod {
        CONCAT,
        HASH_XOR
    }

    private Method method;
    private int keyLength;
    private KeyBufferMethod keyBufferMethod;


    public CrypterInfo() {
        this(Method.AES, 128, KeyBufferMethod.HASH_XOR);
    }

    public CrypterInfo(Method method, int keyLength, KeyBufferMethod keyBufferMethod) {
        this.method = method;
        this.keyLength = keyLength;
        this.keyBufferMethod = keyBufferMethod;
    }

    public Method getMethod() {
        return method;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public KeyBufferMethod getKeyBufferMethod() {
        return keyBufferMethod;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public void setKeyBufferMethod(KeyBufferMethod keyBufferMethod) {
        this.keyBufferMethod = keyBufferMethod;
    }

//    public Crypter initCrypter(char[] password) throws CryptoException {
//        Crypter crypter = createCrypter();
//        crypter.setKeyLength(keyLength);
//
//        crypter.generateKey(password, SALT, hashIterations, hashAlgorithm);
//
//        return crypter;
//    }

    public Crypter initCrypter(byte[] key) throws CryptoException {
        Crypter crypter = createCrypter();
        crypter.setKeyLength(keyLength);
        crypter.initKey(key);

        return crypter;
    }

    public Crypter initCrypter(KeyBuffer key) throws CryptoException {
        Crypter crypter = createCrypter();
        crypter.setKeyLength(keyLength);
        key.loadKeyIn(crypter);

        return crypter;
    }

    public KeyBuffer createKeyBuffer() {
        switch (keyBufferMethod) {
            case CONCAT:
                return new ConcatBuffer();
            case HASH_XOR:
                return new HashXORBuffer();
            default:
                throw new AssertionError();
        }
    }

    private Crypter createCrypter() {
        switch (method) {
            case AES:
                return new AESCrypter();
            case DES:
                return new DESCrypter();
            case BlowFish:
                return new BlowfishCrypter();
            case Symmetric:
                return new SymmetricCrypter();
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "{" +
                "method=" + method +
                ", keyLength=" + keyLength +
                ", keyBufferMethod=" + keyBufferMethod +
                '}';
    }
}
