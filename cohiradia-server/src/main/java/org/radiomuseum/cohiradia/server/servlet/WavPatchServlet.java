package org.radiomuseum.cohiradia.server.servlet;

import org.radiomuseum.cohiradia.meta.sdruno.SdrUnoHeaders;
import org.radiomuseum.cohiradia.meta.wav.HeaderUtils;
import org.radiomuseum.cohiradia.server.ApplicationConfiguration;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


@WebServlet(urlPatterns = "/patch/wav/nfn/*")
public class WavPatchServlet extends HttpServlet {

    @Inject
    ApplicationConfiguration config;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var name = req.getPathInfo().substring(1);
        var file = new File(config.basePathStorage(), name);
        var header = SdrUnoHeaders.create(file);
        header.auxi().nextfilename("");
        HeaderUtils.replaceHeader(header, file);
        header = SdrUnoHeaders.create(file);
        resp.getWriter().print(header.toString());
    }
}
