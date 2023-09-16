package org.radiomuseum.cohiradia.cli.template;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;

import java.io.FileOutputStream;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelListExport {

    @SneakyThrows
    public void yamlToXlsx(List<MetaData> metaData) {
        var wb = new XSSFWorkbook();
        createRadioStationSheet(metaData, wb);
        wb.write(new FileOutputStream("list.xlsx"));
    }

    private void createRadioStationSheet(List<MetaData> metaData, XSSFWorkbook wb) {
        var stationsSheet = wb.createSheet("stations");
        var styleBold = createStyleBold(wb);
        var styleWrapped = createStyleWrapped(wb);
        var row = stationsSheet.createRow(0);
        fillAndFormatHeaderCell(row, 0, "ID", styleBold);
        fillAndFormatHeaderCell(row, 1, "URI", styleBold);
        fillAndFormatHeaderCell(row, 2, "Start", styleBold);
        fillAndFormatHeaderCell(row, 3, "Start UTC", styleBold);
        fillAndFormatHeaderCell(row, 4, "Content", styleBold);

        stationsSheet.setColumnWidth(0, 256 * 10);
        stationsSheet.setColumnWidth(1, 256 * 20);
        stationsSheet.setColumnWidth(2, 256 * 20);
        stationsSheet.setColumnWidth(3, 256 * 20);
        stationsSheet.setColumnWidth(4, 256 * 20);

        var rowCounter = new AtomicInteger(1);
        for (var m : metaData) {
            var dataRow = stationsSheet.createRow(rowCounter.get());
            createStringCell(styleWrapped, dataRow, 0, Integer.toString(m.getId()));
            createStringCell(styleWrapped, dataRow, 1, m.getUri());
            createStringCell(styleWrapped, dataRow, 2, m.getRecordingDate().toString());
            createStringCell(styleWrapped, dataRow, 3, m.getRecordingDate().withZoneSameInstant(ZoneId.of("UTC")).toString());
            createStringCell(styleWrapped, dataRow, 4, m.getContent());
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
