package org.radiomuseum.cohiradia.meta.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ByteUtils {

    public static byte[] intToByte(int value) {
        return new byte[]{
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16),
                (byte) (value >> 24)
        };
    }

    public static byte[] shortToByte(short value) {
        return new byte[]{
                (byte) value,
                (byte) (value >> 8)
        };
    }

    public static int bytesToUint(byte[] data) {
        int firstI = (data[3] & 0xFF) << 24;
        int secondI = (data[2] & 0xFF) << 16;
        int thirdI = (data[1] & 0xFF) << 8;
        int fourthI = (data[0] & 0xFF);

        return firstI | secondI | thirdI | fourthI;
    }

    public static long bytesToLong(byte[] data) {
        byte[] internal = new byte[8];
        System.arraycopy(data, 0, internal, 0, 4);
        return ByteBuffer.wrap(internal).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public static byte[] longToBytes(long data) {
        byte[] internal = new byte[8];
        var result = ByteBuffer.wrap(internal).order(ByteOrder.LITTLE_ENDIAN).putLong(data);
        return Arrays.copyOfRange(internal, 0, 4);
    }

    public static int bytesToUint(byte[] data, int offset) {
        return bytesToUint(Arrays.copyOfRange(data, offset, offset + 4));
    }

    public static short bytesToShort(byte[] data, int offset) {
        return bytesToShort(Arrays.copyOfRange(data, offset, offset + 2));
    }

    public static short bytesToShort(byte[] data) {
        int thirdI = (data[1] & 0xFF) << 8;
        int fourthI = (data[0] & 0xFF);
        return (short) (thirdI | fourthI);
    }
}
