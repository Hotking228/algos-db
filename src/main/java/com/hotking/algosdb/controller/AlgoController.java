package com.hotking.algosdb.controller;

import com.hotking.algosdb.enums.TagOperator;
import com.hotking.algosdb.paginator.AlgosPaginator;
import com.hotking.algosdb.service.AlgoService;
import com.hotking.algosdb.service.ComplexityService;
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
    private final ComplexityService compService;
    private final AlgosPaginator algosPaginator;

    @GetMapping("/test")
    public String testController(Model model){
        return "index";
    }

    @GetMapping("/all")
    public String getAllAlgos(Model model){
        model.addAttribute("algos", algoService.getAlgosByTagsAndComps(algosPaginator.getTags(),
                algosPaginator.getComplexities()));
        model.addAttribute("tags", tagService.getAll());
        model.addAttribute("operators", TagOperator.values());
        model.addAttribute("chosenTags", algosPaginator.getTags());
        model.addAttribute("chosenOperator", algosPaginator.getTagOperator());

        model.addAttribute("complexities", compService.getAll());
        model.addAttribute("chosenComplexities", algosPaginator.getComplexities());
        return "algo/algos";
    }

    @PostMapping("/all")
    public String getAllAlgos(Model model,
                              @RequestParam(value = "selectedTags", required = false) List<String> tags,
                              @RequestParam("operator") String operator,
                              @RequestParam(value = "selectedComps", required = false) List<String> comps){
        if(tags != null)algosPaginator.setTags(tags);
        else algosPaginator.setTags(List.of());
        if(comps != null)algosPaginator.setComplexities(comps);
        else algosPaginator.setComplexities(List.of());
        algosPaginator.setTagOperator(operator);
        return "redirect:/algo/all";
    }

    @GetMapping("/{id}")
    public String getAlgo(Model model,
                          @PathVariable("id") Integer id){
        model.addAttribute("content", algoService.getByIdMd(id));
        return "algo/algo";
    }
}
