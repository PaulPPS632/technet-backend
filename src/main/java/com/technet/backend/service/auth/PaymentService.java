package com.technet.backend.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${payment.url}")
    private String url;

    @Value("${payment.username}")
    private String username;

    @Value("${payment.password}")
    private String password;

    @Value("${payment.hash}")
    private String payment_hash;

    private final RestTemplate restTemplate;
    public ResponseEntity<Map<String, Object>> postExternalData(String jsonBody) {
        System.out.println("Received JSON body: " + jsonBody);
        String encodedCredentials = encodeCredentials(username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
        );
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> answer = (Map<String, Object>) responseBody.get("answer");
        String formToken = (String) answer.get("formToken");
        Map<String, Object> result = new HashMap<>();
        result.put("formToken", formToken);

        return ResponseEntity.ok(result);
    }
    public ResponseEntity<Map<String, Object>> postExternalDatav2(String jsonBody) {
        System.out.println("Received JSON body: " + jsonBody);
        String encodedCredentials = encodeCredentials(username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
        );
        Map<String, Object> responseBody = response.getBody();

        return ResponseEntity.ok(responseBody);
    }
    public static String encodeCredentials(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return new String(encodedAuth, StandardCharsets.UTF_8);
    }

    public ResponseEntity<Map<String, Object>> validate(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            String answerJson = mapper.writeValueAsString(payload.get("clientAnswer"));
            String hash = (String) payload.get("hash");
            String computedHash = Hex.encodeHexString(HmacUtils.hmacSha256(payment_hash, answerJson));


            if (hash.equals(computedHash)) {
                response.put("Status", true);
                return ResponseEntity.ok(response);
            } else {
                response.put("Status", false);
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            response.put("Status", false);
            return ResponseEntity.status(500).body(response);
        }
    }
}
