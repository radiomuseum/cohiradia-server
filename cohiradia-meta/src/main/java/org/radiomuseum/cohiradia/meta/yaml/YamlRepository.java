package org.radiomuseum.cohiradia.meta.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;

import static org.radiomuseum.cohiradia.meta.utils.JacksonFactory.createJsonMapper;
import static org.radiomuseum.cohiradia.meta.utils.JacksonFactory.createYamlMapper;

public class YamlRepository {

    @SneakyThrows
    public MetaData read(File file) {
        var mapper = createYamlMapper();
        return mapper.reader().readValue(file, MetaData.class);
    }

    public MetaData read2(File file) throws Exception {
        var mapper = createYamlMapper();
        return mapper.reader().readValue(file, MetaData.class);
    }

    public void persistYaml(File file, MetaData metaData) throws IOException {
        ObjectMapper mapper = createYamlMapper();
        mapper.writeValue(file, metaData);
    }

    public void persistJson(File file, MetaData metaData) throws IOException {
        ObjectMapper mapper = createJsonMapper();
        mapper.writeValue(file, metaData);
    }

    public static boolean isYaml(File file) {
        return file.getName().endsWith(".yaml");
    }
}
