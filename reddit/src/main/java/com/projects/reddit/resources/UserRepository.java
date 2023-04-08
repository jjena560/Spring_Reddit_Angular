package com.projects.reddit.resources;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.reddit.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
