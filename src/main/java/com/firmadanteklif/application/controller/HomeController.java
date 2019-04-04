package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.City;
import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("selectedCity", new City());
        model.addAttribute("loginUser", new SiteUser());
        return "home";
    }

    @PostMapping("/add-new-post")
    public String addNewPost(@ModelAttribute(value = "selectedCity") City selectedCityId) {
        System.out.println("Selected City ID: " + selectedCityId.getId());
        System.out.println("Selected City is: " + selectedCityId.getName());
        return "redirect:/";
    }
}
