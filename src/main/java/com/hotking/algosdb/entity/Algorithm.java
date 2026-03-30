package com.hotking.algosdb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"tags", "complexity"})
@ToString(exclude = {"tags", "complexity"})
@NamedEntityGraph(name = "withAllDependencies",
                    attributeNodes = {@NamedAttributeNode("tags"),
                                      @NamedAttributeNode("complexity")})
public class Algorithm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(nullable = false, unique = true)
    private String filePath;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "algo_id"),
        name = "algo_tag",
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complexity_id")
    private Complexity complexity;
}
