package org.radiomuseum.cohiradia.server.view;

import org.radiomuseum.cohiradia.meta.dat.DatFilename;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.server.api.MetadataCache;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/view/recordings/*")
public class ViewRecording extends HttpServlet {

    final String content = """
            <!doctype html>
            <html>
            <head>
                  <meta charset="utf-8"/>
                  <title>Cohiradia Recordings (Radiomuseum.org)</title>
                  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/patternfly/3.24.0/css/patternfly.min.css">
            </head>
            <body>
                                    
            <div class="container">
                   <h1>Cohiradia Recording: ##title##</h1>
                   <p style="text-align:center"><img src="https://www.radiomuseum.org/img/cohiradia-logo.svg" style="width:40%;max-height:80px;"></p>
                   <a href="./">Overview of recordings...</a>
                   <h2>Recording Info</h2>
                   Center Frequency: ##center-frequency## kHz<br>
                   Sampling Rate: ##samples-per-sec## samples per second<br>
                   Start Date: ##start-date##<br>
                   <h2>Download for RFCorder</h2>
                   <ul>
                     <li>##rfcorder-link##</li>
                   </ul>
                   <h2>Downloads for SDRuno / SDR#</h2>
                   <ul>
                   ##list##
                   </ul>
                   <h2>Downloads for SDRuno / SDR# (Zipped)</h2>
                   <ul>
                     <li>##zipped##</li>
                   </ul>
                   <hr>
                   <footer>powered by <a href="https://www.radiomuseum.org">radiomuseum.org</a>.</footer>
            </div>
            </body>
            </html>""";

    @Inject
    MetadataCache cache;

    @Inject
    PathUtil pathUtil;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        var name = req.getPathInfo().substring(1);
        var id = 0;
        try {
            id = Integer.parseInt(name);
            name = pathUtil.getPath(id);
        } catch (NumberFormatException nfe) {
            // do nothing
        }

        var descriptor = cache.getDescriptor(id);

        if (descriptor == null) {
            req.getRequestDispatcher("/view/recordings").forward(req, resp);
            return;
        }

        var metadata = cache.get(id);
        var title = metadata.map(MetaData::getContent).orElse(descriptor.name());

        var n = name;
        descriptor.parts().stream()
                .map(f -> String.format("<li><a href=\"../../files/wav/%s?part=%s\">%s</a> (%s GB)</li>", n, descriptor.parts().indexOf(f), f.name(), f.sizeAsGB()))
                .forEach(sb::append);
        var rfcorderLink = String.format("<a href=\"../../files/dat/%s\">%s</a> (%s GB)", name, DatFilename.generate(descriptor), descriptor.sizeAsGB());

        var zipped = String.format("<a href=\"../../files/wavzip/%s?\">%s</a> (%s GB)", name, DatFilename.generate(descriptor).replace(".dat", ""), descriptor.sizeAsGB());

        // rendering
        resp.getWriter().write(
                content.replace("##list##", sb.toString())
                        .replace("##title##", title)
                        .replace("##rfcorder-link##", rfcorderLink)
                        .replace("##zipped##", zipped)
                        .replace("##center-frequency##", Integer.toString(descriptor.centerFrequency() / 1000))
                        .replace("##samples-per-sec##", Integer.toString(descriptor.sampleRate()))
                        .replace("##start-date##", descriptor.startDate().toString())
        );
    }
}
