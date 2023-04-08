package com.projects.reddit.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.proc.SecurityContext;
import com.projects.reddit.dto.AuthenticationResponse;
import com.projects.reddit.dto.LoginRequest;
import com.projects.reddit.dto.RegisterRequest;
import com.projects.reddit.exception.SpringRedditException;
import com.projects.reddit.model.NotificationEmail;
import com.projects.reddit.model.User;
import com.projects.reddit.model.VerificationToken;
import com.projects.reddit.resources.UserRepository;
import com.projects.reddit.resources.VerificationTokenRepository;
import com.projects.reddit.security.JwtProvider;

@Service
public class AuthService {
	
	@Autowired
	private PasswordEncoder passEncode;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private VerificationTokenRepository tokenRepo;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	// As there are multiple implementation of this so we need to create a bean inside our project. Here in SecurityConfig
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * @param registerRequest
	 * we are performing Db operation hence @Transactional
	 */
	@Transactional
	public void signUp(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passEncode.encode(registerRequest.getPassword()));;
		user.setCreatedTime(Instant.now());
		user.setActive(false);
		userRepo.save(user);
		
		String generatedToken = generateVerficationToken(user);
		
		
		mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + generatedToken));
	}

	private String generateVerficationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verToken = new VerificationToken();
		verToken.setToken(token);
		verToken.setUser(user);
		tokenRepo.save(verToken);
		return token;		
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = tokenRepo.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid token"));
		fetchUserAndEnable(verificationToken.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepo.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with username "+ username));
		user.setActive(true);
		userRepo.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		// in pom.xml - auth2 - provides JWT capabilities
		Authentication authenticate =  authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		
		return new AuthenticationResponse(token, loginRequest.getUsername());
	}
}