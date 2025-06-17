package com.example.demo.dtos;

import lombok.Data;

@Data
public class QRResponse {
    private int code;
    private String desc;
    private QRData data;

    @Data
    public static class QRData {
        private String qrDataURL;
        private String qrCodeURL;
    }
}