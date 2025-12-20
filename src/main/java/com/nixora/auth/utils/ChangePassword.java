package com.nixora.auth.utils;


public record ChangePassword(String password, String repeatedPassword) {

    public static ChangePasswordBuilder builder() {
        return new ChangePasswordBuilder();
    }

    public static class ChangePasswordBuilder {
        private String password;
        private String repeatedPassword;

        ChangePasswordBuilder() {
        }

        public ChangePasswordBuilder password(String password) {
            this.password = password;
            return this;
        }

        public ChangePasswordBuilder repeatedPassword(String repeatedPassword) {
            this.repeatedPassword = repeatedPassword;
            return this;
        }

        public ChangePassword build() {
            return new ChangePassword(this.password, this.repeatedPassword);
        }

        public String toString() {
            return "ChangePassword.ChangePasswordBuilder(password=" + this.password + ", repeatedPassword=" + this.repeatedPassword + ")";
        }
    }
}
