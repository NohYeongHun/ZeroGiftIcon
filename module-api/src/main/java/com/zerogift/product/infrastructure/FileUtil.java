package com.zerogift.product.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import javax.imageio.ImageIO;
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

    public String update(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());

        String fileName = UUID.randomUUID().toString() + System.currentTimeMillis() + ".png";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());

        amazonS3.putObject(s3Bucket, fileName, inputStream, objectMetadata);

        return amazonS3.getUrl(s3Bucket, fileName).toString();
    }

}
