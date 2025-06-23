package com.SecurityAuthConfig.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;   
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import com.SecurityAuthConfig.Dto.APIResponse;
import com.SecurityAuthConfig.Dto.LoginDto;
import com.SecurityAuthConfig.Dto.LoginMobileOTPDto;
import com.SecurityAuthConfig.Dto.UserDto;
import org.springframework.http.HttpStatus;
import com.SecurityAuthConfig.Entity.User;
import com.SecurityAuthConfig.Repository.UserRepository;
import com.SecurityAuthConfig.Service.AuthService;
import com.SecurityAuthConfig.Service.JwtService;
import com.SecurityAuthConfig.Service.OTPService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private OTPService otpService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/register")
	@CachePut(value = "users", key = "#dto.userName")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto) {
		APIResponse<String> response = authService.register(dto);
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
	}
	
	@PostMapping("/login")
	@Cacheable(value = "logins", key = "#loginDto.userName")
	public ResponseEntity<APIResponse<String>> loginCheck(@RequestBody LoginDto loginDto){
		APIResponse<String> response = new APIResponse<>();
		UsernamePasswordAuthenticationToken token = 
				 new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword());
		try {
			Authentication authenticate = authManager.authenticate(token);
			System.out.println(authenticate);
			if(authenticate.isAuthenticated()) {
				String jwtToken = jwtService.generateToken(loginDto.getUserName(),
							authenticate.getAuthorities().iterator().next().getAuthority());
				response.setMessage("Login Sucessful");
				response.setStatus(200);
				response.setData(jwtToken);
				return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
			}
		} catch (Exception ex) {
			response.setMessage("Authentication Failed");
			response.setStatus(401);
			response.setData("Un-Authorized Access");
			return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
		}
		response.setMessage("Authentication failed");
		response.setStatus(401);
		response.setData("Unauthorized");
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
	}
	
	@GetMapping("/get-user")
	@Cacheable(value = "users", key = "#username")
	public User getUser(@RequestParam String username) {
		return userRepository.findByUsername(username);
	}
	
	@PostMapping("/login/mobile")
	public ResponseEntity<APIResponse<String>> loginWithOtp(@RequestBody LoginMobileOTPDto otpDto) {
	    APIResponse<String> response = new APIResponse<>();

	    // Step 1: Validate OTP
	    boolean isValid = otpService.validateOTP(otpDto.getMobile(), otpDto.getOtp());

	    if (!isValid) {
	        response.setMessage("Invalid or expired OTP");
	        response.setStatus(401);
	        response.setData(null);
	        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	    }

	    // Step 2: Fetch user by mobile number
	    Optional<User> userOptional = userRepository.findByMobile(otpDto.getMobile());
	    if (userOptional.isEmpty()) {
	        response.setMessage("User not found");
	        response.setStatus(404);
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

	    User user = userOptional.get();

	    // Step 3: Generate JWT Token
	    String jwtToken = jwtService.generateToken(user.getUsername(), user.getRole().name());

	    // Step 4: Return Token
	    response.setMessage("Login Successful with OTP");
	    response.setStatus(200);
	    response.setData(jwtToken);
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

	 
	
	@GetMapping("/get-email")
	@Cacheable(value = "emails", key = "#username")
	public User getEmailId(@RequestParam String username) {
		return userRepository.findByEmail(username);
	}
	
	@PutMapping("/add-mobile")
	@Cacheable(value = "users", key = "#username")
	public Boolean addMobileNo(@RequestParam String username, @RequestParam String mobile) {
		 
		return authService.updateMobileNumberByUserName(username,mobile);
	}
}
