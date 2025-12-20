package com.nixora.auth.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "forgot_password")
public class ForgotPassword {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpId;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;

    public ForgotPassword(Integer fpId, Integer otp, Date expirationTime, User user) {
        this.fpId = fpId;
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    public ForgotPassword() {
    }

    public static ForgotPasswordBuilder builder() {
        return new ForgotPasswordBuilder();
    }

    public Integer getFpId() {
        return this.fpId;
    }

    public Integer getOtp() {
        return this.otp;
    }

    public Date getExpirationTime() {
        return this.expirationTime;
    }

    public User getUser() {
        return this.user;
    }

    public void setFpId(Integer fpId) {
        this.fpId = fpId;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ForgotPassword)) return false;
        final ForgotPassword other = (ForgotPassword) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$fpId = this.getFpId();
        final Object other$fpId = other.getFpId();
        if (this$fpId == null ? other$fpId != null : !this$fpId.equals(other$fpId)) return false;
        final Object this$otp = this.getOtp();
        final Object other$otp = other.getOtp();
        if (this$otp == null ? other$otp != null : !this$otp.equals(other$otp)) return false;
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
        return other instanceof ForgotPassword;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fpId = this.getFpId();
        result = result * PRIME + ($fpId == null ? 43 : $fpId.hashCode());
        final Object $otp = this.getOtp();
        result = result * PRIME + ($otp == null ? 43 : $otp.hashCode());
        final Object $expirationTime = this.getExpirationTime();
        result = result * PRIME + ($expirationTime == null ? 43 : $expirationTime.hashCode());
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        return result;
    }

    public String toString() {
        return "ForgotPassword(fpId=" + this.getFpId() + ", otp=" + this.getOtp() + ", expirationTime=" + this.getExpirationTime() + ", user=" + this.getUser() + ")";
    }

    public static class ForgotPasswordBuilder {
        private Integer fpId;
        private Integer otp;
        private Date expirationTime;
        private User user;

        ForgotPasswordBuilder() {
        }

        public ForgotPasswordBuilder fpId(Integer fpId) {
            this.fpId = fpId;
            return this;
        }

        public ForgotPasswordBuilder otp(Integer otp) {
            this.otp = otp;
            return this;
        }

        public ForgotPasswordBuilder expirationTime(Date expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public ForgotPasswordBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ForgotPassword build() {
            return new ForgotPassword(this.fpId, this.otp, this.expirationTime, this.user);
        }

        public String toString() {
            return "ForgotPassword.ForgotPasswordBuilder(fpId=" + this.fpId + ", otp=" + this.otp + ", expirationTime=" + this.expirationTime + ", user=" + this.user + ")";
        }
    }
}
