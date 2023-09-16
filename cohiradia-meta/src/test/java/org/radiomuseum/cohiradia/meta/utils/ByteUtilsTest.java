package org.radiomuseum.cohiradia.meta.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class ByteUtilsTest {

    @Test
    public void t1() {
        var actual = ByteUtils.intToByte(126308);
        Assertions.assertThat(ByteUtils.bytesToUint(actual)).isEqualTo(126308);
    }

    @Test
    public void t2() {
        var actual = ByteUtils.intToByte(Integer.MAX_VALUE);
        Assertions.assertThat(ByteUtils.bytesToUint(actual)).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void t3() {
        var input = new byte[]{(byte) 0XD0, (byte) 0X28, (byte) 0X9A, (byte) 0XB2};
        Assertions.assertThat(ByteUtils.bytesToLong(input)).isEqualTo(2996447440L);
    }

    @Test
    public void t4() {
        var result = new byte[]{(byte) 0XD0, (byte) 0X28, (byte) 0X9A, (byte) 0XB2};
        Assertions.assertThat(ByteUtils.longToBytes(2996447440L)).isEqualTo(result);
    }

    @Test
    public void t5() {
        Assertions.assertThat(1L).isEqualTo(1);
    }

}