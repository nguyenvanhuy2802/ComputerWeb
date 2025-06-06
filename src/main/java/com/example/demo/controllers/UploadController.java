package com.example.demo.controllers;

import com.example.demo.utils.MultipartInputStreamFileResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UploadController {
    @PostMapping("/upload-imgur")
    public ResponseEntity<?> uploadToImgur(@RequestParam("image") MultipartFile file) {
        try {
            String url = "https://catbox.moe/user/api.php";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Chuẩn bị body theo form data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            // Loại form field upload
            body.add("reqtype", "fileupload");
            // Thêm file upload (dùng helper MultipartInputStreamFileResource để đọc MultipartFile)
            body.add("fileToUpload", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename(), file.getSize()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();

            // Gửi POST request
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Response body là link trực tiếp tới file vừa upload
                String imageUrl = response.getBody();
                return ResponseEntity.ok(Map.of("link", imageUrl));
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    }
