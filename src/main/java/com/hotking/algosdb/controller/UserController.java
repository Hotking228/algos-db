package com.hotking.algosdb.controller;

import com.hotking.algosdb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String register(Model model,
                           @RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "email", required = false) String email){
        model.addAttribute("email", email == null ? "" : email);
        model.addAttribute("username", username == null ? "" : username);

        return "/user/registration";
    }

    @PostMapping("/registration")
    public String register(Model model){




        return "redirect:/user/login";
    }
}
