package com.hotking.algosdb.repository;

import com.hotking.algosdb.entity.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<Tag> findByName(String name);

    @Query(value = "SELECT\n" +
            "    t.id,\n" +
            "    t.name\n" +
            "FROM tag t\n" +
            "JOIN algo_tag a_t ON t.id = a_t.tag_id\n" +
            "JOIN algorithm a ON a_t.algo_id = a.id\n" +
            "WHERE a.id = :algoId;",
    nativeQuery = true)
    List<Tag> findByAlgoId(Integer algoId);
}
