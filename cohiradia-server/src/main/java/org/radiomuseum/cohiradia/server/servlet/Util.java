package org.radiomuseum.cohiradia.server.servlet;

import jakarta.servlet.http.HttpServletRequest;

public class Util {

    public static int getFolderId(HttpServletRequest req) {
        var name = req.getPathInfo().substring(1);
        try {
            return Integer.parseInt(name);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
}
