package org.radiomuseum.cohiradia.meta.dat;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;
import org.radiomuseum.cohiradia.meta.utils.Filename;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@Accessors(fluent = true)
public class DatFilename implements Filename {

    private int centerFrequency;
    private int samplesPerSec;
    String prefix;


    public static DatFilename parse(String name) {
        Matcher m = Pattern.compile("(.*)_lo(\\d+)+_r(\\d+)_.*").matcher(name);
        if (m.find()) {
            return DatFilename.builder()
                    .prefix(m.group(1))
                    .centerFrequency(Integer.parseInt(m.group(2)) * 1000)
                    .samplesPerSec(Integer.parseInt(m.group(3)) * 1000)
                    .build();
        }
        throw new IllegalStateException("Pattern not recognized.");
    }

    public static String generate(RecordingDescriptor descriptor) {
        return String.format("%s_lo%s_r%s_c0.dat", descriptor.name(), descriptor.centerFrequency() / 1000, descriptor.sampleRate() / 1000);
    }
}
