package com.technet.backend.controller.auth;

import com.technet.backend.service.auth.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validatePayment(@RequestBody Map<String, Object> payload) {
        return paymentService.validate(payload);
    }

    @PostMapping("/external-data")
    public ResponseEntity<Map<String, Object>> postExternalData(@RequestBody String body) {
        return paymentService.postExternalData(body);

    }
    @PostMapping("/v2/external-data")
    public ResponseEntity<Map<String, Object>> postExternalDatav2(@RequestBody String body) {
        return paymentService.postExternalDatav2(body);

    }
}
