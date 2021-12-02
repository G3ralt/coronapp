package com.alexosenov.coronavirus.model.service;

import com.alexosenov.coronavirus.model.CountryData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class URLService {

    private final String SOURCE_URL;

    public URLService() {
        SOURCE_URL = "https://www.worldometers.info/coronavirus/";
    }

    public List<CountryData> getTodayData() {
        try {
            Document doc = Jsoup.connect(SOURCE_URL).get();
            Element mainTable = Objects.requireNonNull(doc.getElementById("main_table_countries_today"));
            Elements rowElements = mainTable.select("tbody").select("tr");
            List<Element> dataRows = rowElements.stream()
                    .filter(element -> element.childNodeSize() == 43).collect(Collectors.toList());
            List<CountryData> countryDataList = getDataFromRows(dataRows);
            return countryDataList;
        } catch (IOException e) {
            log.error("Connection error: ", e);
        }
        return null;
    }

    private List<CountryData> getDataFromRows(List<Element> dataRows) {
        return dataRows.stream()
                .map(this::getDataFromElement)
                .collect(Collectors.toList());
    }

    private CountryData getDataFromElement(Element element) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        try {//TODO take the numbers from first row
            String countryName = element.children().get(1).children().text();
            int totalCases = format.parse(element.children().get(2).text()).intValue();
            int activeCases = format.parse(element.children().get(8).text()).intValue();
            int totalTests = format.parse(element.children().get(12).text()).intValue(); //TODO handle unparseable numbers
            String region = element.select("td[data-continent]").text();
            return CountryData.builder()
                    .countryName(countryName)
                    .totalCases(totalCases)
                    .activeCases(activeCases)
                    .totalTests(totalTests)
                    .region(region)
                    .build();
        } catch (ParseException e) {
            log.error("Could not extract data from element" + element, e);
            return new CountryData();
        }
    }
}
