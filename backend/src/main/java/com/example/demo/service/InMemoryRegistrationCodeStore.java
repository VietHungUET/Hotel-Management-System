package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Profile("!cloud")
public class InMemoryRegistrationCodeStore implements RegistrationCodeStore {

    private static final long TTL_SECONDS = 600;

    private final ConcurrentMap<String, RegistrationCode> codes = new ConcurrentHashMap<>();

    @Override
    public void save(String code, String email) {
        cleanupExpiredCodes();
        codes.put(code, new RegistrationCode(email, Instant.now().plusSeconds(TTL_SECONDS)));
    }

    @Override
    public boolean consume(String code) {
        RegistrationCode registrationCode = codes.remove(code);
        return registrationCode != null && registrationCode.expiresAt().isAfter(Instant.now());
    }

    private void cleanupExpiredCodes() {
        Instant now = Instant.now();
        codes.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));
    }

    private record RegistrationCode(String email, Instant expiresAt) {
    }
}
