package com.example.timekeeper.service.integration;

import com.example.timekeeper.dto.RecognizeReqDto;
import com.example.timekeeper.dto.RecognizeResDto;
import com.example.timekeeper.dto.RegisterResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    protected String process(String endpoint) {
        try {
            ResponseEntity<RecognizeResDto> response = restTemplate.getForEntity(endpoint, RecognizeResDto.class, Map.class);
            log.info("Received response from recognizer: {}", response.getBody().toString());
            return validateRecognizeResponse(response);
        } catch (Exception e) {
            return "Error calling Python API: " + e.getMessage();
        }
    }

    protected String process(String endpoint, String name) {
        try {
            RecognizeReqDto reqDto = new RecognizeReqDto();
            reqDto.setName(name);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RecognizeReqDto> request = new HttpEntity<>(reqDto, headers);
            ResponseEntity<RegisterResDto> response = restTemplate.postForEntity(endpoint, request, RegisterResDto.class);
            log.info("Received response from recognizer: {}", response.getBody().toString());
            return validateRegisterResponse(response);
        } catch (Exception e) {
            return "Error calling Python API: " + e.getMessage();
        }
    }

    protected String validateRegisterResponse(ResponseEntity<RegisterResDto> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            RegisterResDto result = response.getBody();
            if (result.getStatus().equals("success")) {
                log.info("Recognized {}", result.getName());
                return "Registered: " + result.getName();
            } else {
                log.error("Recognition failed: {}", result.getName());
                return "Recognition failed: " + result.getName();
            }
        } else {
            return "API call failed with status: " + response.getStatusCode();
        }
    }

    protected String validateRecognizeResponse(ResponseEntity<RecognizeResDto> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            RecognizeResDto result = response.getBody();
            if (result.getStatus().equals("success")) {
                log.info("Recognized {}", result.getName());
                return result.getName();
            } else {
                log.error("Recognition failed: {}", result.getName());
                return "Recognition failed: " + result.getName();
            }
        } else {
            return "API call failed with status: " + response.getStatusCode();
        }
    }

}
