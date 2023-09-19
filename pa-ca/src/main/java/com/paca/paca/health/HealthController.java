package com.paca.paca.health;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthComponent;

@RestController
@RequiredArgsConstructor
@RequestMapping(HealthStatics.Endpoint.PATH)
public class HealthController {
    private final HealthEndpoint healthEndpoint;

    @GetMapping("/status")
    public HealthComponent health() {
        return healthEndpoint.health();
    }
}
