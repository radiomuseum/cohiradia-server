package org.radiomuseum.cohiradia.meta.descriptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.NumberFormat;
import java.time.ZonedDateTime;

@Builder
@Data
@Accessors(fluent = true)
public class RecordingPartDescriptor {

    @JsonProperty(value = "start-date")
    private ZonedDateTime startDate;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "size")
    private long size;
    /**
     * Duration in {@link java.time.temporal.ChronoUnit#MILLIS}.
     */
    @JsonProperty(value = "duration")
    private long duration;

    public String sizeAsGB() {
        var format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        return format.format(((double) size / 1024 / 1024 / 1024));
    }
}
