package com.hotking.algosdb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"algos"})
@ToString(exclude = {"algos"})
public class Complexity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comp;

    @OneToMany(mappedBy = "complexity", fetch = FetchType.LAZY)
    private List<Algorithm> algos;
}
