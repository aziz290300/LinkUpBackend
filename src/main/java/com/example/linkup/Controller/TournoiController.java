package com.example.linkup.Controller;

import com.example.linkup.Entities.Tournoi;
import com.example.linkup.Services.Impl.TournoiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/tournoi")
public class TournoiController {
    @Autowired
    TournoiServiceImpl tournoiService;

    @PostMapping("/addTournoi")
    public Tournoi addTournoi(@RequestBody Tournoi ct){return tournoiService.addTournoi(ct);}

    @GetMapping("/displayTournoi")
    public List<Tournoi> displayTournoi(){ return (List<Tournoi>) tournoiService.displayTournoi();}

    @GetMapping("/displayTournoiByID/{idTournoi}")
    public Tournoi displayTournoi(@PathVariable("idTournoi") long idTournoi){return tournoiService.displayTournoi((int)idTournoi);}


    @PutMapping("/updateTournoi")
    public Tournoi modifieTournoi(@RequestBody Tournoi ct){ return tournoiService.modifieTournoi(ct); }


    @DeleteMapping("/deleteTournoi")
    public void deleteTournoi(@RequestBody Tournoi ct){tournoiService.deleteTournoi(ct);}
}
