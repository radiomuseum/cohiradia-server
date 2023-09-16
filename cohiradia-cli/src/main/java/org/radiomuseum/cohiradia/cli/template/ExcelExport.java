package org.radiomuseum.cohiradia.cli.template;

import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;

import java.io.File;
import java.io.FileOutputStream;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelExport {

    @SneakyThrows
    public void yamlToXlsx(MetaData metaData, File file) {
        var wb = new XSSFWorkbook();
        createMetadataSheet(metaData, wb);
        createRadioStationSheet(metaData, wb);
        wb.write(new FileOutputStream(file));
    }

    private void createMetadataSheet(MetaData metaData, XSSFWorkbook wb) {
        var metadataSheet = wb.createSheet("metadata");
        // metadataSheet.protectSheet("abcd");

        var styleBold = createStyleBold(wb);
        var styleWrapped = createStyleWrapped(wb);
        var row = metadataSheet.createRow(0);
        fillAndFormatHeaderCell(row, 0, "Name", styleBold);
        fillAndFormatHeaderCell(row, 1, "Value", styleBold);

        var properties = PropertyUtils.getPropertyDescriptors(MetaData.class);

        metadataSheet.setColumnWidth(0, 256 * 25);
        metadataSheet.setColumnWidth(1, 256 * 80);

        var rowCounter = new AtomicInteger(1);

        Arrays.stream(MetaData.class.getDeclaredFields())
                .filter(e -> !e.getName().toLowerCase().contains("html"))
                //.sorted((e1, e2) -> String.CASE_INSENSITIVE_ORDER.compare(e1.getName(), e2.getName()))
                .forEach(e -> {
                    try {
                        var dataRow = metadataSheet.createRow(rowCounter.get());
                        fillAndFormatHeaderCell(dataRow, 0, e.getName(), styleBold);

                        var valueCell = dataRow.createCell(1);
                        switch (e.getType().getName()) {
                            case "double":
                            case "int":
                                valueCell.setCellValue(Double.parseDouble(BeanUtils.getProperty(metaData, e.getName())));
                                break;
                            case "java.lang.String":
                                valueCell.setCellValue(BeanUtils.getProperty(metaData, e.getName()));
                                valueCell.setCellStyle(styleWrapped);
                                break;
                            case "java.time.ZonedDateTime":
                                valueCell.setCellType(CellType.BLANK);
                                valueCell.setCellValue(((ZonedDateTime) PropertyUtils.getProperty(metaData, e.getName())).toLocalDateTime());
                                break;
                            case "java.util.List":
                                break;
                            default:
                                throw new IllegalStateException(String.format("Unknown Type: %s", e.getType().getName()));
                        }
                        rowCounter.incrementAndGet();
                    } catch (Exception x) {
                        x.printStackTrace();
                    }
                });
    }

    private void createRadioStationSheet(MetaData metaData, XSSFWorkbook wb) {
        var stationsSheet = wb.createSheet("stations");
        var styleBold = createStyleBold(wb);
        var styleWrapped = createStyleWrapped(wb);
        var row = stationsSheet.createRow(0);
        fillAndFormatHeaderCell(row, 0, "Frequency", styleBold);
        fillAndFormatHeaderCell(row, 1, "SNR", styleBold);
        fillAndFormatHeaderCell(row, 2, "S-Level", styleBold);
        fillAndFormatHeaderCell(row, 3, "Programme", styleBold);
        fillAndFormatHeaderCell(row, 4, "Country", styleBold);
        fillAndFormatHeaderCell(row, 5, "TX-Site", styleBold);
        fillAndFormatHeaderCell(row, 6, "TX-Power", styleBold);
        fillAndFormatHeaderCell(row, 7, "Remarks", styleBold);

        var properties = PropertyUtils.getPropertyDescriptors(MetaData.class);

        stationsSheet.setColumnWidth(0, 256 * 10);
        stationsSheet.setColumnWidth(1, 256 * 20);
        stationsSheet.setColumnWidth(2, 256 * 20);
        stationsSheet.setColumnWidth(3, 256 * 20);
        stationsSheet.setColumnWidth(4, 256 * 20);
        stationsSheet.setColumnWidth(5, 256 * 20);
        stationsSheet.setColumnWidth(6, 256 * 20);
        stationsSheet.setColumnWidth(7, 256 * 20);

        var rowCounter = new AtomicInteger(1);
        for (var station : metaData.getRadioStations()) {
            var dataRow = stationsSheet.createRow(rowCounter.get());
            createStringCell(styleWrapped, dataRow, 0, station.getFrequenz());
            createStringCell(styleWrapped, dataRow, 1, station.getSnrDb());
            createStringCell(styleWrapped, dataRow, 2, station.getSLevel());
            createStringCell(styleWrapped, dataRow, 3, station.getProgramme());
            createStringCell(styleWrapped, dataRow, 4, station.getCountry());
            createStringCell(styleWrapped, dataRow, 5, station.getTxLocation());
            createStringCell(styleWrapped, dataRow, 6, station.getTxPower());
            createStringCell(styleWrapped, dataRow, 7, station.getRemarks());
            rowCounter.incrementAndGet();
        }
    }

    private void createStringCell(XSSFCellStyle styleWrapped, XSSFRow dataRow, int column, String value) {
        var valueCell = dataRow.createCell(column);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(styleWrapped);
    }


    private void fillAndFormatHeaderCell(XSSFRow row, int columnIndex, String Name, CellStyle styleBold) {
        var nameCell = row.createCell(columnIndex);
        nameCell.setCellValue(Name);
        nameCell.setCellStyle(styleBold);
    }

    private XSSFCellStyle createStyleBold(XSSFWorkbook wb) {
        var style = wb.createCellStyle();
        var font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setLocked(true);
        return style;
    }

    private XSSFCellStyle createStyleWrapped(XSSFWorkbook wb) {
        var style = wb.createCellStyle();
        style.setWrapText(true);
        return style;
    }
}
