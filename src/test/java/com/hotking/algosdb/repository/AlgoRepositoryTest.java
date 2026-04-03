package com.hotking.algosdb.repository;

import com.hotking.algosdb.AlgosDbApplication;
import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.entity.Complexity;
import com.hotking.algosdb.entity.Tag;
import com.hotking.algosdb.paginator.AlgosPaginator;
import com.hotking.algosdb.service.AlgoService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(classes = AlgosDbApplication.class)
@RequiredArgsConstructor
public class AlgoRepositoryTest {

    @Autowired
    public AlgoService algoService;
    @Autowired
    public TagRepository tagRepository;
    @Autowired
    public ComplexityRepository complexityRepository;
    @Autowired
    public AlgosPaginator algosPaginator;

    @Test
    public void shouldReturnBasicAlgorithmsByTagsAndComplexities(){
        List<Tag> tags = tagRepository.findByName("сортировка");
        List<Complexity> complexities = complexityRepository.findByComp("O(n log n)");
        algosPaginator.setTags(tags.stream().map(Tag::getName).toList());
        List<Algorithm> algorithms = algoService.getAlgosByTagsAndComps(tags.stream().map(Tag::getName).toList(), complexities.stream().map(Complexity::getComp).toList());
        assertThat(algorithms).hasSize(3);
        assertThat(algorithms.stream().map(Algorithm::getId).sorted().toList()).isEqualTo(List.of(1,
                6,
                13));
    }
}
