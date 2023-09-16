package org.radiomuseum.cohiradia.server.servlet;

import org.apache.commons.io.IOUtils;
import org.radiomuseum.cohiradia.meta.dat.DatFilename;
import org.radiomuseum.cohiradia.server.api.MetadataCache;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


@WebServlet(urlPatterns = "/files/dat/*")
public class RFCorderServlet extends HttpServlet {

    @Inject
    MetadataCache cache;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var id = Util.getFolderId(req);
        var descriptor = cache.getDescriptor(id);
        resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", DatFilename.generate(descriptor)));

        for (var part : descriptor.parts()) {
            var src = new BufferedInputStream(
                    Files.newInputStream(new File(new File(descriptor.basePath()), part.name()).toPath(), StandardOpenOption.READ), 8192 * 1000);
            // Skip Header
            src.skipNBytes(216);
            // Copy
            IOUtils.copy(src, resp.getOutputStream());
        }

        resp.flushBuffer();
    }
}
