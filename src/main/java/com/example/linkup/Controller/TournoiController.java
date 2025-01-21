package com.example.linkup.Controller;

import com.example.linkup.Entities.ImageData;
import com.example.linkup.Entities.Tournoi;
import com.example.linkup.Entities.Type;
import com.example.linkup.Services.Impl.TournoiServiceImpl;
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
@RequestMapping("tournoi")
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

    @DeleteMapping("/deleteTournoiByID/{idTournoi}")
    public void deleteTournoiByID(@PathVariable("idTournoi") long idTournoi){tournoiService.deleteTournoiByID(idTournoi);}

    //------------------------------Add with image----------------------------------------------------------
    @PutMapping(value = {"/updateNewTournoi"},produces = {"text/plain","application/json"}, consumes= {"multipart/mixed", MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<String> updateTournoi(@RequestPart("tournoi") @Valid Tournoi tournoi,
                                               @RequestPart("file") MultipartFile[] file){
        try{
            List<ImageData> images = uploadImage(file);
            tournoi.setLogotournoi(images);
            tournoiService.modifieTournoi(tournoi);
            return ResponseEntity.ok("File and JSON data received");
        } catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @PostMapping(value = {"/addNewTournoi"},produces = {"text/plain","application/json"}, consumes= {"multipart/mixed", MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<String> addNewTournoi(@RequestPart("tournoi") @Valid Tournoi tournoi,
                                               @RequestPart("file") MultipartFile[] file,
                                                @RequestParam("academyIds") List<Long> academyIds){

        try{
            Type type = Type.valueOf(tournoi.getType().name());
            tournoi.setType(type);

            List<ImageData> images = uploadImage(file);
            tournoi.setLogotournoi(images);
            tournoiService.addTournoiAndAffectAcademy(tournoi, academyIds);

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
