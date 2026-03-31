package com.hotking.algosdb.controller;

import com.hotking.algosdb.service.AlgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/algo")
@RequiredArgsConstructor
public class AlgoController {

    private final AlgoService algoService;

    @GetMapping("/all")
    public String getAllAlgos(Model model){
        model.addAttribute("algos", algoService.getAlgosByTagsAndComps(List.of(), List.of()));
        return "/algo/algos";
    }

    @GetMapping("/{id}")
    public String getAlgo(Model model,
                          @PathVariable("id") Integer id){
        model.addAttribute("content", algoService.getById(id));
        return "/algo/algo";
    }
}
