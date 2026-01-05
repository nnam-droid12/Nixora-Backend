package com.nixora.loan.document.service;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class LoanFieldUpdater {

    public void update(Object root, String path, String newValue) {
        try {
            String[] parts = path.split("\\.");
            Object current = root;

            // Traverse the object graph (e.g., parties.borrower)
            for (int i = 0; i < parts.length - 1; i++) {
                Field f = getField(current.getClass(), parts[i]);
                f.setAccessible(true);
                Object next = f.get(current);

                // If a nested object (like 'parties') is null, we can't update its children
                if (next == null) {
                    throw new RuntimeException("Null intermediate field: " + parts[i]);
                }
                current = next;
            }

            // Set the final field value
            String fieldName = parts[parts.length - 1];
            Field targetField = getField(current.getClass(), fieldName);
            targetField.setAccessible(true);

            // Convert the String input to the actual field type (Boolean, Double, etc.)
            Object converted = convert(newValue, targetField.getType());
            targetField.set(current, converted);

        } catch (Exception e) {
            throw new RuntimeException("Update failed for path: " + path + ". Error: " + e.getMessage(), e);
        }
    }

    /**
     * Helper to find fields even if they are in a parent class
     */
    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) throw e;
            return getField(superClass, fieldName);
        }
    }

    private Object convert(String value, Class<?> type) {
        if (value == null || value.equalsIgnoreCase("null")) return null;

        if (type == String.class) return value;
        if (type == Boolean.class || type == boolean.class) return Boolean.parseBoolean(value);
        if (type == Integer.class || type == int.class) return Integer.parseInt(value);
        if (type == Long.class || type == long.class) return Long.parseLong(value);
        if (type == Double.class || type == double.class) return Double.parseDouble(value);

        return value;
    }
}