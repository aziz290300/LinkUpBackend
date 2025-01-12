package com.example.linkup.Controller;

import com.example.linkup.Entities.Academie;
import com.example.linkup.Entities.ImageData;
import com.example.linkup.Entities.Joueur;
import com.example.linkup.Services.Impl.JoueurServiceImpl;
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
@RequestMapping("joueur")
public class JoueurController {
    @Autowired
    JoueurServiceImpl joueurService;

    @PostMapping("/addJoueur")
    public Joueur addJoueur(@RequestBody Joueur ct){return joueurService.addJoueur(ct);}

    @GetMapping("/displayJoueur")
    public List<Joueur> displayJoueur(){ return (List<Joueur>) joueurService.displayJoueur();}

    @GetMapping("/displayJoueurByID/{idJoueur}")
    public Joueur displayJoueur(@PathVariable("idJoueur") long idJoueur){return joueurService.displayJoueur((int)idJoueur);}


    @PutMapping("/updateJoueur")
    public Joueur modifieJoueur(@RequestBody Joueur ct){ return joueurService.modifieJoueur(ct); }


    @DeleteMapping("/deleteJoueur")
    public void deleteJoueur(@RequestBody Joueur ct){joueurService.deleteJoueur(ct);}

    @PutMapping("/updateImgs")
    public ImageData modifieimages(@RequestBody ImageData o){ return joueurService.modifieImage(o); }

    @DeleteMapping("/deleteJoueurByID/{idJoueur}")
    public void deleteJoueurByID(@PathVariable("idJoueur") long idJoueur){joueurService.deleteJoueurByID(idJoueur);}

    //------------------------------Add with image----------------------------------------------------------
    @PutMapping(value = {"/updateNewJoueur"},produces = {"text/plain","application/json"}, consumes= {"multipart/mixed", MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<String> updateJoueur(@RequestPart("joueur") @Valid Joueur joueur,
                                                 @RequestPart("file") MultipartFile[] file){
        try{
            List<ImageData> images = uploadImage(file);
            joueur.setPhotoJoueur(images);
            joueurService.modifieJoueur(joueur);
            return ResponseEntity.ok("File and JSON data received");
        } catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @PostMapping(value = {"/addNewJoueur"},produces = {"text/plain","application/json"}, consumes= {"multipart/mixed", MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<String> addNewJoueur(@RequestPart("joueur") @Valid Joueur joueur,
                                                 @RequestPart("file") MultipartFile[] file){
        try{
            List<ImageData> images = uploadImage(file);
            joueur.setPhotoJoueur(images);
            joueurService.addJoueur(joueur);
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
}
