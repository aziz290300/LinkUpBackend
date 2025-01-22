package com.example.linkup.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Academie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String adresse;
    private String nomproprietaire;
    private String telephone;
    private String email;
    private String imageURL;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL}
    )
    @JoinTable(name = "dossierImage",
            joinColumns = {
                    @JoinColumn(name = "dossierId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "imageId")
            }
    )
    public List<ImageData> logoacademie;

    @JsonIgnore
    @ManyToOne
    private User utilisateur;
    @JsonIgnore
    @OneToMany(mappedBy = "academie")
    private List<Joueur> joueurs;
    @JsonIgnore
    @ManyToMany (mappedBy = "academiesList")
    private List<Tournoi> tournois;
    @ManyToMany

    private List<match> matches;



}
