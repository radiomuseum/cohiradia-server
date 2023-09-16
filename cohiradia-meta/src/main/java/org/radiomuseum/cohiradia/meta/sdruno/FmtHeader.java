package org.radiomuseum.cohiradia.meta.sdruno;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.radiomuseum.cohiradia.meta.utils.ByteArrayBuilder;
import org.radiomuseum.cohiradia.meta.utils.ByteUtils;

import java.util.Arrays;
import java.util.Objects;

@Data
@Builder
@ToString
@Accessors(fluent = true)
public class FmtHeader {

    @Builder.Default()
    private String chunkId = "fmt ";
    @Builder.Default
    private int chunkSize = 16;
    @Builder.Default
    private short formatTag = 1;
    @Builder.Default
    private short nChannels = 2;
    private int nSamplesPerSec;
    private int nAvgBytesPerSec;
    @Builder.Default
    private short nBlockAlign = 4;
    @Builder.Default
    private short nBitsPerSample = 16;

    public static FmtHeader create(byte[] fmtHeader) {
        Objects.requireNonNull(fmtHeader);
        if (fmtHeader.length != length()) {
            throw new IllegalArgumentException(String.format("ByteArray must be %s bytes long.", length()));
        }

        return FmtHeader.builder()
                .chunkId(new String(Arrays.copyOfRange(fmtHeader, 0, 4)))
                .chunkSize(ByteUtils.bytesToUint(fmtHeader, 4))
                .formatTag(ByteUtils.bytesToShort(fmtHeader, 8))
                .nChannels(ByteUtils.bytesToShort(fmtHeader, 10))
                .nSamplesPerSec(ByteUtils.bytesToUint(fmtHeader, 12))
                .nAvgBytesPerSec(ByteUtils.bytesToUint(fmtHeader, 16))
                .nBlockAlign(ByteUtils.bytesToShort(fmtHeader, 20))
                .nBitsPerSample(ByteUtils.bytesToShort(fmtHeader, 22))
                .build();
    }

    public byte[] toByteArray() {
        return ByteArrayBuilder.create(length())
                .append(chunkId())
                .append(chunkSize())
                .append(formatTag())
                .append(nChannels())
                .append(nSamplesPerSec())
                .append(nAvgBytesPerSec())
                .append(nBlockAlign())
                .append(nBitsPerSample())
                .toByteArray();
    }

    public static int length() {
        return 24;
    }


}
