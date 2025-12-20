package com.nixora.auth.utils;

public record MailBody(String to, String subject, String text) {
    public static MailBodyBuilder builder() {
        return new MailBodyBuilder();
    }

    public static class MailBodyBuilder {
        private String to;
        private String subject;
        private String text;

        MailBodyBuilder() {
        }

        public MailBodyBuilder to(String to) {
            this.to = to;
            return this;
        }

        public MailBodyBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBodyBuilder text(String text) {
            this.text = text;
            return this;
        }

        public MailBody build() {
            return new MailBody(this.to, this.subject, this.text);
        }

        public String toString() {
            return "MailBody.MailBodyBuilder(to=" + this.to + ", subject=" + this.subject + ", text=" + this.text + ")";
        }
    }
}
