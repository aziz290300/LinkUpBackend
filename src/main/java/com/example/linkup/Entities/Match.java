package com.example.linkup.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private int nbrmitemps;
    private String duree;
    private String score;
    @ElementCollection
    private List<String> butteur;
    @ManyToOne
    private Tournoi tournoi;
}
