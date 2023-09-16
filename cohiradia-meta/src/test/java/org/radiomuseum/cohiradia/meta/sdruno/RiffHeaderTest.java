package org.radiomuseum.cohiradia.meta.sdruno;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RiffHeaderTest {

    @Test
    @DisplayName("Test filesize")
    public void t1() {
        var header = RiffHeader.builder().filesize(1500_000_000L).build();
        var actual = RiffHeader.create(header.toByteArray());
        Assertions.assertThat(actual.filesize).isEqualTo(1500_000_000L);
    }

}