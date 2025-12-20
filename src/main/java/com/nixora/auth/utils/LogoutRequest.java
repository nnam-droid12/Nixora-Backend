package com.nixora.auth.utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class LogoutRequest {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    LogoutRequest(@NotBlank(message = "Email cannot be empty") @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public static LogoutRequestBuilder builder() {
        return new LogoutRequestBuilder();
    }

    public @NotBlank(message = "Email cannot be empty") @Email(message = "Email should be valid") String getEmail() {
        return this.email;
    }

    public void setEmail(@NotBlank(message = "Email cannot be empty") @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LogoutRequest)) return false;
        final LogoutRequest other = (LogoutRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LogoutRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        return result;
    }

    public String toString() {
        return "LogoutRequest(email=" + this.getEmail() + ")";
    }

    public static class LogoutRequestBuilder {
        private @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email should be valid") String email;

        LogoutRequestBuilder() {
        }

        public LogoutRequestBuilder email(@NotBlank(message = "Email cannot be empty") @Email(message = "Email should be valid") String email) {
            this.email = email;
            return this;
        }

        public LogoutRequest build() {
            return new LogoutRequest(this.email);
        }

        public String toString() {
            return "LogoutRequest.LogoutRequestBuilder(email=" + this.email + ")";
        }
    }
}
