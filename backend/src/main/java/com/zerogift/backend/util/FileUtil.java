package com.zerogift.backend.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileUtil {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String s3Bucket;

    public String update(MultipartFile multipartFile) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(s3Bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(s3Bucket, s3FileName).toString();
    }

}
