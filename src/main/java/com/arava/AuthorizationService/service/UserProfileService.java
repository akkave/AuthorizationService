package com.arava.AuthorizationService.service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arava.AuthorizationService.domain.UserProfile;
import com.arava.AuthorizationService.repository.UserProfileRepository;

@Service
public class UserProfileService {

	private final UserProfileRepository profiles;

	public UserProfileService(UserProfileRepository profiles) {
		this.profiles = profiles;
	}

	public Map<String, Object> buildMeResponse(Jwt jwt) {
		String sub = jwt.getSubject();
		UserProfile profile = profiles.findBySubject(sub).orElse(null);

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("subject", sub);
		body.put("claims", jwt.getClaims());
		body.put("profile", profile == null ? null : Map.of(
			"id", profile.getId(),
			"email", profile.getEmail() != null ? profile.getEmail() : "",
			"createdAt", profile.getCreatedAt().toString()));
		return body;
	}

	@Transactional
	public Map<String, Object> updateProfile(Jwt jwt, String email) {
		String sub = jwt.getSubject();
		UserProfile profile = profiles.findBySubject(sub).orElseGet(() -> {
			UserProfile p = new UserProfile();
			p.setSubject(sub);
			p.setCreatedAt(Instant.now());
			return p;
		});
		profile.setEmail(email);
		profiles.save(profile);
		return Map.of(
			"id", profile.getId(),
			"subject", profile.getSubject(),
			"email", profile.getEmail() != null ? profile.getEmail() : "");
	}

}
