package org.radiomuseum.cohiradia.server.servlet;

import org.apache.commons.io.IOUtils;
import org.radiomuseum.cohiradia.server.api.MetadataCache;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


@WebServlet(urlPatterns = "/files/wav/*")
public class WavServlet extends HttpServlet {

    @Inject
    MetadataCache cache;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var id = Util.getFolderId(req);
        var descriptor = cache.getDescriptor(id);
        var part = descriptor.parts().get(Integer.parseInt(req.getParameter("part")));

        resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", part.name()));

        var src = new BufferedInputStream(
                Files.newInputStream(new File(new File(descriptor.basePath()), part.name()).toPath(), StandardOpenOption.READ), 8192 * 1000);

        // Copy
        IOUtils.copy(src, resp.getOutputStream());
        resp.flushBuffer();
    }
}
