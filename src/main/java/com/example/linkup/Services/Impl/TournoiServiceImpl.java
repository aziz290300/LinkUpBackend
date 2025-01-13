package com.example.linkup.Services.Impl;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.ImageData;
import com.example.linkup.Entities.Tournoi;
import com.example.linkup.Repository.AcademieRepository;
import com.example.linkup.Repository.StorageRepository;
import com.example.linkup.Repository.TournoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournoiServiceImpl {
    @Autowired
    TournoiRepository tournoiRepository;
    @Autowired
    AcademieRepository academieRepository;
    @Autowired
    StorageRepository storageRepository;
    //----------------------CRUD--------------------------------------------------------------------------------------

    public Tournoi addTournoi(Tournoi tournoi){return tournoiRepository.save(tournoi);}
    public Tournoi addTournoiAndAffectAcademy(Tournoi tournoi,List<Long> idAcademy){
        List<Academie> academies = academieRepository.findAllById(idAcademy); // Fetch academies by IDs
        tournoi.setAcademiesList(academies); // Set the relationship
        return tournoiRepository.save(tournoi);
    }

    public List<Tournoi> displayTournoi(){ return (List<Tournoi>) tournoiRepository.findAll();}


    public Tournoi displayTournoi(long idTournoi){return tournoiRepository.findById(idTournoi).get();}

    public Tournoi modifieTournoi(Tournoi tournoi){ return tournoiRepository.save(tournoi); }

    public void deleteTournoi(Tournoi tournoi){tournoiRepository.delete(tournoi);}

    public ImageData modifieImage(ImageData o){ return storageRepository.save(o); }
    public void deleteTournoiByID(long cl){tournoiRepository.deleteById(cl);}

}
