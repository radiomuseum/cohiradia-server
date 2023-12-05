package org.radiomuseum.cohiradia.server.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.radiomuseum.cohiradia.server.api.MetadataCache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@WebServlet(urlPatterns = "/files/wavzip/*")
public class WavZipServlet extends HttpServlet {

    @Inject
    MetadataCache cache;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var id = Util.getFolderId(req);
        var descriptor = cache.getDescriptor(id);

        resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", descriptor.name().toLowerCase() + ".zip"));

        ZipOutputStream out = new ZipOutputStream(resp.getOutputStream());
        out.setLevel(Deflater.NO_COMPRESSION);

        for (var part : descriptor.parts()) {
            var src = new BufferedInputStream(
                    Files.newInputStream(new File(new File(descriptor.basePath()), part.name()).toPath(), StandardOpenOption.READ), 8192 * 1000);
            ZipEntry e = new ZipEntry(part.name());
            out.putNextEntry(e);
            IOUtils.copy(src, out);
            out.closeEntry();
        }

        out.close();
        resp.flushBuffer();
    }
}
