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
public class Tournoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private String nom;
    private int nbrpoule;
    @Enumerated(EnumType.STRING)
    private type type ;
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
    public List<ImageData> logotournoi;
    @ManyToMany
    private List<Academie> academiesList;
    @OneToMany (mappedBy = "tournoi")
    private List<Match> matches;


}
