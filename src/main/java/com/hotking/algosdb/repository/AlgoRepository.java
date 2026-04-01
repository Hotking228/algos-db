package com.hotking.algosdb.repository;

import com.hotking.algosdb.entity.Algorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlgoRepository extends JpaRepository<Algorithm, Integer> {

    public List<Algorithm> findAlgorithmsByName(String name);

    @Query("select distinct a " +
            "from Algorithm a " +
            "join fetch a.tags t " +
            "join fetch a.complexity c " +
            "where " +
            "(c.comp in (:complexities) or :#{#complexities.isEmpty()} = true) " +
            "order by a.id")
    public List<Algorithm> findAlgorithmsByComps(List<String> complexities);
}
