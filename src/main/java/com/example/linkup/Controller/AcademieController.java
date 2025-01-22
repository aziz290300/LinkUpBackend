package com.example.linkup.Controller;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.ImageData;
import com.example.linkup.Entities.Joueur;
import com.example.linkup.Entities.User;
import com.example.linkup.Services.Impl.AcademieServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("academie")
public class AcademieController {
    @Autowired
    AcademieServiceImpl academieService;



    @GetMapping("/displayAcademie")
    public List<Academie> displayAcademie(){ return (List<Academie>) academieService.displayAcademie();}

    @GetMapping("/displayOwnersWithoutAcademie")
    public List<User> displayOwnersWithoutAcademie(){ return (List<User>) academieService.displayOwnersWithoutAcademie();}

    @GetMapping("/displayAcademieByID/{idAcademie}")
    public Academie displayAcademie(@PathVariable("idAcademie") long idAcademie){return academieService.displayAcademie((int)idAcademie);}


    @PutMapping("/updateAcademie")
    public Academie modifieAcademie(@RequestBody Academie ct){ return academieService.modifieAcademie(ct); }


    @DeleteMapping("/deleteAcademie")
    public void deleteAcademie(@RequestBody Academie ct){academieService.deleteAcademie(ct);}
    @DeleteMapping("/deleteAcademieByID/{idAcademy}")
    public void deleteAcademieByID(@PathVariable("idAcademy") long idAcademy){academieService.deleteAcademieByID(idAcademy);}


    @PutMapping("/updateImgs")
    public ImageData modifieimages(@RequestBody ImageData o){ return academieService.modifieImage(o); }

    //------------------------------Add with image----------------------------------------------------------
    @PutMapping(value = {"/updateNewAcademie"},produces = {"text/plain","application/json"}, consumes= {"multipart/mixed", MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<String> updateAcademie(@RequestPart("academie") @Valid Academie academie,
                                               @RequestPart("file") MultipartFile[] file){
        try{
            List<ImageData> images = uploadImage(file);
            academie.setLogoacademie(images);
            academieService.modifieAcademie(academie);
            return ResponseEntity.ok("File and JSON data received");
        } catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @PostMapping(value = {"/addNewAcademie"},produces = {"text/plain","application/json"}, consumes= {"multipart/mixed", MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<String> addNewAcademie(@RequestPart("academie") @Valid Academie academie,
                                               @RequestPart("file") MultipartFile[] file,
                                                 @RequestParam("idAcademyOwner") Long idAcademyOwner){
        try{
            List<ImageData> images = uploadImage(file);
            academie.setLogoacademie(images);
            academie.setTournois(null);
            academieService.addAcademie(academie,idAcademyOwner);
            return ResponseEntity.ok("File and JSON data received");
        } catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ImageData> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        List<ImageData> imageDatas = new ArrayList<>();
        for (MultipartFile file: multipartFiles) {
            ImageData imageData = new ImageData(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageDatas.add(imageData);
        }


        return imageDatas;
    }
    @GetMapping("/{idAcademie}/joueurs")
    public List<Joueur> getJoueursByAcademie(@PathVariable Long idAcademie) {
        return academieService.getJoueursByAcademie(idAcademie);
    }
}
