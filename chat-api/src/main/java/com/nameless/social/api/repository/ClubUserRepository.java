package com.nameless.social.api.repository;

import com.nameless.social.core.entity.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {
}
