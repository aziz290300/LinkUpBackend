package com.example.linkup.Services;

import com.example.linkup.Entities.ImageData;
import com.example.linkup.Repository.AcademieRepository;
import com.example.linkup.Repository.StorageRepository;
import com.example.linkup.Utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    private StorageRepository repository;
    @Autowired
    AcademieRepository academieRepository;
    public ImageData uploadImage2(MultipartFile file) throws IOException {
        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {

            return imageData;
        }
        return null;
    }

    public byte[] downloadImage(String fileName){
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }
    //    public byte[] downloadImage2(long id){
//        Optional<ImageData> dbImageData = repository.findim(id);
//        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
//        return images;
//    }
    public List<ImageData> displayAcademies(){ return (List<ImageData>) repository.findAll();}

//    public List<ImageData> displayImages(){ return (List<ImageData>) repository.findAll();}

}
