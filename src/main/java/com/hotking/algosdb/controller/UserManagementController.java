package com.hotking.algosdb.controller;

import com.hotking.algosdb.dto.RegisterForm;
import com.hotking.algosdb.email.EmailService;
import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.enums.Role;
import com.hotking.algosdb.enums.Status;
import com.hotking.algosdb.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@SessionAttributes("userId")
public class UserManagementController {

    private Random rnd = new Random();
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    @GetMapping("/register")
    public String showRegister(Model model,
                               @RequestParam(name = "registerForm", required = false) RegisterForm registerForm){
        if(registerForm == null) {
            registerForm = new RegisterForm();
        }
        model.addAttribute("registerForm", registerForm);

        return "management/register";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @Valid @ModelAttribute RegisterForm registerForm,
                           BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
                System.out.println(error.getField() + " " + error.getDefaultMessage());
            }

            if(bindingResult.hasGlobalErrors()){
                errors.put("global", bindingResult.getGlobalError().getDefaultMessage());
                System.out.println("global" + " " + bindingResult.getGlobalError().getDefaultMessage());
            }
            return "management/register";
        }

        Integer mailPass = rnd.nextInt(100_000, 1_000_000);
        System.out.println(mailPass);
        User user = User.builder()
                .username(registerForm.getUsername())
                .email(registerForm.getEmail())
                .password(encoder.encode(registerForm.getPassword()))
                .role(Role.USER)
                .status(Status.PENDING)
                .mailpassword(encoder.encode(mailPass.toString()))
                .build();
        emailService.sendMessage(user.getEmail(), "verification code", mailPass.toString());



        registerForm = null;

        int id = userService.save(user);
        model.addAttribute("userId", id);
        return "redirect:/confirmCode";
    }

    @GetMapping("/confirmCode")
    public String showConfirmCode(Model model,
                                  @SessionAttribute("userId") Integer userId){
        System.out.println(userId);
        return "management/confirmCode";
    }

    @PostMapping("/confirmCode")
    public String confirmCode(Model model,
                              @SessionAttribute("userId") Integer userId,
                              @RequestParam("code") Integer code){

        System.out.println(code);
        String cryptCode = encoder.encode(code.toString());

        User user = userService.getById(userId).get();
        System.out.println(cryptCode);
        System.out.println(user.getMailpassword());
        if(encoder.matches(code.toString(), user.getMailpassword())) {
            user.setStatus(Status.REGISTERED);
            user.setMailpassword(null);
            userService.save(user);
            return "management/login";
        } else {
            model.addAttribute("error", "Код подтверждения не совпадает!");
            return "redirect:/confirmCode";
        }
    }
}
