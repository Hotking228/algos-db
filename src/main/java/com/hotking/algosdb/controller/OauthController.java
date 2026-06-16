package com.hotking.algosdb.controller;

import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.enums.Role;
import com.hotking.algosdb.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth",
        produces = "application/json")
@CrossOrigin("https://localhost:8081")
public class OauthController {

    UserManagementController userController;
    private final RestTemplate restTemplate;

    @PostMapping(value = "/yandex", consumes = "application/json")
    public ResponseEntity<Void> getYandexToken(@RequestBody JsonNode request,
                                              HttpServletRequest req) throws IOException {
        authenticateUser(req);

        String url = "https://login.yandex.ru/info?format=jwt";
        HttpHeaders yaHeaders = new HttpHeaders();
        yaHeaders.set("Authorization", "OAuth " + request.get("token").asString());
        HttpEntity<?> entity = new HttpEntity<>(yaHeaders);

        ResponseEntity<String> yandexResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        String jwt = yandexResponse.getBody();

        System.out.println(jwt);

        System.out.println(parseJwt(jwt).toPrettyString());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("https://localhost:8081/algo/all"));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private JsonNode parseJwt(String jwt){
        String[]parts = jwt.split("\\.");
        String payloadJson = new String(Base64.getDecoder().decode(parts[1]));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(payloadJson);
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
