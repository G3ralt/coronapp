package com.alexosenov.coronavirus.model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
public class CountryDataId implements Serializable {
    private String countryName;
    private Date date;
}
