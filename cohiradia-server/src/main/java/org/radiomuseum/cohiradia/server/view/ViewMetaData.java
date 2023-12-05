package org.radiomuseum.cohiradia.server.view;

import org.radiomuseum.cohiradia.server.ApplicationConfiguration;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@WebServlet(urlPatterns = "/view/metadata")
public class ViewMetaData extends HttpServlet {

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
                   <h1>Cohiradia Metadata</h1>
                   <ul>
                   ##list##
                   </ul>
                   <hr>
                   <footer>powered by <a href="https://www.radiomuseum.org">radiomuseum.org</a>.</footer>
            </div>
            </body>
            </html>""";

    @Inject
    ApplicationConfiguration config;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(Objects.requireNonNull(config.basePathMetadata().listFiles()))
                .filter(File::isFile)
                .map(f -> String.format("<li>%s</li>", f.getName()))
                .sorted(String::compareTo)
                .forEach(sb::append);
        resp.getWriter().write(content.replace("##list##", sb.toString()));
    }
}
