package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.City;
import com.firmadanteklif.application.entity.UserPost;
import com.firmadanteklif.application.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
public class PostController {

    private CityRepository cityRepository;

    @Autowired
    public PostController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping("/new-post")
    public String newPost(Model model, @RequestParam("selectedCity") int id) {
        Optional<City> byId = cityRepository.findById(id);
        UserPost post = new UserPost();
        post.setCity(byId.get());
        log.info("New post: Selected City is: " + byId.get().getName());
        model.addAttribute("post", post);
        return "user/add-new";
    }

}
