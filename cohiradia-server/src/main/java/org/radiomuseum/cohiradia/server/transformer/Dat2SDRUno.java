package org.radiomuseum.cohiradia.server.transformer;

import io.quarkus.logging.Log;
import org.radiomuseum.cohiradia.meta.dat.DatFilename;
import org.radiomuseum.cohiradia.meta.descriptor.DescriptorRepository;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingPartDescriptor;
import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import org.radiomuseum.cohiradia.server.ApplicationConfiguration;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

import static io.quarkus.arc.ComponentsProvider.LOG;
import static java.nio.file.StandardOpenOption.WRITE;

@Dependent
public class Dat2SDRUno {

    /**
     * 4GB - SDRUno Header
     */
    private final static long MAX_WAV_SIZE = 4 * 1024L * 1024L * 1024L - 216;

    @Inject
    ApplicationConfiguration config;

    @Inject
    DescriptorRepository repository;

    public void transformChecked(MetaData metaData) {
        try {
            transform(metaData);
        } catch (RuntimeException | IOException e) {
            Log.errorf("Could not execute transformation.", e);
        }
    }


    public void transform(MetaData metaData) throws IOException {
        if (!metaData.getUri().endsWith(".dat")) {
            LOG.infof("Skip File with URI [%s] and ID [%s].", metaData.getUri(), metaData.getId());
            return;
        } else {
            LOG.infof("Process File with URI [%s] ID [%s].", metaData.getUri(), metaData.getId());
        }

        var datFile = new File(config.basePathStorage(), metaData.getUri());

        var filename = DatFilename.parse(datFile.getName());
        InputStream is = new BufferedInputStream(Files.newInputStream(datFile.toPath(), StandardOpenOption.READ), 8192 * 1000);
        long fileLength = datFile.length();
        long parts = (long) Math.ceil((double) fileLength / (double) MAX_WAV_SIZE);

        ZonedDateTime startTime = metaData.getRecordingDate();

        var targetPath = datFile.getParentFile();

        File descriptorPath = new File(config.basePathDescriptor(), Integer.toString(metaData.getId()));
        Log.info(descriptorPath.mkdirs());
        File descriptorFile = new File(descriptorPath, "descriptor.yaml");
        if (descriptorFile.exists()) { // abort, if descriptor file exists.
            Log.infof("Skip file [%s]. Target exists already.", targetPath.getAbsolutePath());
            return;
        }

        var descriptor = RecordingDescriptor.builder()
                .id(metaData.getId())
                .basePath(targetPath.getAbsolutePath())
                .name(filename.prefix())
                .centerFrequency(filename.centerFrequency())
                .sampleRate(filename.samplesPerSec())
                .startDate(startTime).build();

        for (var part = 1; part <= parts; part++) {
            long partSize = Math.min(fileLength - (part - 1) * MAX_WAV_SIZE, MAX_WAV_SIZE);
            long timestampStart = ((part - 1) * MAX_WAV_SIZE) / 4 / (filename.samplesPerSec() / 1000);
            long durationPart = partSize / 4 / (filename.samplesPerSec() / 1000);
            ZonedDateTime startOfPart = startTime.plus(timestampStart, ChronoUnit.MILLIS);
            ZonedDateTime endOfPart = startOfPart.plus(durationPart, ChronoUnit.MILLIS);
            descriptor.parts()
                    .add(RecordingPartDescriptor.builder()
                            .size(partSize)
                            .name(getFilenameForPart(parts, part, filename, startOfPart))
                            .duration(durationPart)
                            .startDate(startOfPart).build());
            File partFile = new File(targetPath, Objects.requireNonNull(getFilenameForPart(parts, part, filename, startOfPart)));
            byte[] header = SdrUnoHeaders.createHeader(partSize, filename.centerFrequency(), filename.samplesPerSec(), getFilenameForPart(parts, part + 1, filename, endOfPart), startOfPart, endOfPart);
            try (var os = new BufferedOutputStream(Files.newOutputStream(partFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, WRITE), 8192 * 1000)) {
                os.write(header);
                for (long i = 0; i < partSize; i++) {
                    os.write(is.read());
                }
                os.flush();
            }

            repository.write(descriptor, descriptorFile);
        }
    }

    private String getFilenameForPart(long parts, int part, DatFilename filename, ZonedDateTime startOfPart) {
        if (part > parts) {
            return null;
        }

        String date = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(startOfPart) + "Z";

        if (parts == 1L && part == parts) {
            return String.format("%s_SDRuno_%s_%skHz.wav", filename.prefix(), date, (filename.centerFrequency() / 1000));
        }
        return String.format("%s_SDRuno_%s_%skHz_part%s.wav", filename.prefix(), date, (filename.centerFrequency() / 1000), part);
    }

    public void importAll() {
        YamlRepository yamlRepository = new YamlRepository();
        for (File yaml : Arrays.stream(Objects.requireNonNull(config.basePathMetadata().listFiles())).filter(f -> f.getName().endsWith(".yaml")).toList()) {
            transformChecked(yamlRepository.read(yaml));
        }
    }
}
