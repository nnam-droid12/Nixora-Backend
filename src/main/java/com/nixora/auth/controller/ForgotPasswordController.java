package com.nixora.auth.controller;


import com.nixora.auth.entities.ForgotPassword;
import com.nixora.auth.entities.User;
import com.nixora.auth.repository.ForgotPasswordRepository;
import com.nixora.auth.repository.UserRepository;
import com.nixora.auth.service.EmailService;
import com.nixora.auth.utils.ChangePassword;
import com.nixora.auth.utils.MailBody;
import com.resend.core.exception.ResendException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/forgot-password")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmailHandler(@PathVariable String email) throws ResendException {

        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("user not found"));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the otp to verify your email " + otp)
                .subject(user.getName() + " Please Verify Your Email")
                .build();


        forgotPasswordRepository.findByUser(user).ifPresent(existingOtp ->
                forgotPasswordRepository.delete(existingOtp));

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .user(user)
                .expirationTime(new Date(System.currentTimeMillis() + 2 * 60* 10000))
                .build();
        emailService.sendMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/verify-otp/{otp}/{email}")
    public ResponseEntity<String> verifyOtpHandler(@PathVariable Integer otp,
                                                   @PathVariable String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("user not found"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("otp not found"));

        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(fp.getFpId());
            return new ResponseEntity<>("otp expired", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("Otp verified successfully");
    }

    @PostMapping("/change-password/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email){

        if(!Objects.equals(changePassword.password(), changePassword.repeatedPassword())){
            return new ResponseEntity<>("password does not match", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password changed successfully");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return 100_000 + random.nextInt(900_000);
    }

}


