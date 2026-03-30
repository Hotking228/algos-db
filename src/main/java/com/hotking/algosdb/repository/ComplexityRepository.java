package com.hotking.algosdb.repository;

import com.hotking.algosdb.entity.Complexity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplexityRepository extends JpaRepository<Complexity, Integer> {

    List<Complexity> findByComp(String comp);
}
