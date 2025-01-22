package com.example.linkup.Services.Impl;

import com.example.linkup.Entities.*;
import com.example.linkup.Repository.AcademieRepository;
import com.example.linkup.Repository.JoueurRepository;
import com.example.linkup.Repository.StorageRepository;
import com.example.linkup.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcademieServiceImpl {

    @Autowired
    AcademieRepository academieRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StorageRepository storageRepository;
    @Autowired
    private JoueurRepository joueurRepository;
    //----------------------CRUD--------------------------------------------------------------------------------------

    public Academie addAcademie(Academie academie,long id){
        User owner=userRepository.findById(id).get();
        academie.setUtilisateur(owner);
        return academieRepository.save(academie);
    }

    public List<Academie> displayAcademie(){ return (List<Academie>) academieRepository.findAll();}


    public Academie displayAcademie(long idAcademie){return academieRepository.findById(idAcademie).get();}

    public List<User> displayOwnersWithoutAcademie(){
        List<User> owners=userRepository.findAllByRole(Role.ROLE_RESPONSABLE);
        List<User> ownersWithoutAcademies= new ArrayList<>();
        for (User owner:owners
             ) {
            if (owner.getAcademies()==null || owner.getAcademies().isEmpty())
                ownersWithoutAcademies.add(owner);
        }
        return ownersWithoutAcademies;
    }

    public Academie modifieAcademie(Academie academie){ return academieRepository.save(academie); }

    public ImageData modifieImage(ImageData o){ return storageRepository.save(o); }

    public void deleteAcademie(Academie academie){academieRepository.delete(academie);}
    public void deleteAcademieByID(long cl){academieRepository.deleteById(cl);}

    public List<Joueur> getJoueursByAcademie(Long academieId) {
        return joueurRepository.findByAcademieId(academieId);
    }
}
