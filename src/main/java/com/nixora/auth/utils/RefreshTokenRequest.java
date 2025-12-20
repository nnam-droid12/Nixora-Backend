package com.nixora.auth.utils;

public class RefreshTokenRequest {

    private String refreshToken;

    RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshTokenRequest() {
    }

    public static RefreshTokenRequestBuilder builder() {
        return new RefreshTokenRequestBuilder();
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RefreshTokenRequest)) return false;
        final RefreshTokenRequest other = (RefreshTokenRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$refreshToken = this.getRefreshToken();
        final Object other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RefreshTokenRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $refreshToken = this.getRefreshToken();
        result = result * PRIME + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        return result;
    }

    public String toString() {
        return "RefreshTokenRequest(refreshToken=" + this.getRefreshToken() + ")";
    }

    public static class RefreshTokenRequestBuilder {
        private String refreshToken;

        RefreshTokenRequestBuilder() {
        }

        public RefreshTokenRequestBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenRequest build() {
            return new RefreshTokenRequest(this.refreshToken);
        }

        public String toString() {
            return "RefreshTokenRequest.RefreshTokenRequestBuilder(refreshToken=" + this.refreshToken + ")";
        }
    }
}
