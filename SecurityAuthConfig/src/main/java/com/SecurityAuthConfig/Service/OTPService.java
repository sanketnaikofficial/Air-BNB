package com.SecurityAuthConfig.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.SecurityAuthConfig.Entity.User;
import com.SecurityAuthConfig.Repository.UserRepository;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Service
public class OTPService {
    
    @Autowired
    private UserRepository userRepository;

    private static final int OTP_VALID_DURATION = 5 * 60; // 5 minutes in seconds
    private final Map<String, OTPData> otpStorage = new HashMap<>();
    private final SecureRandom random = new SecureRandom();
    

    // Generate and store OTP for a given key (mobile/email)
    public int generateOTP(String mobile) {
        int otp=0;
        Optional<User> isMobile = userRepository.findByMobile(mobile);
        if(isMobile.isPresent())   {
            otp = 100000 + random.nextInt(900000); // 6-digit OTP
            otpStorage.put(mobile, new OTPData(otp, Instant.now().getEpochSecond()));
            System.out.println(otp);
            return otp;
        }
        return otp;
    }
    
    public int generateOTPForEmail(String email) {
        int otp=0;
        Optional<User> isEmail = userRepository.findByEmailIdforOTP(email);
        if(isEmail.isPresent())   {
            otp = 100000 + random.nextInt(900000); // 6-digit OTP
            otpStorage.put(email, new OTPData(otp, Instant.now().getEpochSecond()));
            System.out.println(otp);
            return otp;
        }
        return otp;
    }

    // Validate OTP (Checks both correctness and expiry)
    public Boolean validateOTP(String key, int otp) {
        OTPData otpData = otpStorage.get(key);

        if (otpData == null) {
            return false;
        }

        long currentTime = Instant.now().getEpochSecond();

        if (currentTime - otpData.getTimestamp() > OTP_VALID_DURATION) {
            otpStorage.remove(key); // Remove expired OTP
            return false;
        }

        if (otpData.getOtp() == otp) {
            otpStorage.remove(key); // Remove OTP after successful validation
            return true;
        }

        return false;
    }

    // Background job to remove expired OTPs every 1 minute
    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void cleanupExpiredOTPs() {
        long currentTime = Instant.now().getEpochSecond();
        Iterator<Map.Entry<String, OTPData>> iterator = otpStorage.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, OTPData> entry = iterator.next();
            if (currentTime - entry.getValue().getTimestamp() > OTP_VALID_DURATION) {
                iterator.remove();
            }
        }
    }

    // Inner class to hold OTP and timestamp
    private static class OTPData {
        private final int otp;
        private final long timestamp;

        public OTPData(int otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public int getOtp() {
            return otp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
