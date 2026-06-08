package com.example.demo.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@Profile("cloud")
@RequiredArgsConstructor
public class FirestoreRegistrationCodeStore implements RegistrationCodeStore {

    private static final String COLLECTION = "registration_codes";
    private static final long TTL_SECONDS = 600;

    private final Firestore firestore;

    @Override
    public void save(String code, String email) {
        try {
            Map<String, Object> data = Map.of(
                    "email", email,
                    "expiresAt", Instant.now().plusSeconds(TTL_SECONDS).toString());

            document(code).set(data).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while saving registration code to Firestore", e);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save registration code to Firestore", e);
        }
    }

    @Override
    public boolean consume(String code) {
        DocumentReference document = document(code);
        try {
            DocumentSnapshot snapshot = document.get().get();
            if (!snapshot.exists()) {
                return false;
            }

            String expiresAt = snapshot.getString("expiresAt");
            if (expiresAt == null || Instant.parse(expiresAt).isBefore(Instant.now())) {
                document.delete().get();
                return false;
            }

            document.delete().get();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private DocumentReference document(String code) {
        return firestore.collection(COLLECTION).document(code);
    }
}
