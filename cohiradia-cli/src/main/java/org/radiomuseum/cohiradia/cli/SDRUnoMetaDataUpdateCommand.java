package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import org.radiomuseum.cohiradia.meta.wav.HeaderUtils;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Option;

@Command(name = "sdruno-update", mixinStandardHelpOptions = true, version = "1.0",
        description = "Updates the SDRUno Meta Data Header with YAML Metadata.")
class SDRUnoMetaDataUpdateCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The SDRUno WAVE file.")
    private File sdrUnoFile;

    @Option(names = {"-m", "--meta-data-file"}, paramLabel = "YAML Metadata File",
            description = "Uses the YAML Metadata File to update the SDRUno header.", required = true)
    File yamlMetadataFile;

    @Override
    public Integer call() {

        System.out.println();
        System.out.println(Chalk.on("SDRUno Header Update").blue().bold().toString());

        if (!sdrUnoFile.exists()) {
            System.out.println(Chalk.on("SDRUno WAVE File does not exist. Abort.").red().bold().toString());
            return 1;
        }
        System.out.println(Chalk.on("SDRUno WAVE File: ").blue().toString() + sdrUnoFile);

        if (yamlMetadataFile == null || !yamlMetadataFile.exists()) {
            System.out.println(Chalk.on("YAML Metadata File does not exist. Abort.").red().bold().toString());
            return 1;
        }

        var metaData = new YamlRepository().read(yamlMetadataFile);
        var headers = SdrUnoHeaders.create(sdrUnoFile);

        headers.riff().filesize(sdrUnoFile.length() - 8);
        headers.data().fileSize(sdrUnoFile.length() - SdrUnoHeaders.HEADER_SIZE);
        headers.auxi().centerFreq(metaData.centerFrequencyAsHz());
        headers.auxi().startTime(metaData.getRecordingDate());
        headers.auxi().stopTime(metaData.getRecordingDate().plus(metaData.getDuration(), ChronoUnit.SECONDS));

        if (!new File(sdrUnoFile.getParentFile(), headers.auxi().nextfilename()).exists()) {
            headers.auxi().nextfilename("");
        }

        try {
            HeaderUtils.replaceHeader(headers, sdrUnoFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Header updated.");

        System.out.println();

        return 0;
    }


}