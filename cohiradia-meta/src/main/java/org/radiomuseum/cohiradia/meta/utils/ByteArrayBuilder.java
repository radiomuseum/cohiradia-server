package org.radiomuseum.cohiradia.meta.utils;

import lombok.Synchronized;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;


public class ByteArrayBuilder {

    private final byte[] data;
    private int pointer;

    private ByteArrayBuilder(int size) {
        this.data = new byte[size];
        this.pointer = 0;
    }

    public static ByteArrayBuilder create(int size) {
        return new ByteArrayBuilder(size);
    }

    @Synchronized
    public ByteArrayBuilder append(int value) {
        System.arraycopy(ByteUtils.intToByte(value), 0, data, pointer, 4);
        pointer += 4;
        return this;
    }

    @Synchronized
    public ByteArrayBuilder append(long value) {
        System.arraycopy(ByteUtils.longToBytes(value), 0, data, pointer, 4);
        pointer += 4;
        return this;
    }

    @Synchronized
    public ByteArrayBuilder append(short value) {
        System.arraycopy(ByteUtils.shortToByte(value), 0, data, pointer, 2);
        pointer += 2;
        return this;
    }

    @Synchronized
    public ByteArrayBuilder append(String value) {
        byte[] src = value.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(src, 0, data, pointer, src.length);
        pointer += src.length;
        return this;
    }

    @Synchronized
    public ByteArrayBuilder append(String value, int length) {
        var result = new byte[length];
        Arrays.fill(result, (byte) ('\0'));
        if (value != null) {
            System.arraycopy(value.getBytes(), 0, result, 0, value.length());
        }
        System.arraycopy(result, 0, data, pointer, result.length);
        pointer += result.length;
        return this;
    }

    @Synchronized
    public ByteArrayBuilder append(byte[] bytes) {
        System.arraycopy(bytes, 0, data, pointer, bytes.length);
        pointer += bytes.length;
        return this;
    }

    @Synchronized
    public ByteArrayBuilder append(ZonedDateTime value) {
        append((short) value.getYear());
        append((short) value.getMonth().getValue());
        append((short) value.getDayOfWeek().getValue());
        append((short) value.getDayOfMonth());
        append((short) value.getHour());
        append((short) value.getMinute());
        append((short) value.getSecond());
        append((short) ((value.getNano() / 1_000_000)));
        return this;
    }

    public byte[] toByteArray() {
        return data;
    }

}
