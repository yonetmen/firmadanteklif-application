package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.domain.entity.City;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
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
        log.info("Selected City is: " + selectedCityId.getId() + " - " + selectedCityId.getName());
        if(selectedCityId.getId() == 0) {
            return "redirect:/";
        } else {
            // Redirect user to "new post" page
        }
        return "redirect:/";
    }
}
