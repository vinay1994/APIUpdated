package com.flipLearn.bl.csvreader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    CSVParser csvParser;
    List<CSVRecord> csvRecords;

    public CSVReader(String path) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());
            csvRecords = csvParser.getRecords();
            csvParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRowCount() {
        return csvRecords.size();
    }

    public List<CSVRecord> getAllRecords() {
        return csvRecords;
    }

    public String getCellData(String columnName, int row) {
        return csvRecords.get(row).get(columnName);
    }

    public String getCellData(int columnNo, int row) {
        return csvRecords.get(row).get(columnNo);
    }

    public List<Object> convertToDataMap() {
        List<Object> dataList = new ArrayList<>();
        Map<String, Integer> headers = csvParser.getHeaderMap();
        for (CSVRecord csvRecord : getAllRecords()) {
            dataList.add(new HashMap<String, String>() {{
                for (String columnName : headers.keySet()) {
                    put(columnName, csvRecord.get(columnName));
                }
            }});
        }

        return dataList;
    }
}