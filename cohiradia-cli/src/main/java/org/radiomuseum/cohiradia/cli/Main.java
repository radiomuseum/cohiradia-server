package org.radiomuseum.cohiradia.cli;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;

@QuarkusMain
public class Main implements QuarkusApplication {

    @Override
    public int run(String... args) {
        CommandLine cmd = new CommandLine(new ParentCommand());
        cmd.setExecutionStrategy(new CommandLine.RunAll()); // default is RunLast
        int exitCode = cmd.execute(args);

        if (args.length == 0) {
            cmd.usage(System.out);
        }
        return exitCode;
    }

    @CommandLine.Command(name = "cohiradia-cli", subcommands = {SDRUnoMetaDataViewerCommand.class,
            SDRUnoMetaDataValidatorCommand.class,
            SDRUnoMetaDataUpdateCommand.class,
            SDRUnoDeleteNextFilenameCommand.class,
            YAMLValidatorCommand.class,
            YAMLCreateCommand.class,
            YAMLIdWriterCommand.class,
            YAMLExcelCommand.class,

            CommandLine.HelpCommand.class}, description = "Cohiradia Command Line Tool")
    static class ParentCommand implements Runnable {

        @Override
        public void run() {
            // empty
        }
    }
}
