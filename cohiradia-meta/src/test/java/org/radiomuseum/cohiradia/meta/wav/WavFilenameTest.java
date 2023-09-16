package org.radiomuseum.cohiradia.meta.wav;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class WavFilenameTest {

    @Test
    void t1() {
        var actual = WavFilename.parse("i16RAIMWtheENDlasthour_SDRuno_20220910_192612Z_1125kHz_part3.wav");
        Assertions.assertThat(actual.prefix()).isEqualTo("i16RAIMWtheENDlasthour_SDRuno");
    }

    @Test
    void t2() {
        var actual = WavFilename.parse("Grimeton_WBcut_20221116_154501Z_135kHz.wav");
        Assertions.assertThat(actual.prefix()).isEqualTo("Grimeton_WBcut");
    }
}