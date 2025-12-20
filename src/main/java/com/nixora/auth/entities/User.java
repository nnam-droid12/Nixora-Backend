package com.nixora.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, message = "password must be 6 characters and above")
    private String password;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;


    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    public User(Integer userId, @NotBlank(message = "name cannot be blank") String name, @NotBlank(message = "email cannot be blank") @Email(message = "Email must be valid") String email, @NotBlank(message = "password cannot be blank") @Size(min = 6, message = "password must be 6 characters and above") String password, ForgotPassword forgotPassword, RefreshToken refreshToken) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.forgotPassword = forgotPassword;
        this.refreshToken = refreshToken;
    }

    public User() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public @NotBlank(message = "name cannot be blank") String getName() {
        return this.name;
    }

    public @NotBlank(message = "email cannot be blank") @Email(message = "Email must be valid") String getEmail() {
        return this.email;
    }

    public ForgotPassword getForgotPassword() {
        return this.forgotPassword;
    }

    public RefreshToken getRefreshToken() {
        return this.refreshToken;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(@NotBlank(message = "name cannot be blank") String name) {
        this.name = name;
    }

    public void setEmail(@NotBlank(message = "email cannot be blank") @Email(message = "Email must be valid") String email) {
        this.email = email;
    }

    public void setPassword(@NotBlank(message = "password cannot be blank") @Size(min = 6, message = "password must be 6 characters and above") String password) {
        this.password = password;
    }

    public void setForgotPassword(ForgotPassword forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$forgotPassword = this.getForgotPassword();
        final Object other$forgotPassword = other.getForgotPassword();
        if (this$forgotPassword == null ? other$forgotPassword != null : !this$forgotPassword.equals(other$forgotPassword))
            return false;
        final Object this$refreshToken = this.getRefreshToken();
        final Object other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $forgotPassword = this.getForgotPassword();
        result = result * PRIME + ($forgotPassword == null ? 43 : $forgotPassword.hashCode());
        final Object $refreshToken = this.getRefreshToken();
        result = result * PRIME + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        return result;
    }

    public String toString() {
        return "User(userId=" + this.getUserId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", forgotPassword=" + this.getForgotPassword() + ", refreshToken=" + this.getRefreshToken() + ")";
    }

    public static class UserBuilder {
        private Integer userId;
        private @NotBlank(message = "name cannot be blank") String name;
        private @NotBlank(message = "email cannot be blank")
        @Email(message = "Email must be valid") String email;
        private @NotBlank(message = "password cannot be blank")
        @Size(min = 6, message = "password must be 6 characters and above") String password;
        private ForgotPassword forgotPassword;
        private RefreshToken refreshToken;

        UserBuilder() {
        }

        public UserBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder name(@NotBlank(message = "name cannot be blank") String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(@NotBlank(message = "email cannot be blank") @Email(message = "Email must be valid") String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(@NotBlank(message = "password cannot be blank") @Size(min = 6, message = "password must be 6 characters and above") String password) {
            this.password = password;
            return this;
        }

        public UserBuilder forgotPassword(ForgotPassword forgotPassword) {
            this.forgotPassword = forgotPassword;
            return this;
        }

        public UserBuilder refreshToken(RefreshToken refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public User build() {
            return new User(this.userId, this.name, this.email, this.password, this.forgotPassword, this.refreshToken);
        }

        public String toString() {
            return "User.UserBuilder(userId=" + this.userId + ", name=" + this.name + ", email=" + this.email + ", password=" + this.password + ", forgotPassword=" + this.forgotPassword + ", refreshToken=" + this.refreshToken + ")";
        }
    }
}
