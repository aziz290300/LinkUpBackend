package com.example.linkup.Services.Impl;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.ImageData;
import com.example.linkup.Entities.Joueur;
import com.example.linkup.Repository.AcademieRepository;
import com.example.linkup.Repository.JoueurRepository;
import com.example.linkup.Repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademieServiceImpl {

    @Autowired
    AcademieRepository academieRepository;
    @Autowired
    StorageRepository storageRepository;
    @Autowired
    private JoueurRepository joueurRepository;
    //----------------------CRUD--------------------------------------------------------------------------------------

    public Academie addAcademie(Academie academie){return academieRepository.save(academie);}

    public List<Academie> displayAcademie(){ return (List<Academie>) academieRepository.findAll();}


    public Academie displayAcademie(long idAcademie){return academieRepository.findById(idAcademie).get();}

    public Academie modifieAcademie(Academie academie){ return academieRepository.save(academie); }

    public ImageData modifieImage(ImageData o){ return storageRepository.save(o); }

    public void deleteAcademie(Academie academie){academieRepository.delete(academie);}
    public void deleteAcademieByID(long cl){academieRepository.deleteById(cl);}

    public List<Joueur> getJoueursByAcademie(Long academieId) {
        return joueurRepository.findByAcademieId(academieId);
    }
}
