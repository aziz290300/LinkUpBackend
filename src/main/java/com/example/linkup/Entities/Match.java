package com.example.linkup.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match implements Serializable {
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
