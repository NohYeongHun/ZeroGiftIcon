package com.example.demo.product.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.dto.ImageUploadResponse;
import com.example.demo.product.entity.ProductImage;
import com.example.demo.product.repository.ProductImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    private static final String UPLOAD_PATH = "/opt/zerogift/upload/";

    public List<ImageUploadResponse> upload(MultipartFile[] request) {
        createDirIfNotExist();
        List<ImageUploadResponse> results = new ArrayList<>();
        int successCount = 0;
        for (MultipartFile file : request) {
            String originalFileName = null, uuid = null, ext = null;
            byte[] bytes = null;
            try {
                originalFileName = file.getOriginalFilename();
                bytes = file.getBytes();
                uuid = UUID.nameUUIDFromBytes(bytes).toString();
                ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                Files.write(Paths.get(UPLOAD_PATH + uuid + "." + ext), bytes);
            } catch (IOException e) {
                results.add(ImageUploadResponse.builder()
                                               .success(false)
                                               .originalFileName(originalFileName)
                                               .build());
                continue;
            }
            ProductImage productImage = ProductImage.builder()
                                                    .url(uuid + "." + ext)
                                                    .isMainImage(successCount++ > 0 ? false : true)
                                                    .build();
            productImageRepository.save(productImage);
            results.add(ImageUploadResponse.builder()
                                           .productImageId(productImage.getId())
                                           .url(productImage.getUrl())
                                           .originalFileName(originalFileName)
                                           .build());
        };
        return results;
    }

    public ResponseEntity<byte[]> display(String url) {
        ResponseEntity<byte[]> response = null;
        File file = new File(UPLOAD_PATH + url);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", Files.probeContentType(file.toPath()));
            response = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private void createDirIfNotExist() {
        File dir = new File(UPLOAD_PATH);
        if (!dir.exists()){
            dir.mkdir();
        }
    }
}
