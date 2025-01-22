package com.example.linkup.Controller;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.Match;
import com.example.linkup.Services.Impl.MtachServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/match")

public class MatchController {
    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);
    @Autowired
    MtachServiceImpl matchService;

    @PostMapping("/add")
    public Match addMatch(@RequestBody Match match, @RequestParam List<Long> academieIds) {
        return matchService.addMatch(match, academieIds);
    }

    @PutMapping("/update/{id}")
    public Match updateMatch(@PathVariable Integer id, @RequestBody Match match) {
        return matchService.updateMatch(id, match);
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable Integer id) {
        return matchService.getMatchById(id);
    }

    @GetMapping("/all")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable Integer id) {
        matchService.deleteMatch(id);
    }


    @PutMapping("/updateScore/{matchId}")
    public ResponseEntity<Match> updateMatchScore(
            @PathVariable Long matchId,
            @RequestParam Long academieId,
            @RequestParam Long joueurId) {
        try {
            logger.info("Mise à jour du score pour matchId : {}, academieId : {}, joueurId : {}",
                    matchId, academieId, joueurId);

            Match updatedMatch = matchService.updateMatchScore(matchId, academieId, joueurId);
            logger.info("Score mis à jour avec succès : {}", updatedMatch.getScore());
            return ResponseEntity.ok(updatedMatch);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la mise à jour du score : {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la mise à jour du score : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{matchId}/academie/{academieId}")
    public Academie getAcademyById(@PathVariable Integer matchId, @PathVariable Long academieId) {
        Match match = matchService.getMatchById(matchId);
        return matchService.getAcademyById(match, academieId);
    }

    @PutMapping("/addCarton/{matchId}")
    public ResponseEntity<Match> addCarton(
            @PathVariable Long matchId,
            @RequestParam Long academieId,
            @RequestParam Long joueurId,
            @RequestParam String couleurCarton) {
        try {
            logger.info("Ajout d'un carton {} pour matchId : {}, academieId : {}, joueurId : {}",
                    couleurCarton, matchId, academieId, joueurId);

            Match updatedMatch = matchService.addCarton(matchId, academieId, joueurId, couleurCarton);
            logger.info("Carton ajouté avec succès.");
            return ResponseEntity.ok(updatedMatch);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de l'ajout du carton : {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'ajout du carton : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
