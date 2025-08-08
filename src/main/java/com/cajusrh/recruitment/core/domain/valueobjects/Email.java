package com.cajusrh.recruitment.core.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
	        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
	        Pattern.CASE_INSENSITIVE
	);

    private final String value;

    public Email(String value) {
        Objects.requireNonNull(value, "Email cannot be null");
        String trimmed = value.trim();
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        this.value = trimmed;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email other = (Email) o;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
