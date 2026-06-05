package com.hotking.algosdb.controller;

import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.entity.Complexity;
import com.hotking.algosdb.entity.Tag;
import com.hotking.algosdb.paginator.AlgosPaginator;
import com.hotking.algosdb.paginator.PageProperties;
import com.hotking.algosdb.service.AlgoService;
import com.hotking.algosdb.service.ComplexityService;
import com.hotking.algosdb.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/management/algo")
@RequiredArgsConstructor
public class AlgoManagementController {

    private final AlgoService algoService;
    private final TagService tagService;
    private final ComplexityService compService;
    private final AlgosPaginator algosPaginator;
    private final PageProperties pageProperties;

    @GetMapping
    public String showAlgos(Model model){
        model.addAttribute("algos", algosPaginator.paginate(
                algoService.getAlgosByTagsAndComps(algosPaginator.getTags(),
                        algosPaginator.getComplexities())));
        model.addAttribute("pageNums",algosPaginator.getPageNums());
        return "management/algos";
    }

    @PostMapping("/{pageNum}")
    public String setPage(@PathVariable("pageNum") Integer pageNum){
        pageProperties.setNum(pageNum);
        return "redirect:/management/algo";
    }

    @GetMapping("/edit/{id}")
    public String editAlgo(@PathVariable("id") Integer id,
                           Model model) throws IOException {
        Algorithm algo = algoService.getById(id).get();
        String name = algo.getName();
        List<String> selectedTags = algo.getTags().stream()
                .map(Tag::getName)
                .toList();
        String selectedComp = algo.getComplexity().getComp();
        List<Complexity> comps = compService.getAll();
        List<Tag> tags = tagService.getAll();
        String algoDescription = algoService.getAlgoText(id);
        model.addAttribute("id", id);
        model.addAttribute("selectedTags", selectedTags);
        model.addAttribute("selectedComp", selectedComp);
        model.addAttribute("comps", comps);
        model.addAttribute("tags", tags);
        model.addAttribute("description", algoDescription);
        model.addAttribute("name", name);
        return "management/editAlgo";
    }

    @PostMapping("/edit/{id}")
    public String editAlgo(@PathVariable("id") Integer id,
                           @RequestParam("description") String description,
                           @RequestParam("name") String name,
                           @RequestParam("newTags") List<String> tagIds,
                           @RequestParam("newComp") String compId) throws IOException {

        //TODO: добавить исключение
        Complexity comp = compService.getByName(compId);

        //TODO: добавить исключение
        List<Tag> tags = tagIds.stream()
                .map(tagService::getByName)
                .collect(Collectors.toList());

        Algorithm algo = Algorithm.builder()
                .name(name)
                .tags(tags)
                .complexity(comp)
                .build();
        algoService.fullUpdate(id, algo, description);
        return "redirect:/management/algo";
    }

    @PostMapping("/del/{id}")
    public String deleteAlgo(@PathVariable("id") Integer id){
        algoService.delete(id);
        return "redirect:/management/algo";
    }

    @GetMapping("/add")
    public String showAddNewAlgo(Model model){
        model.addAttribute("comps", compService.getAll());
        model.addAttribute("tags", tagService.getAll());
        return "management/addNewAlgo";
    }

    @PostMapping("/add")
    public String addNewAlgo(@RequestParam("description") String description,
                             @RequestParam("name") String name,
                             @RequestParam("newTags") List<String> tagIds,
                             @RequestParam("newComp") String compId){
        //TODO: добавить исключение
        Complexity comp = compService.getByName(compId);

        //TODO: добавить исключение
        List<Tag> tags = tagIds.stream()
                .map(tagService::getByName)
                .collect(Collectors.toList());
        Algorithm algo = Algorithm.builder()
                .name(name)
                .filePath("algos/" + name + ".md")
                .tags(tags)
                .complexity(comp)
                .build();
        algoService.save(algo, description);
        return "redirect:/management/algo";
    }
}
