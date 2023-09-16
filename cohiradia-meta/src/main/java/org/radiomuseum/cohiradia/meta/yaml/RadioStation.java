package org.radiomuseum.cohiradia.meta.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RadioStation {

    @JsonProperty(value = "frequency", index = 1)
    private String frequenz;

    @JsonProperty(value = "snr", index = 2)
    private String snrDb;

    @JsonProperty(value = "s-level", index = 3)
    private String sLevel;

    @JsonProperty(value = "country", index = 4)
    private String country;

    @JsonProperty(value = "programme", index = 5, required = true)
    private String programme;

    @JsonProperty(value = "tx-site", index = 6)
    private String txLocation;

    @JsonProperty(value = "tx-power", index = 7)
    private String txPower;

    @JsonProperty(value = "remarks", index = 8)
    private String remarks;

}
