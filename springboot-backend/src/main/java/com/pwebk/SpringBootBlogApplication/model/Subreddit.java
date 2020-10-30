package com.pwebk.SpringBootBlogApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Community name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    //Reference to the list of posts with one to many relationship
    @OneToMany(fetch = LAZY)
    private List<Post> posts;
    private Instant createdAt;

    //Reference to User entity with One to Many relationship
    @ManyToOne(fetch = LAZY)
    private User user;

}
