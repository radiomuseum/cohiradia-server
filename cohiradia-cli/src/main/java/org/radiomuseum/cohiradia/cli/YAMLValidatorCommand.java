package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "yaml-validate", mixinStandardHelpOptions = true, version = "1.0",
        description = "Validates the YAML metadata.")
class YAMLValidatorCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The YAML file or a directory of YAML files.")
    private File yaml;

    @Override
    public Integer call() {

        System.out.println();
        System.out.println(Chalk.on("YAML Metadata Validation").blue().bold().toString());

        List<File> files = new ArrayList<>();
        if (yaml.isDirectory()) {
            Arrays.stream(yaml.listFiles())
                    .filter(File::isFile)
                    .filter(f -> f.getName().endsWith(".yaml")).forEach(files::add);
        } else {
            files.add(yaml);
        }
        boolean hasError = false;
        for (File file : files) {
            try {
                var metadata = new YamlRepository().read2(file);
                System.out.println("- " + file.getName() + ": " + Chalk.on("OK").green().toString());
            } catch (Exception e) {
                System.out.println("- " + file.getName() + ": " + Chalk.on("FAILED").red().toString());
                System.out.println(Chalk.on(e.getMessage()).red().toString());
                hasError = true;
            }
        }

        return hasError ? 1 : 0;
    }


}