package com.example.linkup.Services.Impl;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.Joueur;
import com.example.linkup.Entities.match;
import com.example.linkup.Repository.AcademieRepository;
import com.example.linkup.Repository.JoueurRepository;
import com.example.linkup.Repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MtachServiceImpl  {
    @Autowired
     MatchRepository matchRepository;
    @Autowired
     AcademieRepository academieRepository;
    @Autowired
    JoueurRepository joueurRepository;




    public match addMatch(match match, List<Long> academieIds) {
        // Vérifier que deux académies sont sélectionnées
        if (academieIds == null || academieIds.size() != 2) {
            throw new IllegalArgumentException("Deux académies doivent être sélectionnées.");
        }

        // Récupérer les académies
        List<Academie> academies = academieRepository.findAllByIdIn(academieIds);
        if (academies.size() != 2) {
            throw new IllegalArgumentException("Les académies sélectionnées ne sont pas valides.");
        }

        match.setAcademies(academies);
        return matchRepository.save(match);
    }


    public match updateMatch(Integer matchId, match match) {
        Optional<com.example.linkup.Entities.match> existingMatch = matchRepository.findById(matchId);
        if (existingMatch.isPresent()) {
            com.example.linkup.Entities.match updatedMatch = existingMatch.get();
            updatedMatch.setNbrmitemps(match.getNbrmitemps());
            updatedMatch.setDuree_mitemps(match.getDuree_mitemps());
            updatedMatch.setDatetime(match.getDatetime());
            updatedMatch.setScore(match.getScore());
            return matchRepository.save(updatedMatch);
        } else {
            throw new IllegalArgumentException("Match introuvable.");
        }
    }


    public match getMatchById(Integer id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Match introuvable."));
    }
    public match updateMatchScore(Long matchId, Long academieId, Long joueurId) {
        // Trouver le match
        match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match introuvable."));

        // Trouver l'académie
        Academie academie = academieRepository.findById(academieId)
                .orElseThrow(() -> new IllegalArgumentException("Académie introuvable."));

        // Vérifier si l'académie est associée au match
        if (!match.getAcademies().contains(academie)) {
            throw new IllegalArgumentException("L'académie n'est pas associée à ce match.");
        }

        // Trouver le joueur
        Joueur joueur = joueurRepository.findById(joueurId)
                .orElseThrow(() -> new IllegalArgumentException("Joueur introuvable."));

        // Vérifier si le joueur appartient à l'académie
        if (!joueur.getAcademie().equals(academie)) {
            throw new IllegalArgumentException("Le joueur n'appartient pas à cette académie.");
        }

        // Ajouter un but au joueur
        joueur.setButs(joueur.getButs() + 1);
        joueurRepository.save(joueur);

        // Mettre à jour le score du match
        String[] scoreParts = match.getScore() == null || match.getScore().isEmpty()
                ? new String[]{"0", "0"} // Initialiser le score si absent
                : match.getScore().split(" - ");

        int scoreAcademie1 = Integer.parseInt(scoreParts[0].trim());
        int scoreAcademie2 = Integer.parseInt(scoreParts[1].trim());

        if (match.getAcademies().get(0).equals(academie)) {
            scoreAcademie1++; // Incrémenter le score pour la première académie
        } else {
            scoreAcademie2++; // Incrémenter le score pour la seconde académie
        }

        // Mettre à jour et sauvegarder le score
        match.setScore(scoreAcademie1 + " - " + scoreAcademie2);
        return matchRepository.save(match);
    }

    public Academie getAcademyById(match match, Long academieId) {
        return match.getAcademies().stream()
                .filter(academie -> academie.getId().equals(academieId))
                .findFirst()
                .orElse(null);
    }




    public List<match> getAllMatches() {
        return matchRepository.findAll();
    }


    public void deleteMatch(Integer id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Match introuvable.");
        }
    }}
