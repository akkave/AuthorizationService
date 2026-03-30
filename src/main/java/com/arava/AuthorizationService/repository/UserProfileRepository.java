package com.arava.AuthorizationService.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arava.AuthorizationService.domain.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

	Optional<UserProfile> findBySubject(String subject);

}
