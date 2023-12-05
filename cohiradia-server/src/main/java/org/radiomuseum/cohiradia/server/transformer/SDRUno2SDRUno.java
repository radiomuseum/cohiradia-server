package org.radiomuseum.cohiradia.server.transformer;

import io.quarkus.logging.Log;
import org.radiomuseum.cohiradia.meta.descriptor.DescriptorRepository;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingPartDescriptor;
import org.radiomuseum.cohiradia.meta.sdruno.AuxiHeader;
import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import org.radiomuseum.cohiradia.meta.wav.HeaderUtils;
import org.radiomuseum.cohiradia.meta.wav.WavFilename;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.server.ApplicationConfiguration;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.quarkus.arc.ComponentsProvider.LOG;

@Dependent
public class SDRUno2SDRUno {

    @Inject
    ApplicationConfiguration config;

    @Inject
    DescriptorRepository repository;

    public void transformChecked(MetaData metaData) {
        try {
            if (metaData.getUri().endsWith(".wav")) {
                LOG.infof("Process [%s] with URI [%s].", metaData.getId(), metaData.getUri());
                transform(metaData);
            } else {
                LOG.infof("Skip [%s] with URI [%s].", metaData.getId(), metaData.getUri());
            }
        } catch (RuntimeException | IOException e) {
            Log.error("Could not execute transformation.", e);
        }
    }

    public void transform(MetaData metadata) throws IOException {
        String firstWavName = metadata.getUri();
        var wavSrcFile = checkWaveFile(config.basePathStorage(), firstWavName);
        var wavSrcDir = wavSrcFile.getParentFile();

        var wavFilename = WavFilename.parse(wavSrcFile.getName());
        var header = SdrUnoHeaders.create(wavSrcFile);
        RecordingDescriptor descriptor = createDescriptor(wavSrcDir, header, wavFilename, metadata.getId());

        List<File> filesToProcess = new ArrayList<>();
        filesToProcess.add(wavSrcFile);
        descriptor.parts().add(createPartDescriptor(wavSrcFile, header.auxi()));

        String nextFilename;
        while (!(nextFilename = removePath(header.auxi().nextfilename().trim())).isBlank()) {
            Log.infof("Next filename: %s", nextFilename);
            wavSrcFile = checkWaveFile(wavSrcDir, nextFilename);
            header = SdrUnoHeaders.create(wavSrcFile);
            descriptor.parts().add(createPartDescriptor(wavSrcFile, header.auxi()));
            filesToProcess.add(wavSrcFile);
        }
        writeFilesToTarget(descriptor, filesToProcess);
    }

    private RecordingDescriptor createDescriptor(File wavSrcDir, SdrUnoHeaders header, WavFilename wavFilename, int id) {
        return RecordingDescriptor.builder()
                .id(id)
                .basePath(wavSrcDir.getAbsolutePath())
                .name(wavFilename.prefix())
                .centerFrequency(header.auxi().centerFreq())
                .sampleRate(header.fmt().nSamplesPerSec())
                .startDate(header.auxi().startTime()).build();
    }

    private File checkWaveFile(File basePath, String name) {
        var wavFile = new File(basePath, name);
        if (!wavFile.exists()) {
            Log.errorf("Referenced WAVE file [%s] does not exist. Abort.", wavFile.getAbsolutePath());
            throw new IllegalArgumentException("WAVE file does not exist.");
        }
        return wavFile;
    }

    private void writeFilesToTarget(RecordingDescriptor descriptor, List<File> filesToCopy) throws IOException {

        var descriptorPath = new File(config.basePathDescriptor(), Integer.toString(descriptor.id()));
        Log.info(descriptorPath.mkdirs());
        var descriptorFile = new File(descriptorPath, "descriptor.yaml");

        if (!descriptorFile.exists()) {
            for (File src : filesToCopy) {
                Log.infof("Copy file: %s", src.getAbsolutePath());
                fixNextFilenameIfNecessary(src);
            }
            repository.write(descriptor, descriptorFile);
        } else {
            Log.infof("Skip file [%s]. Target exists already.", descriptorFile.getAbsolutePath());
        }
    }

    private void fixNextFilenameIfNecessary(File target) throws IOException {
        var header = SdrUnoHeaders.create(target);
        var auxi = header.auxi();
        var shorten = removePath(auxi.nextfilename());
        if (!shorten.equals(auxi.nextfilename())) {
            header.auxi().nextfilename(shorten);
            HeaderUtils.replaceHeader(header, target);
        }
    }

    private String removePath(String s) {
        if (s == null) {
            return "";
        }
        if (s.contains("/")) {
            return s.substring(s.lastIndexOf("/") + 1);
        }
        if (s.contains("\\")) {
            return s.substring(s.lastIndexOf("\\") + 1);
        }
        return s;
    }

    private RecordingPartDescriptor createPartDescriptor(File wavFile, AuxiHeader auxiHeader) {
        var duration = Duration.between(auxiHeader.startTime(), auxiHeader.stopTime());
        return RecordingPartDescriptor.builder()
                .name(wavFile.getName())
                .duration(duration.toMillis())
                .startDate(auxiHeader.startTime())
                .size(wavFile.length())
                .build();
    }

}
