package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.domain.entity.SiteCompany;
import com.firmadanteklif.application.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class CompanyController {

    private CompanyService companyService;

    @GetMapping("firma-giris")
    public String loginFirma(Model model) {
        model.addAttribute("loginFirma", new SiteCompany());
        return "firma/login";
    }

    @GetMapping("firma-kayit")
    public String registerFirma(Model model) {
        model.addAttribute("registerFirma", new SiteCompany());
        return "firma/register";
    }
}
