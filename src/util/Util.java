package util;

import com.wx.action.arg.ArgumentsSupplier;
import com.wx.io.Accessor;
import crypter.CrypterInfo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.stream.Stream;

import static user.cmd.Main.LOG;

/**
 * Created on 16/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Util {

    public static <E extends Enum<E>> void writeEnum(E value, DataOutput accessor) throws IOException {
        accessor.writeByte(value.ordinal());
    }

    public static <E extends Enum<E>> E readEnum(DataInput accessor, Class<E> c) throws IOException {
        byte ordinal = accessor.readByte();

        E[] enumConstants = c.getEnumConstants();
        if (ordinal < 0 || ordinal >= enumConstants.length) {
            throw new IOException("Invalid method ordinal: " + ordinal);
        }

        return enumConstants[ordinal];
    }


    public static CrypterInfo initCrypterInfo(ArgumentsSupplier args) {
        CrypterInfo info = new CrypterInfo();
        info.setKeyBufferMethod(getBufferMethod(args.supplyString()));
        info.setMethod(getAlgorithm(args.supplyString()));
        info.setKeyLength(args.supplyInteger());


        LOG.info("Crypter info: " + info);
        return info;
    }

    private static CrypterInfo.KeyBufferMethod getBufferMethod(String name) {
        switch (name.toLowerCase()) {
            case "xor":
                return CrypterInfo.KeyBufferMethod.HASH_XOR;
            case "concat":
                return CrypterInfo.KeyBufferMethod.CONCAT;
            default:
                throw new IllegalArgumentException("Unknown key buffer: " + name);
        }
    }

    private static CrypterInfo.Method getAlgorithm(String name) {
        return Stream.of(CrypterInfo.Method.values())
                .filter(m -> m.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new IllegalArgumentException("Unknown algorithm: " + name));
    }
}
