package com.alexosenov.coronavirus.service;

import com.alexosenov.coronavirus.model.CountryData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CSVService {

    public void exportDataToCSV(List<CountryData> dataByRegion, String region) {
        String fileName = createFileName(dataByRegion, region);
        File outputFile = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            dataByRegion.stream()
                    .map(this::transformToCSV)
                    .forEach(printWriter::println);
        } catch (FileNotFoundException e) {
            log.error("Error writing data", e);
        }
        log.info("Export successful: {}", outputFile.exists());
    }

    private String transformToCSV(CountryData countryData) {
        List<String> dataAsList = getStringArrayFromIntegers(new Integer[]{countryData.getActiveCases(), countryData.getTotalCases(), countryData.getTotalTests()});
        dataAsList.add(countryData.getCountryName());
        Collections.reverse(dataAsList);
        return dataAsList.stream()
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String str) {
        String escaped = str.replaceAll("\\R", " ");
        if (str.contains(",") || str.contains("\"") || str.contains("'")) {
            str = str.replace("\"", "\"\"");
            escaped = "\"" + str + "\"";
        }
        return escaped;
    }

    private List<String> getStringArrayFromIntegers(Integer[] integers) {
        return Arrays.stream(integers).map(integer -> Objects.isNull(integer) ? "N/A" : integer.toString()).collect(Collectors.toList());
    }

    private String createFileName(List<CountryData> dataByRegion, String region) {
        Date date = dataByRegion.stream().findFirst().get().getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MM_dd");
        return String.format("export_%s_%s.csv", region.toLowerCase(), dateFormat.format(date));
    }
}
