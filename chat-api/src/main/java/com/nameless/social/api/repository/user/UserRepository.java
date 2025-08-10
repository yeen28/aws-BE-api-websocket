package com.nameless.social.api.repository.user;

import com.nameless.social.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByToken(final String token);
	Optional<User> findByEmail(final String email);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE User u SET name = :username WHERE u.id = :userId")
	int updateUsername(@Param("userId") final long userId, @Param("username") final String username);
}
