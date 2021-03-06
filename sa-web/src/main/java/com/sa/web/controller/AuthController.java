package com.sa.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.TextNode;
import com.sa.dao.entity.Role;
import com.sa.dao.entity.User;
import com.sa.dao.repository.RoleRepository;
import com.sa.dao.repository.UserRepository;
import com.sa.web.pojo.ReturnObject;
import com.sa.web.pojo.SignupUser;
import com.sa.web.pojo.UserUIComposite;

//@CrossOrigin
@RestController
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping(value = "/currentUser")
	public ReturnObject getDetails(Authentication authentication) {
		ReturnObject retVal = new ReturnObject();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails principal = (UserDetails) authentication.getPrincipal();
			User user = userRepository.findByEmail(principal.getUsername());
			retVal.setResult(UserUIComposite.fromUser(user));
		} else {
			retVal.setResult("anonymousUser");
		}
		return retVal;
	}

	@PostMapping(value = "/register")
	public ReturnObject registerUser(@RequestBody @Valid SignupUser signupUser, BindingResult bindingResult) {
		ReturnObject retVal = new ReturnObject();
		boolean isValid = true;
		if (signupUser == null || bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream().map(err -> {
				if (err instanceof FieldError) {
					return ((FieldError) err).getField() + " " + err.getDefaultMessage();
				} else {
					return err.getDefaultMessage();
				}
			}).collect(Collectors.toList());
			retVal.addObject("errors", errors);
			retVal.setErrorMessage("Unable to register user");
			isValid = false;
		}

		if (isValid) {
			User user = signupUser.toUser();
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			Role role = roleRepository.findByName("ROLE_USER");
			user.addRole(role);
//			userRepository.save(user);
		}
		return retVal;
	}

	@PostMapping(value = "/forgetPass", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnObject forgetPass(@RequestBody String email) {
		ReturnObject retVal = new ReturnObject();
		retVal.setErrorMessage("This functinality will be implemented soon");
		return retVal;
	}
}
