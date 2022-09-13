package com.zerogift.backend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ImageUploadResponse {
    Long productImageId;
    @Builder.Default
    Boolean success = true;
    String url;
    String originalFileName;
}
