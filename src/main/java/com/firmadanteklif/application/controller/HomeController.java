package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.City;
import com.firmadanteklif.application.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String getAllForHomePage(Model model) {
        List<City> all = homeService.getAllCityNames();
        model.addAttribute("cities", all);
        return "home";
    }
}
