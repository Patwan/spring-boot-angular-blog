package com.pwebk.SpringBootBlogApplication.repository;

import com.pwebk.SpringBootBlogApplication.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}