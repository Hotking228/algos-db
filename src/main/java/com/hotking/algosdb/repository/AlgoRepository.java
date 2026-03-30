package com.hotking.algosdb.repository;

import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.entity.Complexity;
import com.hotking.algosdb.entity.Tag;
import jakarta.persistence.NamedEntityGraph;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlgoRepository extends JpaRepository<Algorithm, Integer> {

    public List<Algorithm> findAlgorithmsByName(String name);

    @Query("select distinct a " +
            "from Algorithm a " +
            "join fetch a.tags t " +
            "join fetch a.complexity c " +
            "where t.name in (:tags) and c.comp in (:complexities)")
    public List<Algorithm> findAlgorithmsByTagsAndComps(List<String> tags, List<String> complexities);
}
