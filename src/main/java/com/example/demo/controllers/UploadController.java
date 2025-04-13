package com.example.demo.controllers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Value("${imgur.clientId}")
    private String imgurClientId;

    @PostMapping("/upload-imgur")
    public ResponseEntity<?> uploadToImgur(@RequestParam("image") MultipartFile file) {
        try {
            String url = "https://api.imgur.com/3/image";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Client-ID " + imgurClientId);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map data = (Map) ((Map) response.getBody().get("data"));
                String link = (String) data.get("link");
                return ResponseEntity.ok(Map.of("link", link));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Imgur upload failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }
}

