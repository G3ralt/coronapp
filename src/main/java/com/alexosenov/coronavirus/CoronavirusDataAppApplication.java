package com.alexosenov.coronavirus;

import com.alexosenov.coronavirus.model.CountryData;
import com.alexosenov.coronavirus.model.service.URLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
public class CoronavirusDataAppApplication implements CommandLineRunner {

	@Autowired
	private URLService urlService;

	public static void main(String[] args) {
		SpringApplication.run(CoronavirusDataAppApplication.class, args);
	}


	@Override
	public void run(String... args) {
		List<CountryData> todayData = urlService.getTodayData();
		log.info(String.valueOf(todayData));
	}
}
