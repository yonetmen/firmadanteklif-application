package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.HomePage;
import com.firmadanteklif.application.repository.HomeRepository;
import com.firmadanteklif.application.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class IndexController {

    private HomeService homeService;
    private HomeRepository homeRepository;

    @Autowired
    public IndexController(HomeService homeService, HomeRepository homeRepository) {
        this.homeService = homeService;
        this.homeRepository = homeRepository;
    }

    @GetMapping("/cities")
    public String getAllCities(Model model) {
        List<HomePage> all = homeRepository.findAll();
        model.addAttribute("cities", all);
        return "home";
    }
}
