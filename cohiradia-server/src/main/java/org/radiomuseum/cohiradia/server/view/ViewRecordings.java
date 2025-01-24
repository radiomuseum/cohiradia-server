package org.radiomuseum.cohiradia.server.view;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.server.api.MetadataCache;

import java.io.IOException;

@WebServlet(urlPatterns = "/view/recordings")
public class ViewRecordings extends HttpServlet {

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
                   <h1>Cohiradia Recordings</h1>
                   <ul>
                   ##list##
                   </ul>
                   <hr>
                   <footer>powered by <a href="https://www.radiomuseum.org">radiomuseum.org</a>.</footer>
            </div>
            </body>
            </html>""";

    @Inject
    MetadataCache cache;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();

        cache.getAllDescriptors()
                .map(f -> String.format("<li><a href=\"%s\">%s</a></li>", f.id(), toString(cache.get(f.id()).orElse(null))))
                .sorted(String::compareTo)
                .forEach(sb::append);

        resp.getWriter().write(content.replace("##list##", sb.toString()));
    }

    private String toString(MetaData metadata) {
        if (metadata == null) {
            return "Unknown.";
        }
        return String.format("%s: %s, uri=[%s], id=[%s]", metadata.getRecordingDate(), metadata.getContent(), metadata.getUri(), metadata.getId());
    }
}
