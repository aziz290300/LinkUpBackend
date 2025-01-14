package com.example.linkup.Controller;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.match;
import com.example.linkup.Services.Impl.MtachServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/match")

public class MatchController {
    @Autowired
    MtachServiceImpl matchService;

    @PostMapping("/add")
    public match addMatch(@RequestBody match match, @RequestParam List<Long> academieIds) {
        return matchService.addMatch(match, academieIds);
    }

    @PutMapping("/update/{id}")
    public match updateMatch(@PathVariable Integer id, @RequestBody match match) {
        return matchService.updateMatch(id, match);
    }

    @GetMapping("/{id}")
    public match getMatchById(@PathVariable Integer id) {
        return matchService.getMatchById(id);
    }

    @GetMapping("/all")
    public List<match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable Integer id) {
        matchService.deleteMatch(id);
    }
    @PutMapping("/updateScore/{matchId}")
    public match updateMatchScore(
            @PathVariable Long matchId,
            @RequestParam Long academieId,
            @RequestParam Long joueurId) {
        try {
            return matchService.updateMatchScore(matchId, academieId, joueurId);
        } catch (IllegalArgumentException e) {
            // Vous pouvez personnaliser la réponse HTTP ici
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @GetMapping("/{matchId}/academie/{academieId}")
    public Academie getAcademyById(@PathVariable Integer matchId, @PathVariable Long academieId) {
        match match = matchService.getMatchById(matchId);
        return matchService.getAcademyById(match, academieId);
    }


}
