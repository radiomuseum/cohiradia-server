package org.radiomuseum.cohiradia.meta.wav;

import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HeaderUtils {

    public static void replaceHeader(SdrUnoHeaders headers, File fileToUpdate) throws IOException {
        var headerAsBytes = headers.toByteArray();
        try (RandomAccessFile raf = new RandomAccessFile(fileToUpdate, "rw")) {
            raf.seek(0);
            raf.write(headerAsBytes);
        }
    }

}
