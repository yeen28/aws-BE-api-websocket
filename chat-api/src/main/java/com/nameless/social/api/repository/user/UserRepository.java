package com.nameless.social.api.repository.user;

import com.nameless.social.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByToken(String token);
	Optional<User> findByEmail(String email);
}
