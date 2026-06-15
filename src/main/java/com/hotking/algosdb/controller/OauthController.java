package com.hotking.algosdb.controller;

import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.enums.Role;
import com.hotking.algosdb.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth",
        produces = "application/json")
@CrossOrigin("http://localhost:8081")
public class OauthController {

    UserManagementController userController;

    @PostMapping(value = "/yandex", consumes = "application/json")
    public ResponseEntity<Void> getYandexToken(@RequestBody Map<String, String> request,
                                              HttpServletRequest req) throws IOException {
        authenticateUser(req);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:8081/algo/all"));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private void authenticateUser(HttpServletRequest request){
        User user = User.builder()
                .role(Role.USER)
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // Устанавливаем в SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // Сохраняем в сессии (ЭТО КЛЮЧЕВОЙ МОМЕНТ!)
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }
}
