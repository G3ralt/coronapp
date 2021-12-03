package com.alexosenov.coronavirus.repository;

import com.alexosenov.coronavirus.model.CountryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryDataRepository extends JpaRepository<CountryData, Long> {
}
