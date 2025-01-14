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
public class JoueurServiceImpl {
    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    AcademieRepository academieRepository;
    @Autowired
    StorageRepository storageRepository;
    //----------------------CRUD--------------------------------------------------------------------------------------

    public Joueur addJoueur(Joueur joueur){return joueurRepository.save(joueur);}

    public Joueur addJoueurAndAffectToAcademy(Joueur joueur, Long idAcademyOwner) {
        Academie academie = academieRepository.findByUtilisateurId(idAcademyOwner);

        if (academie == null) {
            throw new RuntimeException("Academy not found for the given owner ID.");
        }

        joueur.setAcademie(academie);

        return joueurRepository.save(joueur);
    }

    public List<Joueur> displayJoueur(){ return (List<Joueur>) joueurRepository.findAll();}


    public Joueur displayJoueur(long idJoueur){return joueurRepository.findById(idJoueur).get();}

    public Joueur modifieJoueur(Joueur joueur){ return joueurRepository.save(joueur); }

    public void deleteJoueur(Joueur joueur){joueurRepository.delete(joueur);}

    public ImageData modifieImage(ImageData o){ return storageRepository.save(o); }
    public void deleteJoueurByID(long cl){joueurRepository.deleteById(cl);}

}
