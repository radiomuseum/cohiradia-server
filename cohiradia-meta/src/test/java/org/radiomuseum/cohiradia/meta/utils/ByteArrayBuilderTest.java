package org.radiomuseum.cohiradia.meta.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ByteArrayBuilderTest {

    @Test
    void t1() {
        var actual = ByteArrayBuilder.create(4).append("ue", 4).toByteArray();
        var expected = new byte[]{'u', 'e', '\0', '\0'};
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void t2() {
        var actual = ByteArrayBuilder.create(4).append(null, 4).toByteArray();
        var expected = new byte[]{'\0', '\0', '\0', '\0'};
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}