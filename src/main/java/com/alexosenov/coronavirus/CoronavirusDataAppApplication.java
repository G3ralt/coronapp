package com.alexosenov.coronavirus;

import com.alexosenov.coronavirus.model.CountryData;
import com.alexosenov.coronavirus.service.CSVService;
import com.alexosenov.coronavirus.service.URLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class CoronavirusDataAppApplication implements CommandLineRunner {

    @Autowired
    private URLService urlService;

    @Autowired
    private CSVService csvService;

    public static void main(String[] args) {
        SpringApplication.run(CoronavirusDataAppApplication.class, args);
    }


    @Override
    public void run(String... args) {
        List<CountryData> todayData = urlService.getTodayData();
        if (args.length > 0) {
            String regionArg = args[0];
            List<CountryData> dataByRegion = todayData.stream().filter(countryData -> countryData.getRegion().equals(regionArg)).collect(Collectors.toList());
            if (dataByRegion.size() > 0) {
                csvService.exportDataToCSV(dataByRegion, regionArg);
            } else {
                log.error("Region doesn't exist: {}", regionArg);
            }
        }
    }
}
