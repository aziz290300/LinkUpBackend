package com.example.linkup.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tournoi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String lieu;

    private int nbrpoule;
    @Enumerated(EnumType.STRING)
    private Type type ;
    private String imageURL;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL}
    )
    public List<ImageData> logotournoi;
    @ManyToMany
    private List<Academie> academiesList;
    @OneToMany (mappedBy = "tournoi")
    private List<Match> matches;

    @Override
    public String toString() {
        return "Tournoi{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieu='" + lieu + '\'' +
                ", nbrpoule=" + nbrpoule +
                ", type=" + type +
                ", imageURL='" + imageURL + '\'' +
                ", logotournoi=" + logotournoi +
                ", academiesList=" + academiesList +
                ", matches=" + matches +
                '}';
    }
}
