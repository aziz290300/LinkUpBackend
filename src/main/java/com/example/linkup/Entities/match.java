package com.example.linkup.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "match_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class match implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nbrmitemps;
    private int duree_mitemps;
    private LocalDateTime datetime;
    private String score;
    @ManyToMany
    @JoinTable(
            name = "match_academie", // nom de la table de jonction
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "academie_id")
    )

    private List<Academie> academies;


}
