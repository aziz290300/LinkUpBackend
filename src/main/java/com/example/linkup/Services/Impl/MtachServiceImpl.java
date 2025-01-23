package com.example.linkup.Services.Impl;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.Joueur;
import com.example.linkup.Entities.Match;
import com.example.linkup.Repository.AcademieRepository;
import com.example.linkup.Repository.JoueurRepository;
import com.example.linkup.Repository.MatchRepository;
import com.example.linkup.Services.MatchService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MtachServiceImpl  {
    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Autowired
     MatchRepository matchRepository;
    @Autowired
     AcademieRepository academieRepository;
    @Autowired
    JoueurRepository joueurRepository;




    public Match addMatch(Match match, List<Long> academieIds) {
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


    public Match updateMatch(Integer matchId, Match match) {
        Optional<Match> existingMatch = matchRepository.findById(matchId);
        if (existingMatch.isPresent()) {
            Match updatedMatch = existingMatch.get();
            updatedMatch.setNbrmitemps(match.getNbrmitemps());
            updatedMatch.setDuree_mitemps(match.getDuree_mitemps());
            updatedMatch.setDatetime(match.getDatetime());
            updatedMatch.setScore(match.getScore());
            return matchRepository.save(updatedMatch);
        } else {
            throw new IllegalArgumentException("Match introuvable.");
        }
    }


    public Match getMatchById(Integer id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Match introuvable."));
    }
    @Transactional
    public Match updateMatchScore(Long matchId, Long academieId, Long joueurId) {
        logger.info("Mise à jour du score pour le match ID: {}, académie ID: {}, joueur ID: {}", matchId, academieId, joueurId);

        Match match = matchRepository.findById(matchId)
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

        // Vérifier si le joueur a un carton rouge
        if (match.getCarton_rouge() != null) {
            List<Long> joueursCartonRouge = Arrays.stream(match.getCarton_rouge().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            if (joueursCartonRouge.contains(joueurId)) {
                throw new IllegalArgumentException("Le joueur avec l'ID " + joueurId + " a reçu un carton rouge et est exclu du match.");
            }
        }

        // Incrémenter les buts du joueur
        joueur.setButs(joueur.getButs() + 1);
        joueurRepository.save(joueur);

        // Ajouter le joueur à la liste des buteurs du match
        if (match.getButteur() == null) {
            match.setButteur(new ArrayList<>());
        }
        match.getButteur().add(joueur.getNom()); // Ajouter le joueur dans la liste des buteurs

        // Mettre à jour le score
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

    public List<String> getMatchButteurs(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Le match avec l'ID " + matchId + " est introuvable."));

        return match.getButteur() != null ? match.getButteur() : new ArrayList<>();
    }


    public Academie getAcademyById(Match match, Long academieId) {
        return match.getAcademies().stream()
                .filter(academie -> academie.getId().equals(academieId))
                .findFirst()
                .orElse(null);
    }




    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }


    public void deleteMatch(Integer id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Match introuvable.");
        }
    }
    @Transactional
    public Match addCarton(Long matchId, Long academieId, Long joueurId, String couleurCarton) {
        logger.info("Ajout d'un carton {} pour le joueur ID: {}, académie ID: {}, match ID: {}", couleurCarton, joueurId, academieId, matchId);

        // Vérification du match
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    logger.error("Le match avec l'ID {} est introuvable.", matchId);
                    return new IllegalArgumentException("Le match avec l'ID " + matchId + " est introuvable.");
                });

        // Initialisation des champs s'ils sont null
        if (match.getCarton_rouge() == null) {
            match.setCarton_rouge("");
        }
        if (match.getCarton_jaune() == null) {
            match.setCarton_jaune("");
        }

        // Vérification de l'académie
        Academie academie = academieRepository.findById(academieId)
                .orElseThrow(() -> new IllegalArgumentException("L'académie avec l'ID " + academieId + " est introuvable."));
        if (!match.getAcademies().contains(academie)) {
            throw new IllegalArgumentException("L'académie avec l'ID " + academieId + " n'est pas associée au match.");
        }

        // Vérification du joueur
        Joueur joueur = joueurRepository.findById(joueurId)
                .orElseThrow(() -> new IllegalArgumentException("Le joueur avec l'ID " + joueurId + " est introuvable."));
        if (!joueur.getAcademie().equals(academie)) {
            throw new IllegalArgumentException("Le joueur avec l'ID " + joueurId + " n'appartient pas à l'académie " + academieId + ".");
        }

        // Vérifier si le joueur a déjà un carton rouge
        List<Long> joueursAvecCartonRouge = Arrays.stream(match.getCarton_rouge().split(","))
                .filter(id -> !id.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        if (joueursAvecCartonRouge.contains(joueurId)) {
            throw new IllegalArgumentException("Le joueur avec l'ID " + joueurId + " a déjà un carton rouge et ne peut pas recevoir d'autres cartons.");
        }

        // Vérifier si le joueur a déjà un carton jaune
        List<Long> joueursAvecCartonJaune = Arrays.stream(match.getCarton_jaune().split(","))
                .filter(id -> !id.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        if ("jaune".equalsIgnoreCase(couleurCarton)) {
            if (joueursAvecCartonJaune.contains(joueurId)) {
                // Si le joueur a déjà un carton jaune, il reçoit un carton rouge
                match.setCarton_jaune(joueursAvecCartonJaune.stream()
                        .filter(id -> !id.equals(joueurId)) // Retirer le joueur de la liste des cartons jaunes
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
                match.setCarton_rouge(match.getCarton_rouge() + (match.getCarton_rouge().isEmpty() ? "" : ",") + joueurId);
                logger.info("Le joueur ID: {} a reçu un deuxième carton jaune et a été converti en carton rouge.", joueurId);
            } else {
                // Sinon, ajouter le joueur à la liste des cartons jaunes
                match.setCarton_jaune(match.getCarton_jaune() + (match.getCarton_jaune().isEmpty() ? "" : ",") + joueurId);
            }
        } else if ("rouge".equalsIgnoreCase(couleurCarton)) {
            // Ajouter directement un carton rouge
            match.setCarton_rouge(match.getCarton_rouge() + (match.getCarton_rouge().isEmpty() ? "" : ",") + joueurId);
            logger.info("Le joueur ID: {} a reçu un carton rouge.", joueurId);
        } else {
            throw new IllegalArgumentException("Couleur du carton invalide. Utilisez 'rouge' ou 'jaune'.");
        }

        return matchRepository.save(match);
    }

    @Transactional
    public Map<String, List<String>> getNomJoueursAvecCartonsRougeEtJaune(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Le match avec l'ID " + matchId + " n'a pas été trouvé."));

        String cartonRouge = match.getCarton_rouge();
        String cartonJaune = match.getCarton_jaune();

        // Initialisation des listes pour éviter les erreurs si les champs sont vides ou nuls
        List<Long> joueursAvecCartonRouge = (cartonRouge == null || cartonRouge.isEmpty())
                ? Collections.emptyList()
                : Arrays.stream(cartonRouge.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Long> joueursAvecCartonJaune = (cartonJaune == null || cartonJaune.isEmpty())
                ? Collections.emptyList()
                : Arrays.stream(cartonJaune.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // Récupération des noms des joueurs pour chaque type de carton
        List<String> nomsCartonsRouges = joueurRepository.findAllById(joueursAvecCartonRouge)
                .stream()
                .map(Joueur::getNom)
                .collect(Collectors.toList());

        List<String> nomsCartonsJaunes = joueurRepository.findAllById(joueursAvecCartonJaune)
                .stream()
                .map(Joueur::getNom)
                .collect(Collectors.toList());

        // Construction de la réponse sous forme de Map
        Map<String, List<String>> result = new HashMap<>();
        result.put("cartonsRouges", nomsCartonsRouges);
        result.put("cartonsJaunes", nomsCartonsJaunes);

        return result;
    }

}
