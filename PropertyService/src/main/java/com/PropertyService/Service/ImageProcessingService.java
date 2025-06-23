package com.PropertyService.Service;

import com.PropertyService.Entity.Property; 
import com.PropertyService.Entity.PropertyImages;
import com.PropertyService.Repository.PropertyImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessingService {
	@Autowired
    private final RekognitionService rekognitionService;
	@Autowired
	private final S3Service s3Service;
	@Autowired
	private final PropertyImagesRepository propertyImagesRepository;

    @Async
    public void processImageAsync(MultipartFile file, Property property) {
        try {
            byte[] compressedBytes = compressImage(file);
            boolean isValid = rekognitionService.isPropertyImage(compressedBytes);

            if (!isValid) {
                saveRejectedImage(property, "Image not identified as property image");
                log.info("Image not identified as property image");
                return;
            }

            String key = "property-images/" + UUID.randomUUID() + ".jpg";
            String s3Url = s3Service.uploadBytes(compressedBytes, key);

            PropertyImages image = new PropertyImages();
            image.setProperty(property);
            image.setPropertyImageUrl(s3Url);
            image.setValid(true);
            propertyImagesRepository.save(image);
            log.info("Images were identified as property image and saved {}"+image.getPropertyImageUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] compressImage(MultipartFile file) throws IOException {
        long sizeInKB = file.getSize() / 1024;
        float quality = sizeInKB > 800 ? 0.7f : sizeInKB > 300 ? 0.8f : 0.9f;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .scale(1.0)
                .outputQuality(quality)
                .outputFormat("jpg")
                .toOutputStream(outputStream);
        log.info("Image is compressed");
        return outputStream.toByteArray();
    }

    private void saveRejectedImage(Property property, String reason) {
        PropertyImages rejected = new PropertyImages();
        rejected.setProperty(property);
        rejected.setValid(false);
        rejected.setRejectionReason(reason);
        propertyImagesRepository.save(rejected);
        log.info("Image is rejected {} and the reason is {}"+rejected.getPropertyImageUrl()+reason);
    }
}
