package com.example.linkup.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Joueur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate datenaissance;
    private String telephone;
    private String categorie;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL}
    )
    @JoinTable(name = "dossierImageJoueur",
            joinColumns = {
                    @JoinColumn(name = "dossierImageJoueurId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "imageId")
            }
    )
    public List<ImageData> photoJoueur;
    @ManyToOne
    private Academie academie;



}