package com.PropertyService.Service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;

@Service
public class RekognitionService {
	
	@Autowired
    private final AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

    public boolean isPropertyImage(byte[] imageBytes) {
        ByteBuffer imageByteBuffer = ByteBuffer.wrap(imageBytes);
        Image image = new Image().withBytes(imageByteBuffer);

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(image)
                .withMaxLabels(10)
                .withMinConfidence(75F);

        DetectLabelsResult result = rekognitionClient.detectLabels(request);
        List<Label> labels = result.getLabels();

        for (Label label : labels) {
            String name = label.getName().toLowerCase();
            if (name.contains("room") || name.contains("apartment") || name.contains("bedroom") 
                || name.contains("interior") || name.contains("building")||name.contains("property")) {
                return true;
            }
        }
        return false;
    }
}

