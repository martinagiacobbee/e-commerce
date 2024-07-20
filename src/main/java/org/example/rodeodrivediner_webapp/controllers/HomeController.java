package org.example.rodeodrivediner_webapp.controllers;

import org.example.rodeodrivediner_webapp.security.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins="*")
public class HomeController {


    @GetMapping
    public String home(@RequestParam(value = "someValue") int value) {
        return "Welcome, " + Utils.getEmail() + " "+value  + " !";
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping("/username")
    public String getUser(){
        return "Welcome "+Utils.getUsername();
    }


}
