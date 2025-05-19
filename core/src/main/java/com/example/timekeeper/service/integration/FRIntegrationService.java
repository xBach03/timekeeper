package com.example.timekeeper.service.integration;

import com.example.timekeeper.dto.RecognizeResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

// Face recognizer integration service
@Service
@Slf4j
public class FRIntegrationService {
    private final RestTemplate restTemplate;
    private final String url = "http://192.168.100.109:8000";

    public FRIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String recognize() {
        String apiEndpoint = url + "/testmodel";
        return process(apiEndpoint);
    }

    public String register(String name) {
        String apiEndpoint = url + "/datacollect";
        return process(apiEndpoint, name);
    }

    protected String process(String endpoint, String name) {
        try {
            ResponseEntity<RecognizeResDto> response = restTemplate.postForEntity(endpoint, RecognizeResDto.class, RecognizeResDto.class);
            return validateResponse(response);
        } catch (Exception e) {
            return "Error calling Python API: " + e.getMessage();
        }
    }

    protected String process(String endpoint) {
        try {
            ResponseEntity<RecognizeResDto> response = restTemplate.getForEntity(endpoint, RecognizeResDto.class, Map.class);
            return validateResponse(response);
        } catch (Exception e) {
            return "Error calling Python API: " + e.getMessage();
        }
    }

    protected String validateResponse(ResponseEntity<RecognizeResDto> response) {
        log.info(response.getBody().getStatus());
        if (response.getStatusCode().is2xxSuccessful()) {
            RecognizeResDto result = response.getBody();
            if (result.getStatus().equals("success")) {
                log.info("Recognized {}", result.getName());
                return "Recognized: " + result.getName();
            } else {
                log.error("Recognition failed: {}", result.getName());
                return "Recognition failed: " + result.getName();
            }
        } else {
            return "API call failed with status: " + response.getStatusCode();
        }
    }

}
