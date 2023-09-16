package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import org.radiomuseum.cohiradia.meta.wav.HeaderUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Command(name = "sdruno-delete-next-filename", mixinStandardHelpOptions = true, version = "1.0",
        description = "Removes the next-filename header.")
class SDRUnoDeleteNextFilenameCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The SDRUno WAVE file.")
    private File sdrUnoFile;

    @Override
    public Integer call() {
        System.out.println();
        System.out.println(Chalk.on("SDRUno Delete Next Filename").blue().bold().toString());

        if (!sdrUnoFile.exists()) {
            System.out.println(Chalk.on("SDRUno WAVE File does not exist. Abort.").red().bold().toString());
            return 1;
        }

        System.out.println(Chalk.on("SDRUno WAVE File: ").blue().toString() + sdrUnoFile);
        var headers = SdrUnoHeaders.create(sdrUnoFile);
        headers.auxi().nextfilename("");

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