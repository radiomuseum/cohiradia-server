package org.radiomuseum.cohiradia.meta.dat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;


class DatFilenameTest {

    @Test
    @DisplayName("Extract center frequency.")
    public void t1() {
        String name = "i16RAIMWtheENDinclanthem_lo1125_r1250_c0.dat";
        var actual = DatFilename.parse(name);
        Assertions.assertThat(actual.centerFrequency()).isEqualTo(1125000);
    }

    @Test
    @DisplayName(("Extract samples per sec."))
    public void t2() {
        String name = "i16RAIMWtheENDinclanthem_lo1125_r1250_c0.dat";
        var actual = DatFilename.parse(name);
        Assertions.assertThat(actual.samplesPerSec()).isEqualTo(1250000);
    }

    @Test
    @DisplayName(("Extract prefix."))
    public void t3() {
        String name = "i16RAIMWtheENDinclanthem_lo1125_r1250_c0.dat";
        var actual = DatFilename.parse(name);
        Assertions.assertThat(actual.prefix()).isEqualTo("i16RAIMWtheENDinclanthem");
    }

    @Test
    @DisplayName("Generate dat Filename")
    public void t4() {
        var descriptor = RecordingDescriptor.builder().centerFrequency(100000).sampleRate(20000).name("the-name").build();
        var actual = DatFilename.generate(descriptor);
        Assertions.assertThat(actual).isEqualTo("the-name_lo100_r20_c0.dat");
    }
}