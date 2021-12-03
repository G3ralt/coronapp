package com.alexosenov.coronavirus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country_data")
@IdClass(CountryDataId.class)
public class CountryData {

    @Id
    @Column(name = "country")
    private String countryName;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private String region;

    private Integer totalCases;
    private Integer totalTests;
    private Integer activeCases;

//    @PrePersist
//    void setTodayDate() {
//        this.date = new Date();
//    }

}
