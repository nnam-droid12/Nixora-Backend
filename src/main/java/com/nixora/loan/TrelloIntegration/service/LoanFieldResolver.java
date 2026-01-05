package com.nixora.loan.TrelloIntegration.service;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class LoanFieldResolver {

    public String resolve(Object root, String path) {
        try {
            String[] parts = path.split("\\.");
            Object current = root;

            for (String part : parts) {
                Field f = current.getClass().getDeclaredField(part);
                f.setAccessible(true);
                current = f.get(current);
                if (current == null) return null;
            }

            return String.valueOf(current);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid field path: " + path);
        }
    }
}
