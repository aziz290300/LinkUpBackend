package com.example.linkup.Controller;

import com.example.linkup.Entities.ImageData;
import com.example.linkup.Services.Impl.AcademieServiceImpl;
import com.example.linkup.Services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private StorageService service;

    @Autowired
    private AcademieServiceImpl academieService;

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadImage(@RequestParam long idTool,@RequestPart("image") MultipartFile file) throws IOException, IOException {
//        String uploadImage = service.uploadImage(idTool, file);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
//    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData=service.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
    @GetMapping("/getall")
    public List<ImageData> displayToolOffers(){ return (List<ImageData>) service.displayAcademies();}
//    @GetMapping("/displayImages")
//    public List<ImageData> displayImages(){ return (List<ImageData>) service.displayImages();}

}
