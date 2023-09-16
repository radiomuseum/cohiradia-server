package org.radiomuseum.cohiradia.meta.sdruno;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.radiomuseum.cohiradia.meta.utils.ByteArrayBuilder;
import org.radiomuseum.cohiradia.meta.utils.ByteUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

@Data
@Builder
@ToString
@Accessors(fluent = true)
public class AuxiHeader {

    @Builder.Default
    private String chunkId = "auxi"; // ="auxi" (chunk rfspace)
    @Builder.Default
    private int chunkSize = 164; // lunghezza del chunk
    private ZonedDateTime startTime; // ANMERKUNG: das sind einfach 8 short integers intereinander
    private ZonedDateTime stopTime; // ANMERKUNG: das sind einfach 8 short integers intereinander
    private int centerFreq; //receiver center frequency, DWORD = long !
    @Builder.Default
    private int adFrequency = 0; //A/D sample frequency before downsampling
    @Builder.Default
    private int ifFrequency = 0; //IF freq if an external down converter is used
    @Builder.Default
    private int bandwidth = 0; //displayable BW if you want to limit the display to less than Nyquist band
    @Builder.Default
    private int iqOffset = 0; //DC offset of the I and Q channels in 1/1000's of a count
    @Builder.Default
    private int unused2 = 0; // used to mark demo files & RSP special files, otherwise zero
    @Builder.Default
    private int unused3 = 0;
    @Builder.Default
    private int unused4 = 0; // used by HDSDR for 64 bit tuning "CenterFrqLo"
    @Builder.Default
    private int unused5 = 0; // used by HDSDR for 64 bit tuning "CenterFrqHi"
    @Builder.Default
    private String nextfilename = "";

    public static AuxiHeader create(byte[] auxiHeader) {
        Objects.requireNonNull(auxiHeader);
        if (auxiHeader.length != length()) {
            throw new IllegalArgumentException(String.format("ByteArray must be %s bytes long.", length()));
        }

        return AuxiHeader.builder()
                .chunkId(new String(Arrays.copyOfRange(auxiHeader, 0, 4)))
                .chunkSize(ByteUtils.bytesToUint(auxiHeader, 4))
                .startTime(ZonedDateTime.of(ByteUtils.bytesToShort(auxiHeader, 8),
                        ByteUtils.bytesToShort(auxiHeader, 10),
                        ByteUtils.bytesToShort(auxiHeader, 14),
                        ByteUtils.bytesToShort(auxiHeader, 16),
                        ByteUtils.bytesToShort(auxiHeader, 18),
                        ByteUtils.bytesToShort(auxiHeader, 20),
                        (int) ByteUtils.bytesToShort(auxiHeader, 22) * 1_000_000, ZoneId.of("UTC")))
                .stopTime(ZonedDateTime.of(ByteUtils.bytesToShort(auxiHeader, 24),
                        ByteUtils.bytesToShort(auxiHeader, 26),
                        ByteUtils.bytesToShort(auxiHeader, 30),
                        ByteUtils.bytesToShort(auxiHeader, 32),
                        ByteUtils.bytesToShort(auxiHeader, 34),
                        ByteUtils.bytesToShort(auxiHeader, 36),
                        (int) ByteUtils.bytesToShort(auxiHeader, 38) * 1_000_000, ZoneId.of("UTC")))
                .centerFreq(ByteUtils.bytesToUint(auxiHeader, 40))
                .adFrequency(ByteUtils.bytesToUint(auxiHeader, 44))
                .ifFrequency(ByteUtils.bytesToUint(auxiHeader, 48))
                .bandwidth(ByteUtils.bytesToUint(auxiHeader, 52))
                .iqOffset(ByteUtils.bytesToUint(auxiHeader, 56))
                .unused2(ByteUtils.bytesToUint(auxiHeader, 60))
                .unused3(ByteUtils.bytesToUint(auxiHeader, 64))
                .unused4(ByteUtils.bytesToUint(auxiHeader, 68))
                .unused5(ByteUtils.bytesToUint(auxiHeader, 72))
                .nextfilename(new String(Arrays.copyOfRange(auxiHeader, 76, 76 + 96)).trim())
                .build();
    }

    public byte[] toByteArray() {
        return ByteArrayBuilder.create(length())
                .append(chunkId())
                .append(chunkSize())
                .append(startTime())
                .append(stopTime())
                .append(centerFreq())
                .append(adFrequency())
                .append(ifFrequency())
                .append(bandwidth())
                .append(iqOffset())
                .append(unused2())
                .append(unused3())
                .append(unused4())
                .append(unused5())
                .append(nextfilename(), 96)
                .toByteArray();
    }

    public static int length() {
        return 172;
    }
}
