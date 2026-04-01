package com.hotking.algosdb.controller;

import com.hotking.algosdb.enums.TagOperator;
import com.hotking.algosdb.paginator.AlgosPaginator;
import com.hotking.algosdb.service.AlgoService;
import com.hotking.algosdb.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/algo")
@RequiredArgsConstructor
public class AlgoController {

    private final AlgoService algoService;
    private final TagService tagService;
    private final AlgosPaginator algosPaginator;

    @GetMapping("/all")
    public String getAllAlgos(Model model){
        model.addAttribute("algos", algoService.getAlgosByTagsAndComps(algosPaginator.getTags(),
                algosPaginator.getComplexities()));
        model.addAttribute("tags", tagService.getAll());
        model.addAttribute("operators", TagOperator.values());
        model.addAttribute("chosenTags", algosPaginator.getTags());
        model.addAttribute("chosenOperator", algosPaginator.getTagOperator());
        return "/algo/algos";
    }

    @PostMapping("/all")
    public String getAllAlgos(Model model,
                              @RequestParam(value = "selectedTags", required = false) List<String> tags,
                              @RequestParam("operator") String operator){
        if(tags != null)algosPaginator.setTags(tags);
        else algosPaginator.setTags(List.of());
        algosPaginator.setTagOperator(operator);
        return "redirect:/algo/all";
    }

    @GetMapping("/{id}")
    public String getAlgo(Model model,
                          @PathVariable("id") Integer id){
        model.addAttribute("content", algoService.getById(id));
        return "/algo/algo";
    }
}
