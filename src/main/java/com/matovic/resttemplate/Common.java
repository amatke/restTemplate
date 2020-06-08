package com.matovic.resttemplate;

import com.matovic.resttemplate.models.entities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ControllerAdvice
public class Common {

    @Autowired
    private RestTemplate restTemplate;

    @ModelAttribute
    public void sharedData(Model model){

        // ovako uzimamo listu strana
        ResponseEntity<List<Page>> response =
                restTemplate.exchange("http://localhost:8080/pages",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Page>>() {});

        List<Page> pages = response.getBody();  // sad ih imamo dostupnim

        model.addAttribute("cpages", pages);
    }
}
