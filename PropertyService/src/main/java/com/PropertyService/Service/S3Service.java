package com.PropertyService.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
	
	@Autowired
    private final AmazonS3 amazonS3 = AmazonS3ClientBuilder.defaultClient();
	@Value("${stagging.bucketname}")
    private  String stagingBucket;
	@Value("${final.bucketname}")
	private  String finalBucket;

    public String uploadStaging(MultipartFile file, String key) {
        try {
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(stagingBucket, key, inputStream, metadata);
            return amazonS3.getUrl(stagingBucket, key).toString();
        } catch (Exception e) {
        	log.error("Failed to upload to staging the reason is {}"+e);
            throw new RuntimeException("Failed to upload to staging");
        }
    }

    public String uploadBytes(byte[] bytes, String key) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setContentType("image/jpeg");
        amazonS3.putObject(finalBucket, key, new ByteArrayInputStream(bytes), metadata);
        return amazonS3.getUrl(finalBucket, key).toString();
    }
}
