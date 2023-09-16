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
public class RiffHeader {

    @Builder.Default
    private String chunkId = "RIFF";
    long filesize;
    @Builder.Default
    private String wave = "WAVE";

    public static RiffHeader create(byte[] riffHeader) {
        Objects.requireNonNull(riffHeader);
        if (riffHeader.length != length()) {
            throw new IllegalArgumentException(String.format("ByteArray must be %s bytes long.", length()));
        }

        return RiffHeader.builder()
                .chunkId(new String(Arrays.copyOfRange(riffHeader, 0, 4)))
                .filesize(ByteUtils.bytesToLong(Arrays.copyOfRange(riffHeader, 4, 8)))
                .wave(new String(Arrays.copyOfRange(riffHeader, 8, 12)))
                .build();
    }

    public byte[] toByteArray() {
        return ByteArrayBuilder.create(length())
                .append(chunkId())
                .append(filesize())
                .append(wave())
                .toByteArray();
    }

    public static int length() {
        return 12;
    }


}
