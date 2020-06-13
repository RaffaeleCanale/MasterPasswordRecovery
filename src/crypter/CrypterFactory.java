package crypter;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.io.Accessor;
import util.Util;

import java.io.IOException;

/**
 * Created on 16/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CrypterFactory {


    private static final byte[] INTERNAL_HASH = {-97, 47, -21, 15, 30, -12, 37, -78, -110, -14, -7, 75, -56, 72, 36, -108, -33, 67, 4, 19};

//    private static final CrypterInfo currentCrypter = new CrypterInfo();
//
//    public static CrypterInfo getCurrentCrypter() {
//        return currentCrypter;
//    }
//
//    public static Crypter initDefaultCrypter(char[] password) throws CryptoException {
//        return currentCrypter.initCrypter(password);
//    }

    public static Crypter initDefaultCrypter() throws CryptoException {
        CrypterInfo defaultCrypter = new CrypterInfo();
        return defaultCrypter.initCrypter(INTERNAL_HASH);
    }

    public static void writeCrypterInfo(CrypterInfo info, Accessor out) throws IOException {

        Util.writeEnum(info.getMethod(), out);
        Util.writeEnum(info.getKeyBufferMethod(), out);
        out.writeInt(info.getKeyLength());

        out.flush();
    }

    public static CrypterInfo readCrypterInfo(Accessor in) throws IOException {

        CrypterInfo.Method method = Util.readEnum(in, CrypterInfo.Method.class);
        CrypterInfo.KeyBufferMethod keyBuffer = Util.readEnum(in, CrypterInfo.KeyBufferMethod.class);
        int keyLength = in.readInt();

        return new CrypterInfo(method, keyLength, keyBuffer);
    }



}
