package org.radiomuseum.cohiradia.meta.yaml;

import java.util.List;
import java.util.Optional;

public class TableRenderer {

    public static String render(List<RadioStation> stations) {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>");
        sb.append("  <tr>");
        sb.append("    <th style=\"text-align:left\">Frequency</th>");
        sb.append("    <th style=\"text-align:left\">SNR</th>");
        sb.append("    <th style=\"text-align:left\">S-Level</th>");
        sb.append("    <th style=\"text-align:left\">Country</th>");
        sb.append("    <th style=\"text-align:left\">Programme</th>");
        sb.append("    <th style=\"text-align:left\">TX-Site</th>");
        sb.append("    <th style=\"text-align:left\">TX-Power</th>");
        sb.append("  </tr>");

        stations.forEach(l -> {
            if (l.getFrequenz() == null) {
                return;
            }

            sb.append("  <tr>");


            if (l.getFrequenz().isEmpty()) {
                sb.append("    <td></td>");
            } else {
                sb.append(String.format("    <td>%s kHz</td>", l.getFrequenz()));
            }
            sb.append(String.format("    <td>%s</td>", Optional.ofNullable(l.getSnrDb()).map(x -> x + " dB").orElse("-")));
            sb.append(String.format("    <td>%s</td>", Optional.ofNullable(l.getSLevel()).orElse("-")));
            sb.append(String.format("    <td>%s</td>", Optional.ofNullable(l.getCountry()).orElse("-")));
            sb.append(String.format("    <td>%s</td>", Optional.ofNullable(l.getProgramme()).orElse("-")).replace("(Quelle http://www.hfcc.org/data/ 2008B)", ""));
            sb.append(String.format("    <td>%s</td>", Optional.ofNullable(l.getTxLocation()).orElse("-")));
            sb.append(String.format("    <td>%s</td>", Optional.ofNullable(l.getTxPower()).orElse("-")));
            sb.append("  </tr>");
        });
        sb.append("</table>");

        return sb.toString();
    }
}
