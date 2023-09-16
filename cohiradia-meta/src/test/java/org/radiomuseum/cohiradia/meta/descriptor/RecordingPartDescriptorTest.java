package org.radiomuseum.cohiradia.meta.descriptor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

class RecordingPartDescriptorTest {

    @Test
    void t1() {
        var actual = RecordingPartDescriptor.builder()
                .duration(100)
                .size(100_000_000_000L)
                .startDate(ZonedDateTime.now()).build();

        Assertions.assertThat(actual.sizeAsGB()).isEqualTo("93.13");
    }

}