package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.meta.dat.DatFilename;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Command(name = "yaml-create", mixinStandardHelpOptions = true, version = "1.0",
        description = "Creates a new YAML metadata file.")
class YAMLCreateCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The recording file (dat/wav)")
    private File recordingFile;
    @Parameters(index = "1", description = "The YAML file to create.")
    private File yaml;

    private enum FileType {
        dat, wav;

        static FileType create(File file) {
            for (var type : FileType.values()) {
                if (file.getName().endsWith(String.format(".%s", type.name()))) {
                    return type;
                }
            }
            throw new IllegalStateException("Type not found.");
        }
    }

    @Override
    public Integer call() {
        System.out.println();
        System.out.println(Chalk.on("Create YAML Metadata File").blue().bold().toString());

        var type = FileType.create(recordingFile);

        MetaData metaData = createMetadata(recordingFile);
        try {
            new YamlRepository().persistYaml(yaml, metaData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return 1;
    }

    private MetaData createMetadata(File datFile) {
        var name = DatFilename.parse(datFile.getName());

        var metaData = new MetaData();
        metaData.setUri(datFile.getAbsolutePath());
        metaData.setDuration(0);
        metaData.setCenterFrequency(name.centerFrequency());
        metaData.setFrequencyHigh(0);
        metaData.setFrequencyLow(0);
        metaData.setEncoding("i16");
        metaData.setRecordingDate(null);
        return metaData;

    }


}