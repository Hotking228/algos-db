package com.hotking.algosdb.controller;

import com.hotking.algosdb.repository.AlgoRepository;
import com.hotking.algosdb.service.AlgoService;
import jakarta.persistence.GeneratedValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/management/algo")
@RequiredArgsConstructor
public class AlgoManagementController {

    private final AlgoService algoService;

    @GetMapping
    public String showAlgos(Model model){
        model.addAttribute("algos", algoService.findAll());
        return "management/algos";
    }

    @GetMapping("/edit/{id}")
    public String editAlgo(@PathVariable("id") Integer id,
                           Model model) throws IOException {
        String algoDescription = algoService.getAlgoText(id);
        model.addAttribute("description", algoDescription);
        return "management/editAlgo";
    }

    @PostMapping("/edit/{id}")
    public String postAlgo(){
        return "";
    }
}
