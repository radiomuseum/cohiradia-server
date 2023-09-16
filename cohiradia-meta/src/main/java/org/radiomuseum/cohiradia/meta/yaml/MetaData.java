package org.radiomuseum.cohiradia.meta.yaml;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MetaData {

    @JsonProperty(value = "id", defaultValue = "0")
    private int id;
    @JsonProperty(value = "uri")
    private String uri;
    @JsonProperty(value = "recording-date")
    private ZonedDateTime recordingDate;
    @JsonProperty(value = "duration")
    private int duration;
    @JsonProperty(value = "band")
    private String band;
    @JsonProperty(value = "frequency-unit")
    private String frequencyUnit;
    @JsonProperty(value = "frequency-low")
    private double frequencyLow;
    @JsonProperty(value = "frequency-high")
    private double frequencyHigh;
    @JsonProperty(value = "frequency-correction")
    private double frequencyCorrection;
    @JsonProperty(value = "encoding")
    private String encoding;
    @JsonProperty(value = "center-frequency")
    private double centerFrequency;
    @JsonProperty(value = "bandwidth")
    private double bandwidth;
    @JsonProperty(value = "antenna")
    private String antenna;
    @JsonProperty(value = "recording-type")
    private String recordingType;
    @JsonProperty(value = "remark")
    private String note;
    @JsonProperty(value = "content")
    private String content;
    @JsonIgnore
    private transient String radioStation;
    @JsonProperty(value = "radio-stations")
    private List<RadioStation> radioStations;
    @JsonProperty(value = "radio-station-announcement")
    private String radioStationAnnouncement;
    @JsonProperty(value = "location-longitude")
    private String locationLongitude;
    @JsonProperty(value = "location-latitude")
    private String locationLatitude;
    @JsonProperty(value = "location-country")
    private String locationCountry;
    @JsonProperty(value = "location-city")
    private String locationCity;
    @JsonProperty(value = "location-qth")
    private String locationQTH;
    @JsonProperty(value = "upload-user-fk")
    private int uploadUserFk;
    @JsonProperty(value = "filters")
    private String filters;
    @JsonProperty(value = "preamp-settings")
    private String preampSettings;

    @JsonIgnore
    public String getNotesHtml() {
        if (getNote() == null) {
            return "";
        }
        return getNote().replace("\n", "<br><br>");
    }

    @JsonIgnore
    public String getRadioStationsHtml() {
        if (this.radioStations != null && this.radioStations.size() > 0) {
            return TableRenderer.render(this.radioStations);
        }
        return "";
    }

    @JsonIgnore
    public int getLocationCountryFk() {
        return switch (getLocationCountry().toUpperCase()) {
            case "AT" -> 2;
            case "US" -> 3;
            case "DE" -> 1;
            case "CH" -> 8;
            case "ZZ" -> 1002;
            default -> 0;
        };
    }

    public long frequencyFactor() {
        return switch (getFrequencyUnit()) {
            case "kHz" -> 1000;
            case "Hz" -> 1;
            case "MHz" -> 1000 * 1000;
            default -> throw new IllegalStateException("Unknown unit.");
        };
    }

    @JsonIgnore
    public int centerFrequencyAsHz() {
        return (int) (frequencyFactor() * getCenterFrequency());
    }

    @JsonIgnore
    public int frequencyLowAsHz() {
        return (int) (frequencyFactor() * getFrequencyLow());
    }

    @JsonIgnore
    public int frequencyHighAsHz() {
        return (int) (frequencyFactor() * getFrequencyHigh());
    }

    @JsonIgnore
    public String contentLogic() {
        return content != null ? getContent() : getNotesHtml().substring(40);
    }

}
