package com.example.demo.services.impls;

import com.example.demo.dtos.QRRequest;
import com.example.demo.dtos.QRResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class QRPaymentService {

    @Value("${vietqr.api.url}")
    private String apiUrl;

    @Value("${vietqr.client.id}")
    private String clientId;

    @Value("${vietqr.api.key}")
    private String apiKey;

    @Value("${vietqr.account.no}")
    private String defaultAccountNo;

    @Value("${vietqr.account.name}")
    private String defaultAccountName;

    @Value("${vietqr.acq.id}")
    private String defaultAcqId;

    @Value("${vietqr.format}")
    private String defaultFormat;

    @Value("${vietqr.template}")
    private String defaultTemplate;


    private final RestTemplate restTemplate = new RestTemplate();

    public String generateQRCode(QRRequest qrRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountNo", defaultAccountNo);
        requestBody.put("accountName", defaultAccountName);
        requestBody.put("acqId", defaultAcqId);
        requestBody.put("amount", qrRequest.getAmount());
        requestBody.put("addInfo", qrRequest.getAddInfo());
        requestBody.put("format", defaultFormat);
        requestBody.put("template", defaultTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<QRResponse> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, request, QRResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()
                    && response.getBody() != null
                    && response.getBody().getData() != null) {
                return response.getBody().getData().getQrDataURL();
            } else {
                throw new RuntimeException("Lỗi khi gọi API VietQR: " + response.getBody().getDesc());
            }

        } catch (HttpServerErrorException e) {
            // Ghi log chi tiết nếu cần
            System.err.println("Lỗi từ phía server VietQR: " + e.getResponseBodyAsString());
            throw new RuntimeException("Hệ thống VietQR đang bảo trì. Vui lòng thử lại sau.");
        }

    }
}
