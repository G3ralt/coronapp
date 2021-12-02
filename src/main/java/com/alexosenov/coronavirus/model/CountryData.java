package com.alexosenov.coronavirus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryData {

    @Id
    private long id;
    @Column(name = "country")
    private String countryName;
    private String region;
    private int totalCases;
    private int totalTests;
    private int activeCases;
    private Date date;

}
