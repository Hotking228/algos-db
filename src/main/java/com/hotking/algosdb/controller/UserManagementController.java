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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequiredArgsConstructor
@SessionAttributes(value = {"userId", "confirmCodeError"})
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

        int mailPass = rnd.nextInt(100_000, 1_000_000);
        User user = User.builder()
                .username(registerForm.getUsername())
                .email(registerForm.getEmail())
                .password(encoder.encode(registerForm.getPassword()))
                .role(Role.USER)
                .status(Status.PENDING)
                .mailpassword(encoder.encode(Integer.toString(mailPass)))
                .build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            emailService.sendMessage(user.getEmail(), "verification code", Integer.toString(mailPass));
        });
        int id = 0;
        if(userService.isUserExists(registerForm.getUsername()) == Status.PENDING ||
                userService.isEmailExists(registerForm.getEmail()) == Status.PENDING){
            id = userService.update(userService.getByEmail(user.getEmail()).getId(), user);
        } else {
            id = userService.save(user);
        }

        model.addAttribute("userId", id);
        registerForm = null;
        return "redirect:/confirmCode";
    }

    @GetMapping("/confirmCode")
    public String showConfirmCode(Model model,
                                  @SessionAttribute("userId") Integer userId){
        return "management/confirmCode";
    }

    @PostMapping("/confirmCode")
    public String confirmCode(Model model,
                              @SessionAttribute(value = "userId", required = false) Integer userId,
                              @RequestParam("code") Integer code){

        model.addAttribute("confirmCodeError", "");
        if(userId == null){
            return "redirect:/register";
        }

        User user = userService.getById(userId).get();
        if(encoder.matches(code.toString(), user.getMailpassword())) {
            System.out.println("matches");
            user.setStatus(Status.REGISTERED);
            user.setMailpassword(null);
            userService.update(userId, user);
            return "redirect:/login";
        } else {
            System.out.println(code);
            if(!(code >= 100_000 && code < 1_000_000)){
                model.addAttribute("confirmCodeError", "Код подтверждения не совпадает!");
            }
            return "redirect:/confirmCode";
        }
    }
}
