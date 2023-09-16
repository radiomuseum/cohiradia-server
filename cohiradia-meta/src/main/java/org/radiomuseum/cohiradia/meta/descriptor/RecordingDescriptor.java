package org.radiomuseum.cohiradia.meta.descriptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Accessors(fluent = true)
public class RecordingDescriptor {

    @JsonProperty(value = "id")
    private int id;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "center-frequency")
    private int centerFrequency;
    @JsonProperty(value = "sample-rate")
    private int sampleRate;
    @JsonProperty(value = "start-date")
    private ZonedDateTime startDate;
    @JsonProperty(value = "base-path")
    private String basePath;
    @JsonProperty(value = "parts")
    @Builder.Default
    private List<RecordingPartDescriptor> parts = new ArrayList<>();

    public String sizeAsGB() {
        var format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        return format.format(((double) parts().stream().map(RecordingPartDescriptor::size).reduce(Long::sum).orElse(0L) / 1024 / 1024 / 1024));
    }

}
