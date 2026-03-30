package com.hotking.algosdb.repository;

import com.hotking.algosdb.AlgosDbApplication;
import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.entity.Complexity;
import lombok.AllArgsConstructor;
import com.hotking.algosdb.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

@SpringBootTest(classes = AlgosDbApplication.class)
@RequiredArgsConstructor
public class AlgoRepositoryTest {

    @Autowired
    public AlgoRepository algoRepository;
    @Autowired
    public TagRepository tagRepository;
    @Autowired
    public ComplexityRepository complexityRepository;

    @Test
    public void shouldReturnBasicAlgorithmsByTagsAndComplexities(){
        List<Tag> tags = tagRepository.findByName("сортировка");
        List<Complexity> complexities = complexityRepository.findByComp("O(n log n)");
        List<Algorithm> algorithms = algoRepository.findAlgorithmsByTagsAndComps(tags.stream().map(Tag::getName).toList(), complexities.stream().map(Complexity::getComp).toList());
        assertThat(algorithms).hasSize(3);
        assertThat(algorithms.stream().map(Algorithm::getId).sorted().toList()).isEqualTo(List.of(1,
                6,
                13));
    }
}
