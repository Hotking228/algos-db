package com.hotking.algosdb.repository;

import com.hotking.algosdb.AlgosDbApplication;
import com.hotking.algosdb.entity.Complexity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@SpringBootTest(classes = AlgosDbApplication.class)
public class ComplexityRepositoryTest {
    @Autowired
    public ComplexityRepository complexityRepository;

    @Test
    public void shouldReturnComplexityByName(){
        List<Complexity> complexities = complexityRepository.findByComp("O(n log n)");
        assertThat(complexities).hasSize(1);
        assertThat(complexities.get(0).getComp()).isEqualTo("O(n log n)");
    }
}
