package com.example.linkup.Services.Impl;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.Joueur;
import com.example.linkup.Entities.match;
import com.example.linkup.Repository.AcademieRepository;
import com.example.linkup.Repository.JoueurRepository;
import com.example.linkup.Repository.MatchRepository;
import com.example.linkup.Services.MatchService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class MtachServiceImpl  {
    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

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
    @Transactional
    public match updateMatchScore(Long matchId, Long academieId, Long joueurId) {
        logger.info("Mise à jour du score pour le match ID: {}, académie ID: {}, joueur ID: {}", matchId, academieId, joueurId);

        match match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    logger.error("Le match avec l'ID {} est introuvable.", matchId);
                    return new IllegalArgumentException("Le match avec l'ID " + matchId + " est introuvable.");
                });

        logger.info("Match trouvé : {}", match);

        Academie academie = academieRepository.findById(academieId)
                .orElseThrow(() -> new IllegalArgumentException("L'académie avec l'ID " + academieId + " est introuvable."));
        if (!match.getAcademies().contains(academie)) {
            throw new IllegalArgumentException("L'académie avec l'ID " + academieId + " n'est pas associée au match.");
        }

        Joueur joueur = joueurRepository.findById(joueurId)
                .orElseThrow(() -> new IllegalArgumentException("Le joueur avec l'ID " + joueurId + " est introuvable."));
        if (!joueur.getAcademie().equals(academie)) {
            throw new IllegalArgumentException("Le joueur avec l'ID " + joueurId + " n'appartient pas à l'académie " + academieId + ".");
        }

        joueur.setButs(joueur.getButs() + 1);
        joueurRepository.save(joueur);

        String[] scoreParts = parseScore(match.getScore());

        int scoreAcademie1 = Integer.parseInt(scoreParts[0]);
        int scoreAcademie2 = Integer.parseInt(scoreParts[1]);

        int academieIndex = match.getAcademies().indexOf(academie);
        if (academieIndex == 0) {
            scoreAcademie1++;
        } else if (academieIndex == 1) {
            scoreAcademie2++;
        } else {
            throw new IllegalArgumentException("L'académie n'est pas reconnue dans ce match.");
        }

        match.setScore(scoreAcademie1 + "-" + scoreAcademie2);
        return matchRepository.save(match);
    }


    private String[] parseScore(String score) {
        if (score == null || score.isEmpty()) {
            throw new IllegalArgumentException("Le score ne peut pas être nul ou vide.");
        }
        // Supprimer les espaces autour du tiret
        String normalizedScore = score.replace(" - ", "-").replace(" ", "");
        if (!normalizedScore.matches("\\d+-\\d+")) {
            throw new IllegalArgumentException("Format de score invalide : " + score);
        }
        return normalizedScore.split("-");
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
