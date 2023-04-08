package com.projects.reddit.resources;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.reddit.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
