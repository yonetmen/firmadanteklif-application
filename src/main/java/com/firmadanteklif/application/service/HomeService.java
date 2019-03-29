package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.HomePage;
import com.firmadanteklif.application.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    private HomeRepository homeRepository;

    @Autowired
    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    public List<HomePage> findAll() {
        return homeRepository.findAll();
    }
}
