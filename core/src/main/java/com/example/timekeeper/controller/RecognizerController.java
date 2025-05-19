package com.example.timekeeper.controller;


import com.example.timekeeper.dto.RecognizeReqDto;
import com.example.timekeeper.service.integration.FRIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/recognizer")
@Slf4j
public class RecognizerController {
    private final FRIntegrationService frIntegrationService;

    public RecognizerController(FRIntegrationService frIntegrationService) {
        this.frIntegrationService = frIntegrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RecognizeReqDto recognizeReqDto) {
        String result = frIntegrationService.register(recognizeReqDto.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recognize")
    public ResponseEntity<String> recognize() {
        String result = frIntegrationService.recognize();
        return ResponseEntity.ok(result);
    }

}
