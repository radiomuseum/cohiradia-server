package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.cli.template.ExcelExport;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "yaml-excel-export", mixinStandardHelpOptions = true, version = "1.0",
        description = "Exports YAML metadata as Excel file (xlsx).")
class YAMLExcelCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The YAML file to export.")
    private File yaml;

    @Parameters(index = "1", description = "The XLSX target file.")
    private File xlsx;

    @Override
    public Integer call() {
        System.out.println();
        System.out.println(Chalk.on("YAML Metadata Export").blue().bold().toString());
        System.out.printf("Source: %s%n", yaml.getName());
        System.out.printf("Target: %s%n", xlsx.getName());
        var repository = new YamlRepository();
        new ExcelExport().yamlToXlsx(repository.read(yaml), xlsx);
        return 0;
    }
}