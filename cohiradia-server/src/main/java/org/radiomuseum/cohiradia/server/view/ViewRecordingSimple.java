package org.radiomuseum.cohiradia.server.view;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.radiomuseum.cohiradia.meta.dat.DatFilename;
import org.radiomuseum.cohiradia.meta.descriptor.DescriptorRepository;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(urlPatterns = "/view/download/*")
public class ViewRecordingSimple extends HttpServlet {

    final String content = """
            <!doctype html>
            <html>
            <head>
                  <meta ‡charset="utf-8"/>
                  <title>Cohiradia Recordings (Radiomuseum.org)</title>
                  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/patternfly/3.24.0/css/patternfly.min.css">
            </head>
            <body>
                                    
            <div class="container">
                   <h2>Download for RFCorder</h2>
                   <ul>
                     <li>##rfcorder-link##</li>
                   </ul>
                   <h2>Downloads for SDRuno / SDR#</h2>
                   <ul>
                     ##list##
                   </ul>
            </div>
            </body>
            </html>""";

    @Inject
    @ConfigProperty(name = "rmorg.sdruno.base", defaultValue = "./tmp/sdruno")
    File basePathSdruno;

    @Inject
    DescriptorRepository repository;

    @Inject
    PathUtil pathUtil;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        var name = req.getPathInfo().substring(1);
        try {
            var id = Integer.parseInt(name);
            name = pathUtil.getPath(id);
        } catch (NumberFormatException nfe) {
            // do nothing
        }

        var folder = new File(basePathSdruno, name);

        var descriptorFile = new File(folder, "descriptor.yaml");
        if (!descriptorFile.exists()) {
            req.getRequestDispatcher("/view/recordings").forward(req, resp);
            return;
        }
        var descriptor = repository.read(descriptorFile);
        var n = name;
        descriptor.parts().stream()
                .map(f -> String.format("<li><a href=\"../../files/wav/%s?part=%s\">%s</a> (%s GB)</li>", n, descriptor.parts().indexOf(f), f.name(), f.sizeAsGB()))
                .forEach(sb::append);
        var rfcorderLink = String.format("<a href=\"../../files/dat/%s\">%s</a> (%s GB)", name, DatFilename.generate(descriptor), descriptor.sizeAsGB());


        // rendering
        resp.getWriter().write(
                content.replace("##list##", sb.toString())
                        .replace("##title##", descriptor.name())
                        .replace("##rfcorder-link##", rfcorderLink)
                        .replace("##center-frequency##", Integer.toString(descriptor.centerFrequency() / 1000))
                        .replace("##samples-per-sec##", Integer.toString(descriptor.sampleRate()))
                        .replace("##start-date##", descriptor.startDate().toString())
        );
    }
}
