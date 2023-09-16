package org.radiomuseum.cohiradia.meta.wav;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;
import org.radiomuseum.cohiradia.meta.utils.Filename;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Builder
@Data
@Accessors(fluent = true)
public class WavFilename implements Filename {

    String prefix;


    public static WavFilename parse(String name) {
        // i16RAIMWtheENDlasthour_SDRuno_20220910_192612Z_1125kHz_part3
        Matcher m = Pattern.compile("(.*)_(\\d{8}+)_(\\d{6}+Z)_.*\\.wav").matcher(name);
        if (m.find()) {
            return WavFilename.builder()
                    .prefix(m.group(1))
                    .build();
        } else {
            return WavFilename.builder()
                    .prefix(name.replace(".wav", ""))
                    .build();
        }
    }

    public static String generate(RecordingDescriptor descriptor) {
        return String.format("%s_lo%s_r%s_c0.dat", descriptor.name(), descriptor.centerFrequency() / 1000, descriptor.sampleRate() / 1000);
    }
}
