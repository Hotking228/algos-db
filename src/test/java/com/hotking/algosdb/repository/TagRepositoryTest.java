package com.hotking.algosdb.repository;

import com.hotking.algosdb.AlgosDbApplication;
import com.hotking.algosdb.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
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

    @Test
    public void shouldFindTagsByAlgoId(){
        List<Tag> tag = tagRepository.findByAlgoId(1).stream().sorted(Comparator.comparing(Tag::getName)).toList();
        assertThat(tag).hasSize(2);
        assertThat(tag.get(0).getName()).isEqualTo("рекурсия");
        assertThat(tag.get(1).getName()).isEqualTo("сортировка");
    }
}
