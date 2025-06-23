package com.SecurityAuthConfig.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.SecurityAuthConfig.Dto.APIResponse;
import com.SecurityAuthConfig.Dto.UserDto;
import com.SecurityAuthConfig.Entity.User;
import com.SecurityAuthConfig.Enums.UserRole;
import com.SecurityAuthConfig.Repository.UserRepository;
@Service
public class AuthService {	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	public APIResponse<String> register(UserDto dto) {
			// Rules for Saving User name//
		if (dto.getUsername() == null) {
	        APIResponse<String> response = new APIResponse<>();
	        response.setMessage("Registration Failed");
	        response.setStatus(500);
	        response.setData("Username is null");
	        return response;
	    }
		
		if (dto.getUsername().length() < 8 || dto.getUsername().contains(" ")) {
	        APIResponse<String> response = new APIResponse<>();
	        response.setMessage("Registration Failed");
	        response.setStatus(500);
	        response.setData("Username must be at least 8 characters long and must not contain spaces");
	        return response;
	    }
		if(userRepository.existsByUsername(dto.getUsername())) {
			APIResponse<String> response = new APIResponse<>();
			response.setMessage("Registration Failed");
			response.setStatus(500);
			response.setData("User with username exists");
			return response;
		}
		if(userRepository.existsByEmail(dto.getEmail())) {
			APIResponse<String> response = new APIResponse<>();
			response.setMessage("Registration Failed");
			response.setStatus(500);
			response.setData("User with Email Id exists");
			return response;
		}
		
		// Rules for Saving Password//
		int specialCharCount = 0;

		for (char ch : dto.getPassword().toCharArray()) {
		    if (!Character.isLetterOrDigit(ch)) {
		        specialCharCount++;
		    }
		}

		if (dto.getPassword().length() < 8 || dto.getPassword().length() > 15 ||
		    !dto.getPassword().matches(".*[A-Z].*") ||        // at least one uppercase
		    !dto.getPassword().matches(".*[a-z].*") ||        // at least one lowercase
		    specialCharCount < 2)                    // at least two special characters
		{
		    APIResponse<String> response = new APIResponse<>();
		    response.setMessage("Registration Failed");
		    response.setStatus(500);
		    response.setData("Password must be 8-15 characters long, "
		    		+ "Contain at least 1 uppercase letter, 1 lowercase letter, "
		    		+ "and 2 special characters");
		    return response;
		}
		
		// Rules for Saving the avaialble Roles//
		UserRole role = dto.getRole();
		if (role == null || (!role.equals(UserRole.ROLE_CUSTOMER) && !role.equals(UserRole.ROLE_ADMIN))) {
		    APIResponse<String> response = new APIResponse<>();
		    response.setMessage("Registration Failed");
		    response.setStatus(400);
		    response.setData("Invalid role. Must be ROLE_USER or ADMIN_USER.");
		    return response;
		}	
		
		//Finally Saving the User data//
		User user = new User();               // Creates object for User
		user.setName(dto.getName());
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setMobile(dto.getMobile());
		user.setPassword(dto.getPassword());
		user.setRole(dto.getRole()); //Converts dto to entity
		user.setPassword(dto.getPassword());  //Encrypts the Password
		user.setRole(dto.getRole());
		userRepository.save(user);          //Saves the User data
		APIResponse<String> response = new APIResponse<>();
		response.setMessage("Registration Done");
		response.setStatus(201);
		response.setData("User is registered");
		return response;
	}
	
	public Boolean updateMobileNumberByUserName(String username, String mobile) {
		Boolean user=userRepository.updateMobileByUsername(mobile,username)>0;
		
		return user;
	}
}
