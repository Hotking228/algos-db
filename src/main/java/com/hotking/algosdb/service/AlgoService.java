package com.hotking.algosdb.service;

import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.repository.AlgoRepository;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

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

    public Optional<Algorithm> getById(Integer id){
        return algoRepository.findById(id);
    }

    public String getByIdMd(Integer id) {
        //TODO: добавить исключение
        Algorithm algo = algoRepository.findById(id).orElseThrow();
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader((new ClassPathResource(algo.getFilePath())).getInputStream()))) {

            reader.lines().forEach(line -> {sb.append(line); sb.append("\n");});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public List<Algorithm> findAll(){
        return algoRepository.findAll();
    }

    public String getAlgoText(Integer id) throws IOException {
        //TODO: добавить исключение
        String filePath = algoRepository.findById(id).orElseThrow().getFilePath();
        File file = Path.of("src", "main", "resources", filePath).toFile();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        reader.lines()
                .forEach(s -> {
                    sb.append(s);
                    sb.append("\n");
                });
        return sb.toString();
    }

    public void fullUpdate(Integer id, Algorithm algo, String description) throws IOException {
        String filePath = getByName(algo.getName()).getFilePath();
        algo.setFilePath(filePath);
        update(id, algo);
        File file = Path.of("src", "main", "resources", algo.getFilePath()).toFile();
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(description);
    }

    public Algorithm getByName(String name){
        return algoRepository.findAlgorithmsByName(name).get(0);
    }
}
