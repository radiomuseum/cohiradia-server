package org.radiomuseum.cohiradia.meta.sdruno;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class AuxiHeaderTest {

    @Test
    @DisplayName("Timestamp Test")
    public void t1() {
        var time = ZonedDateTime.of(2022, 11, 23, 20, 11, 45, 0, ZoneId.of("UTC"));
        var input = AuxiHeader.builder()
                .startTime(time)
                .stopTime(time)
                .build();

        var actual = AuxiHeader.create(input.toByteArray());
        Assertions.assertThat(actual.startTime().getYear()).isEqualTo(2022);
        Assertions.assertThat(actual.startTime().getMonthValue()).isEqualTo(11);
        Assertions.assertThat(actual.startTime().getDayOfMonth()).isEqualTo(23);
        Assertions.assertThat(actual.startTime().getNano()).isEqualTo(0);
    }

}