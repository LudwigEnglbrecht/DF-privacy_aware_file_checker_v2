package com.ur.seminar.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * This method is the main controller returning the index.html file
     * The /l parameter assigns the controller to the standard IP
     * @return the index.html file containing the code for the user interface
     * **/
    @GetMapping("/l")
    public String index() {
        return "index";
    }

}