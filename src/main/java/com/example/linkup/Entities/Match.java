package com.example.linkup.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "match_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nbrmitemps;
    private int duree_mitemps;
    private LocalDateTime datetime;
    private String score;
    private String carton_rouge = ""; // Initialise avec une chaîne vide
    private String carton_jaune = ""; // Initialise avec une chaîne vide



    @ManyToMany
    @JoinTable(
            name = "match_academie", // nom de la table de jonction
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "academie_id")
    )

    private List<Academie> academies;

    @ElementCollection
    private List<String> butteur;
    @ManyToOne
    private Tournoi tournoi;
}
