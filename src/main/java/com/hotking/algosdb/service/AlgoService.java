package com.hotking.algosdb.service;

import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.repository.AlgoRepository;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlgoService {

    private final AlgoRepository algoRepository;

    public Integer create(Algorithm algorithm){
        return algoRepository.saveAndFlush(algorithm).getId();
    }

    public List<Algorithm> getAlgosByTagsAndComps(List<String> tags, List<String> complexities){
        return algoRepository.findAlgorithmsByComps(complexities);
    }

    public String getById(Integer id) {
        //TODO: добавить исключение
        Algorithm algo = algoRepository.findById(id).orElseThrow();
        BufferedReader reader = null;
        try {
             reader = new BufferedReader(new FileReader(algo.getFilePath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder();
        reader.lines().forEach(line -> {sb.append(line); sb.append("\n");});

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        Node document = parser.parse(sb.toString());

        return renderer.render(document);
    }

    public Integer update(Integer id, Algorithm algorithm){
        //TODO: добавить исключение
        Algorithm algo = algoRepository.findById(id)
                .orElseThrow();
        algo.setComplexity(algorithm.getComplexity());
        algo.setTags(algorithm.getTags());
        algo.setName(algorithm.getName());
        algo.setFilePath(algorithm.getFilePath());
        algoRepository.saveAndFlush(algo);
        return algo.getId();
    }

    //Если возвращает -1 => что-то не так(сущность не удалилась)
    public Integer delete(Integer id){
        return algoRepository.findById(id)
                .map(algo -> {
                    algoRepository.delete(algo);
                    algoRepository.flush();
                    return id;
                })
                .orElse(-1);
    }
}
