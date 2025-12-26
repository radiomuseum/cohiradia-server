package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.apache.logging.log4j.util.Strings;
import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import org.radiomuseum.cohiradia.meta.wav.HeaderUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;

@Command(name = "sdruno-cleanup-filenames", mixinStandardHelpOptions = true, version = "1.0",
        description = "Cleanup next-filename headers.")
class SDRUnoCleanupNextFilenameCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The SDRUno Directory.")
    private File directory;

    @CommandLine.Option(
            names = "--dry-run",
            description = "Nur simulieren"
    )
    private boolean dryRun;

    @Override
    public Integer call() {
        System.out.println();
        System.out.println(Chalk.on("SDRUno Cleanup Filenames").blue().bold().toString());

        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            System.out.println(Chalk.on("SDRUno directory does not exist. Abort.").red().bold().toString());
            return 1;
        }

        for(var file: Objects.requireNonNull(directory.listFiles((dir, name) -> name.endsWith(".wav")))) {
            System.out.println(Chalk.on("SDRUno WAVE File: ").blue().toString() + file);
            var headers = SdrUnoHeaders.create(file);

            var nextFilename = headers.auxi().nextfilename();
            if(!Strings.isBlank(nextFilename)){
                File f = new File(getFilename(nextFilename));
                if(!nextFilename.equals(f.getName()) && f.exists()) {
                    System.out.println(Chalk.on("Change filename to " + f.getName()).red());
                    headers.auxi().nextfilename(f.getName());
                    if(!dryRun){
                        writeHeaders(file, headers);
                    }
                } else if(!f.exists()){
                    System.out.println(Chalk.on("Change filename to empty").red());
                    headers.auxi().nextfilename("");
                    if(!dryRun){
                        writeHeaders(file, headers);
                    }
                } else {
                    System.out.println("Skip.");
                }
            } else {
                System.out.println("Skip.");
            }
        }
        System.out.println();
        return 0;
    }

    private static void writeHeaders(File file, SdrUnoHeaders headers) {
        try {
            HeaderUtils.replaceHeader(headers, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilename(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }

        int lastSlash = Math.max(
                path.lastIndexOf('/'),
                path.lastIndexOf('\\')
        );

        return lastSlash >= 0
                ? path.substring(lastSlash + 1)
                : path;
    }

}