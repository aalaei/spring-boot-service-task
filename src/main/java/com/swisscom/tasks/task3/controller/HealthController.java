package com.swisscom.tasks.task3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Health API")
public class HealthController {
    @Operation(
            description = "Health check",
            summary = "Health check",
            responses = {
                    @ApiResponse(
                            description = "Healthy",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description =  "Unhealthy",
                            responseCode = "500"
                    )
            }
    )
    @GetMapping
    public String health() {
        return "Healthy";
    }
}
