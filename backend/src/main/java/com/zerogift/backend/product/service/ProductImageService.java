package com.zerogift.backend.product.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zerogift.backend.product.dto.ImageUploadResponse;
import com.zerogift.backend.product.entity.ProductImage;
import com.zerogift.backend.product.repository.ProductImageRepository;
import com.zerogift.backend.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final FileUtil fileUtil;

    public List<ImageUploadResponse> upload(MultipartFile[] request) {
        List<ImageUploadResponse> results = new ArrayList<>();
        for (MultipartFile file : request) {
            String url = null, originalFileName = file.getOriginalFilename();
            try {
                url = fileUtil.update(file);
            } catch (IOException e) {
                results.add(ImageUploadResponse.builder()
                                               .success(false)
                                               .originalFileName(originalFileName)
                                               .build());
                continue;
            }
            ProductImage productImage = ProductImage.builder().url(url).build();
            productImageRepository.save(productImage);
            results.add(ImageUploadResponse.builder()
                                           .productImageId(productImage.getId())
                                           .url(productImage.getUrl())
                                           .originalFileName(originalFileName)
                                           .build());
        };
        return results;
    }
}
