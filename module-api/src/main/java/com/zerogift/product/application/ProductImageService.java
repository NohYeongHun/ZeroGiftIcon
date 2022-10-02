package com.zerogift.product.application;

import com.zerogift.product.application.dto.ImageUploadResponse;
import com.zerogift.product.domain.ProductImage;
import com.zerogift.product.infrastructure.s3.FileUtil;
import com.zerogift.product.repository.ProductImageRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
