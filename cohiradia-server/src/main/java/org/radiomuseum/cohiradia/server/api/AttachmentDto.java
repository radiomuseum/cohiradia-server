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
public class AttachmentDto {

    @JsonProperty(value = "name")
    String name;

    @JsonProperty(value = "url")
    String url;

}
