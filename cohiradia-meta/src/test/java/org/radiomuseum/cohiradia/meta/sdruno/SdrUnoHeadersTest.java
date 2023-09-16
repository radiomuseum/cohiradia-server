package org.radiomuseum.cohiradia.meta.sdruno;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;

class SdrUnoHeadersTest {

    @Test
    public void riffTest() {
        var headers = SdrUnoHeaders.create(new File("src/test/data/sdruno-1.header"));
        Assertions.assertThat(headers.riff().filesize()).isEqualTo(53029072L);
        Assertions.assertThat(headers.riff().chunkId()).isEqualTo("RIFF");
        Assertions.assertThat(headers.riff().wave()).isEqualTo("WAVE");
    }

    @Test
    public void fmtTest() {
        var headers = SdrUnoHeaders.create(new File("src/test/data/sdruno-1.header"));
        Assertions.assertThat(headers.fmt().nSamplesPerSec()).isEqualTo(150000);
        Assertions.assertThat(headers.fmt().chunkId()).isEqualTo("fmt ");
        Assertions.assertThat(headers.fmt().chunkSize()).isEqualTo(16);
        Assertions.assertThat(headers.fmt().formatTag()).isEqualTo((short) 1);
        Assertions.assertThat(headers.fmt().nAvgBytesPerSec()).isEqualTo(600000);
        Assertions.assertThat(headers.fmt().nChannels()).isEqualTo((short) 2);
        Assertions.assertThat(headers.fmt().nBitsPerSample()).isEqualTo((short) 16);
        Assertions.assertThat(headers.fmt().nBlockAlign()).isEqualTo((short) 4);
    }

    @Test
    public void auxiTest() {
        var headers = SdrUnoHeaders.create(new File("src/test/data/sdruno-1.header"));
        Assertions.assertThat(headers.auxi().centerFreq()).isEqualTo(220000);
        Assertions.assertThat(headers.auxi().startTime()).isEqualTo(ZonedDateTime.of(2022, 6, 11, 21, 9, 40, 137 * 1_000_000, ZoneId.of("UTC")));
        Assertions.assertThat(headers.auxi().stopTime()).isEqualTo(ZonedDateTime.of(2022, 6, 11, 21, 11, 9, 139 * 1_000_000, ZoneId.of("UTC")));
        Assertions.assertThat(headers.auxi().bandwidth()).isEqualTo(0);
        Assertions.assertThat(headers.auxi().adFrequency()).isEqualTo(0);
        Assertions.assertThat(headers.auxi().ifFrequency()).isEqualTo(0);
        Assertions.assertThat(headers.auxi().iqOffset()).isEqualTo(0);
        Assertions.assertThat(headers.auxi().nextfilename()).isEqualTo("");
    }

    @Test
    public void dataTest() {
        var headers = SdrUnoHeaders.create(new File("src/test/data/sdruno-1.header"));
        Assertions.assertThat(headers.data().fileSize()).isEqualTo(53028864L);
    }

    @Test
    void roundtrip() {
        var headers = SdrUnoHeaders.create(new File("src/test/data/sdruno-1.header"));
        System.out.println(headers);
        var serialized = headers.toByteArray();
        var original = SdrUnoHeaders.readHeaders(new File("src/test/data/sdruno-1.header"));
        Assertions.assertThat(original).isEqualTo(serialized);
    }
}