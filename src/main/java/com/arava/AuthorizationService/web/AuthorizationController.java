package com.arava.AuthorizationService.web;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arava.AuthorizationService.dto.ProfileUpdateRequest;
import com.arava.AuthorizationService.domain.UserProfile;
import com.arava.AuthorizationService.service.UserProfileService;

/**
 * Protected REST API for the authenticated caller (identified by the JWT {@code sub} claim).
 * Requires a valid Bearer token; used by the frontend after login to read identity and sync profile data.
 */
@RestController
@RequestMapping("/api")
public class AuthorizationController {

	private final UserProfileService userProfileService;

	public AuthorizationController(UserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}

	/**
	 * Returns the current user's JWT subject, all decoded claims, and the persisted profile row (if any).
	 * Lets the client verify the token is accepted and optionally show stored profile fields.
	 */
	@GetMapping("/me")
	public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
		return userProfileService.buildMeResponse(jwt);
	}

	/**
	 * Creates or updates the {@link UserProfile} for this JWT's subject (typically after first sign-in or when editing email).
	 * Persists application-specific data in PostgreSQL keyed by {@code sub}, separate from the token itself.
	 */
	@PutMapping("/profile")
	public ResponseEntity<Map<String, Object>> updateProfile(
			@AuthenticationPrincipal Jwt jwt,
			@RequestBody ProfileUpdateRequest request) {
		return ResponseEntity.ok(userProfileService.updateProfile(jwt, request.email()));
	}

}
