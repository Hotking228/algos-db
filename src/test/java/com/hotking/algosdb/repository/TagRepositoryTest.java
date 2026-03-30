package com.hotking.algosdb.repository;

import com.hotking.algosdb.AlgosDbApplication;
import com.hotking.algosdb.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@SpringBootTest(classes = AlgosDbApplication.class)
public class TagRepositoryTest {
    @Autowired
    public TagRepository tagRepository;

    @Test
    public void shouldReturnTagByName(){
        List<Tag> tags = tagRepository.findByName("сортировка");
        assertThat(tags).hasSize(1);
        assertThat(tags.get(0).getName()).isEqualTo("сортировка");
    }

}
