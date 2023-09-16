package org.radiomuseum.cohiradia.cli;

import com.github.tomaslanger.chalk.Chalk;
import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "view", mixinStandardHelpOptions = true, version = "1.0",
        description = "Prints the SDRUno Meta Data Header to STDOUT.")
class SDRUnoMetaDataViewerCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The SDRUno WAVE file.")
    private File sdrUnoFile;

    @Override
    public Integer call() {
        var headers = SdrUnoHeaders.create(sdrUnoFile);
        System.out.println();
        System.out.println(Chalk.on("SDRUno Header").blue().bold().toString());
        System.out.println(Chalk.on("RIFF").blue().bold().toString());
        System.out.println(headers.riff());
        System.out.println(Chalk.on("FMT").blue().bold().toString());
        System.out.println(headers.fmt());
        System.out.println(Chalk.on("AUXI").blue().bold().toString());
        System.out.println(headers.auxi());
        System.out.println(Chalk.on("DATA").blue().bold().toString());
        System.out.println(headers.data());
        System.out.println();
        return 0;
    }


}