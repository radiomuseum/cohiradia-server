package org.radiomuseum.cohiradia.server.api;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.radiomuseum.cohiradia.meta.descriptor.DescriptorRepository;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import org.radiomuseum.cohiradia.server.ApplicationConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

@ApplicationScoped
public class MetadataCache {

    @Inject
    ApplicationConfiguration config;

    @Inject
    DescriptorRepository descriptorRepository;

    private final ConcurrentMap<Integer, MetaData> cache = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, RecordingDescriptor> dataDirs = new ConcurrentHashMap<>();
    private final ConcurrentMap<File, RecordingDescriptor> descriptors = new ConcurrentHashMap<>();
    private final YamlRepository repository = new YamlRepository();

    @Startup
    public void init() {
        cache.clear();
        Arrays.stream(Objects.requireNonNull(config.basePathMetadata().listFiles()))
                .map(repository::read)
                .forEach(m -> cache.put(m.getId(), m));

        dataDirs.clear();
        descriptors.clear();
        for (File path : Objects.requireNonNull(config.basePathDescriptor().listFiles())) {
            if (path.isDirectory()) {
                try {
                    var descriptor = descriptorRepository.readDescriptorInPath(path);
                    dataDirs.put(descriptor.id(), descriptor);
                    descriptors.put(path, descriptor);
                } catch (IOException e) {
                    Log.info("Descriptor not available.");
                }
            }
        }
    }

    public RecordingDescriptor getDescriptor(int id) {
        return dataDirs.get(id);
    }

    public Stream<MetaData> getAll() {
        return cache.values().stream();
    }

    public Optional<MetaData> get(int key) {
        return Optional.ofNullable(cache.get(key));
    }

    public Stream<RecordingDescriptor> getAllDescriptors() {
        return descriptors.values().stream();
    }
}
