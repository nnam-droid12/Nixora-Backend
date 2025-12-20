package com.nixora.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @NotBlank(message = "refresh token cannot be blank")
    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expirationTime;

    @OneToOne
    private User user;

    public RefreshToken(Integer tokenId, @NotBlank(message = "refresh token cannot be blank") String refreshToken, Instant expirationTime, User user) {
        this.tokenId = tokenId;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    public RefreshToken() {
    }

    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }

    public Integer getTokenId() {
        return this.tokenId;
    }

    public @NotBlank(message = "refresh token cannot be blank") String getRefreshToken() {
        return this.refreshToken;
    }

    public Instant getExpirationTime() {
        return this.expirationTime;
    }

    public User getUser() {
        return this.user;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    public void setRefreshToken(@NotBlank(message = "refresh token cannot be blank") String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RefreshToken)) return false;
        final RefreshToken other = (RefreshToken) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$tokenId = this.getTokenId();
        final Object other$tokenId = other.getTokenId();
        if (this$tokenId == null ? other$tokenId != null : !this$tokenId.equals(other$tokenId)) return false;
        final Object this$refreshToken = this.getRefreshToken();
        final Object other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken))
            return false;
        final Object this$expirationTime = this.getExpirationTime();
        final Object other$expirationTime = other.getExpirationTime();
        if (this$expirationTime == null ? other$expirationTime != null : !this$expirationTime.equals(other$expirationTime))
            return false;
        final Object this$user = this.getUser();
        final Object other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RefreshToken;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $tokenId = this.getTokenId();
        result = result * PRIME + ($tokenId == null ? 43 : $tokenId.hashCode());
        final Object $refreshToken = this.getRefreshToken();
        result = result * PRIME + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        final Object $expirationTime = this.getExpirationTime();
        result = result * PRIME + ($expirationTime == null ? 43 : $expirationTime.hashCode());
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        return result;
    }

    public String toString() {
        return "RefreshToken(tokenId=" + this.getTokenId() + ", refreshToken=" + this.getRefreshToken() + ", expirationTime=" + this.getExpirationTime() + ", user=" + this.getUser() + ")";
    }

    public static class RefreshTokenBuilder {
        private Integer tokenId;
        private @NotBlank(message = "refresh token cannot be blank") String refreshToken;
        private Instant expirationTime;
        private User user;

        RefreshTokenBuilder() {
        }

        public RefreshTokenBuilder tokenId(Integer tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public RefreshTokenBuilder refreshToken(@NotBlank(message = "refresh token cannot be blank") String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenBuilder expirationTime(Instant expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public RefreshTokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public RefreshToken build() {
            return new RefreshToken(this.tokenId, this.refreshToken, this.expirationTime, this.user);
        }

        public String toString() {
            return "RefreshToken.RefreshTokenBuilder(tokenId=" + this.tokenId + ", refreshToken=" + this.refreshToken + ", expirationTime=" + this.expirationTime + ", user=" + this.user + ")";
        }
    }
}
