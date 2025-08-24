package com.nameless.social.api.repository;

import com.nameless.social.core.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
	Optional<Group> findByName(String name);
}
