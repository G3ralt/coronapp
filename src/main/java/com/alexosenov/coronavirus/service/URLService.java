package com.alexosenov.coronavirus.service;

import com.alexosenov.coronavirus.model.CountryData;
import com.alexosenov.coronavirus.repository.CountryDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class URLService {

    private final String SOURCE_URL;
    private final NumberFormat FORMAT;

    @Autowired
    private CountryDataRepository countryDataRepository;

    public URLService() {
        SOURCE_URL = "https://www.worldometers.info/coronavirus/";
        FORMAT = NumberFormat.getNumberInstance(Locale.US);
    }

    public List<CountryData> getTodayData() {
        try {
            Document doc = Jsoup.connect(SOURCE_URL).get();
            List<Element> dataRows = getDataRows(doc);
            List<CountryData> countryDataList = getDataFromRows(dataRows);
            countryDataRepository.saveAll(countryDataList);
            return countryDataList;
        } catch (IOException e) {
            log.error("Connection error: ", e);
        } catch (Exception e) {
            log.error("Exception occurred: ", e);
        }
        return null;
    }

    private List<Element> getDataRows(Document doc) {
        Element mainTable = Objects.requireNonNull(doc.getElementById("main_table_countries_today"));
        Elements rowElements = mainTable.select("tbody").select("tr");
        return rowElements.stream()
                .filter(element -> element.childNodeSize() == 43).collect(Collectors.toList());
    }

    private List<CountryData> getDataFromRows(List<Element> dataRows) {
        return dataRows.stream()
                .map(this::getDataFromElement)
                .collect(Collectors.toList());
    }

    private CountryData getDataFromElement(Element element) {

        String countryName = element.children().get(1).children().text();
        Integer totalCases = getNumberFromString(element.children().get(2).text());
        Integer activeCases = getNumberFromString(element.children().get(8).text());
        Integer totalTests = getNumberFromString(element.children().get(12).text());
        String region = element.select("td[data-continent]").text();
        return CountryData.builder()
                .countryName(countryName.isBlank()  ? region : countryName)
                .totalCases(totalCases)
                .activeCases(activeCases)
                .totalTests(totalTests)
                .region(region)
                .date(new Date())
                .build();
    }

    private Integer getNumberFromString(String text) {
        try {
            return FORMAT.parse(text).intValue();
        } catch (ParseException e) {
            log.warn("Could not extract number from: " + text);
        }
        return null;
    }


}






























