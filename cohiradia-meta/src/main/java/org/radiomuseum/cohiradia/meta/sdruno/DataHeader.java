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
public class DataHeader {

    @Builder.Default
    private String chunkId = "data";
    private long fileSize;

    public static DataHeader create(byte[] riffHeader) {
        Objects.requireNonNull(riffHeader);
        if (riffHeader.length != length()) {
            throw new IllegalArgumentException(String.format("ByteArray must be %s bytes long.", length()));
        }

        return DataHeader.builder()
                .chunkId(new String(Arrays.copyOfRange(riffHeader, 0, 4)))
                .fileSize(ByteUtils.bytesToLong(Arrays.copyOfRange(riffHeader, 4, 8)))
                .build();
    }

    public byte[] toByteArray() {
        return ByteArrayBuilder.create(length())
                .append(chunkId())
                .append(fileSize())
                .toByteArray();
    }

    public static int length() {
        return 8;
    }


}
