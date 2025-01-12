package com.example.linkup.Services.Impl;

import com.example.linkup.Entities.Tournoi;
import com.example.linkup.Repository.TournoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournoiServiceImpl {
    @Autowired
    TournoiRepository tournoiRepository;


    //----------------------CRUD--------------------------------------------------------------------------------------

    public Tournoi addTournoi(Tournoi tournoi){return tournoiRepository.save(tournoi);}

    public List<Tournoi> displayTournoi(){ return (List<Tournoi>) tournoiRepository.findAll();}


    public Tournoi displayTournoi(long idTournoi){return tournoiRepository.findById(idTournoi).get();}

    public Tournoi modifieTournoi(Tournoi tournoi){ return tournoiRepository.save(tournoi); }

    public void deleteTournoi(Tournoi tournoi){tournoiRepository.delete(tournoi);}

}
