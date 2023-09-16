package org.radiomuseum.cohiradia.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

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
