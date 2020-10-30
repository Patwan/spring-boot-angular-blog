package com.pwebk.SpringBootBlogApplication.repository;

import com.pwebk.SpringBootBlogApplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
