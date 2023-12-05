package org.radiomuseum.cohiradia.meta.descriptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.Dependent;
import lombok.SneakyThrows;
import org.radiomuseum.cohiradia.meta.utils.JacksonFactory;

import java.io.File;
import java.io.IOException;

@Dependent
public class DescriptorRepositoryImpl implements DescriptorRepository {

    @Override
    @SneakyThrows
    public RecordingDescriptor read(File source) {
        var mapper = JacksonFactory.createYamlMapper();
        return mapper.reader().readValue(source, RecordingDescriptor.class);
    }

    @Override
    public RecordingDescriptor readDescriptorInPath(File path) throws IOException {
        File file = new File(path, "descriptor.yaml");
        var mapper = JacksonFactory.createYamlMapper();
        return mapper.reader().readValue(file, RecordingDescriptor.class);
    }

    @Override
    @SneakyThrows
    public void write(RecordingDescriptor descriptor, File destination) {
        ObjectMapper mapper = JacksonFactory.createYamlMapper();
        mapper.writeValue(destination, descriptor);
    }
}
