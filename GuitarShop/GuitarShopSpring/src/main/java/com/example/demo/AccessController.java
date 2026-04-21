package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {
	
	@GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("exMessage", "You don't have access to this page");
        return "pages/error";
    }
}
