package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import org.radiomuseum.cohiradia.meta.sdruno.Validator;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Option;

@Command(name = "validate", mixinStandardHelpOptions = true, version = "1.0",
        description = "Validates the SDRUno Meta Data Header to STDOUT.")
class SDRUnoMetaDataValidatorCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The SDRUno WAVE file.")
    private File sdrUnoFile;

    @Option(names = {"-m", "--meta-data-file"}, paramLabel = "YAML Metadata File",
            description = "Uses the YAML Metadata File to validate the SDRUno header.")
    File yamlMetadataFile;

    @Override
    public Integer call() {

        System.out.println();
        System.out.println(Chalk.on("SDRUno Header Validation").blue().bold().toString());

        if (!sdrUnoFile.exists()) {
            System.out.println(Chalk.on("SDRUno WAVE File does not exist. Abort.").red().bold().toString());
            return 1;
        }
        System.out.println(Chalk.on("SDRUno WAVE File: ").blue().toString() + sdrUnoFile);

        if (yamlMetadataFile != null && yamlMetadataFile.exists()) {
            System.out.println(Chalk.on("YAML Metadata File: ").blue().toString() + yamlMetadataFile);
        } else if (yamlMetadataFile != null && !yamlMetadataFile.exists()) {
            System.out.println(Chalk.on("YAML Metadata File does not exist. Abort.").red().bold().toString());
            return 1;
        }

        System.out.println();
        var headers = SdrUnoHeaders.create(sdrUnoFile);
        Validator validator = new Validator();
        var result = validator.validate(sdrUnoFile, yamlMetadataFile);
        if (result.isEmpty()) {
            System.out.println(Chalk.on("SDRUno metadata has been successfully verified.").green().toString());
        } else {
            System.out.println(Chalk.on("SDRUno metadata failed some checks:").red().toString());
            result.stream().forEach(System.out::println);
        }
        System.out.println();

        return 0;
    }


}