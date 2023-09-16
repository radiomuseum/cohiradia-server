package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import picocli.CommandLine;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@CommandLine.Command(name = "yaml-generate-id", mixinStandardHelpOptions = true, version = "1.0",
        description = "Updates the SDRUno Meta Data Header with YAML Metadata.")
public class YAMLIdWriterCommand implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "The directory of YAML files.")
    private File yaml;

    public Integer call() throws Exception {
        var repository = new YamlRepository();
        var files = new HashMap<File, MetaData>();
        if (yaml.isDirectory()) {
            for (File f : yaml.listFiles()) {
                files.put(f, repository.read(f));
            }
        } else {
            throw new IllegalArgumentException("file must be a directory.");
        }

        var duplicates = new HashSet<Integer>();
        for (var entry : files.entrySet()) {
            var id = entry.getValue().getId();
            if (id != 0 && !duplicates.add(id)) {
                System.out.println(Chalk.on("Error: Duplicate identifier [" + id + "]").red().bold().toString());
                files.entrySet().stream()
                        .filter(e -> e.getValue().getId() == id)
                        .forEach(e -> System.out.println("- " + e.getKey().getName()));
                System.out.println("Please resolve the conflict manually.");
                System.out.println();
                return 1;
            }
        }

        AtomicInteger idGenerator = new AtomicInteger(
                files.values().stream()
                        .mapToInt(MetaData::getId)
                        .max().orElse(1) + 1);

        for (var es : files.entrySet()) {
            if (es.getValue().getId() == 0) {
                es.getValue().setId(idGenerator.incrementAndGet());
                repository.persistYaml(es.getKey(), es.getValue());
            }
        }
        return 0;
    }
}
