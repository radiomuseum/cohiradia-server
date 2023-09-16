package org.radiomuseum.cohiradia.meta.sdruno;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.radiomuseum.cohiradia.meta.utils.ByteArrayBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Arrays;

@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@ToString
public class SdrUnoHeaders {

    public final static int HEADER_SIZE = 216;

    private RiffHeader riff;
    private FmtHeader fmt;
    private AuxiHeader auxi;
    private DataHeader data;

    static byte[] readHeaders(File file) {
        try (InputStream is = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            byte[] header = new byte[HEADER_SIZE];
            if (is.read(header, 0, HEADER_SIZE) != HEADER_SIZE) {
                throw new RuntimeException("Could not extract full header.");
            }
            return header;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("Could not read headers.");
        }
    }

    public static SdrUnoHeaders create(File file) {
        byte[] header = readHeaders(file);

        return SdrUnoHeaders.builder()
                .riff(RiffHeader.create(Arrays.copyOfRange(header, 0, 12)))
                .fmt(FmtHeader.create(Arrays.copyOfRange(header, 12, 36)))
                .auxi(AuxiHeader.create(Arrays.copyOfRange(header, 36, 36 + 172)))
                .data(DataHeader.create(Arrays.copyOfRange(header, 36 + 172, 36 + 172 + 8)))
                .build();
    }

    public static byte[] createHeader(long payloadSize, int centerFrequency, int samplesPerSecond, String filename, ZonedDateTime start, ZonedDateTime end) {

        var riff = RiffHeader.builder()
                .filesize((int) (4 + FmtHeader.length() + AuxiHeader.length() + payloadSize))
                .build();
        var fmt = FmtHeader.builder()
                .nSamplesPerSec(samplesPerSecond)
                .nAvgBytesPerSec(samplesPerSecond * 2 * 2) //1. 2: Channels, 1. 2: Bytes pro Sample pro Channel
                .build();
        var auxi = AuxiHeader.builder()
                .startTime(start)
                .stopTime(end)
                .centerFreq(centerFrequency)
                .nextfilename(filename).build();
        var data = DataHeader.builder().fileSize(payloadSize).build();

        ByteArrayBuilder builder = ByteArrayBuilder.create(216);
        builder.append(riff.toByteArray());
        builder.append(fmt.toByteArray());
        builder.append(auxi.toByteArray());
        builder.append(data.toByteArray());
        return builder.toByteArray();
    }

    public byte[] toByteArray() {
        ByteArrayBuilder builder = ByteArrayBuilder.create(216);
        builder.append(this.riff().toByteArray());
        builder.append(this.fmt().toByteArray());
        builder.append(this.auxi().toByteArray());
        builder.append(this.data().toByteArray());
        return builder.toByteArray();
    }
}
