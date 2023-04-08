package com.projects.reddit.resources;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.reddit.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
