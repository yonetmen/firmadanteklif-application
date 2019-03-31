package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.City;
import com.firmadanteklif.application.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    private CityRepository cityRepository;

    @Autowired
    public HomeService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCityNames() {
        return cityRepository.findAll();
    }
}
