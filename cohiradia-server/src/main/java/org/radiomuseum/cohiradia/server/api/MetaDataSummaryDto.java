package org.radiomuseum.cohiradia.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
@XmlRootElement
public class MetaDataSummaryDto {

    @JsonProperty(value = "startTimestamp")
    String startTimestamp;

    @JsonProperty(value = "content")
    String content;

    @JsonProperty(value = "band")
    String band;

    @JsonProperty(value = "city")
    String city;

    @JsonProperty(value = "country")
    String country;

    @JsonProperty(value = "id")
    int id;

}
