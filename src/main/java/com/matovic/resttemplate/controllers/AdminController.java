package com.matovic.resttemplate.controllers;

import com.matovic.resttemplate.models.PageRepository;
import com.matovic.resttemplate.models.entities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/pages")
public class AdminController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PageRepository pageRepo;

    @GetMapping
    public String home(Model model){
        ResponseEntity<List<Page>> response = restTemplate
                .exchange("http://localhost:8080/admin/pages",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Page>>() {});

        List<Page> pages = (List<Page>) response.getBody();
        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Page page) {
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        if(bindingResult.hasErrors()){
            return "admin/pages/add";   // vracamo samo gresku
        }

        redirectAttributes.addFlashAttribute("message", "Page added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");    // message for bootstrap

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
                : page.getSlug().toLowerCase().replace(" ", "-");

        Page slugExists = pageRepo.findBySlug(slug);
        if(slugExists != null){
            redirectAttributes.addFlashAttribute("message", "Slug exists, chose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);
            page.setSorting(100);   // svaka strana koja se doda postaje zadnja

            restTemplate.postForObject("http://localhost:8080/admin/pages", page, Page.class);
        }

        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Page page = restTemplate.getForObject("http://localhost:8080/admin/pages/edit/{id}", Page.class, id);
        model.addAttribute("page", page);
        return "admin/pages/edit";
    }


    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        if(bindingResult.hasErrors()){
            return "admin/pages/edit";   // vracamo samo gresku
        }

        redirectAttributes.addFlashAttribute("message", "Page edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");    // message for bootstrap

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
                : page.getSlug().toLowerCase().replace(" ", "-");

        Page slugExists = pageRepo.findBySlugAndIdNot(slug, page.getId());
        // Page slugExists = restTemplate.getForObject("http://localhost:8080/admin/pages/findBySlugAndIdNot/{slug}/{id}", Page.class, slug, page.getId());
        if(slugExists != null){
            redirectAttributes.addFlashAttribute("message", "Slug exists, chose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);
            page.setSorting(100);   // svaka strana koja se doda postaje zadnja

            restTemplate.put("http://localhost:8080/admin/pages/edit", page);
        }

        return "redirect:/admin/pages/edit/" + page.getId();
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        restTemplate.delete("http://localhost:8080/admin/pages/delete/{id}", id);
        redirectAttributes.addFlashAttribute("message", "Page deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/pages";
    }

}
