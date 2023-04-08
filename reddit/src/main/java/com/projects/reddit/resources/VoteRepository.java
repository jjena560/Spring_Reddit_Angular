package com.projects.reddit.resources;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.reddit.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
