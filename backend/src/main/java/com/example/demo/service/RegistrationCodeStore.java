package com.example.demo.service;

public interface RegistrationCodeStore {

    void save(String code, String email);

    boolean consume(String code);
}
