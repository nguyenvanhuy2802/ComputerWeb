package com.example.demo.controllers;

import com.example.demo.dtos.QRRequest;
import com.example.demo.services.impls.QRPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
public class QRCodeController {
    private final QRPaymentService qrPaymentService;

    public QRCodeController(QRPaymentService qrPaymentService) {
        this.qrPaymentService = qrPaymentService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(@RequestBody QRRequest qrRequest) {
        String qrCodeImage = qrPaymentService.generateQRCode(qrRequest);
        return ResponseEntity.ok(Map.of("qrCodeImage", qrCodeImage));
    }
}
