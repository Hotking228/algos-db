package com.hotking.algosdb.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"algorithms"})
@ToString(exclude = {"algorithms"})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "tag_id"),
            name = "algo_tag",
            inverseJoinColumns = @JoinColumn(name = "algo_id"))
    private List<Algorithm> algorithms;
}
