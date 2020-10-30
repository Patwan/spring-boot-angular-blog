package com.pwebk.SpringBootBlogApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String text;

    //Reference to Post entity with a ManytoOne relationship
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="postId", referencedColumnName = "postId")
    private Post post;
    private Instant createdDate;

    //Reference to User entity with a ManytoOne relationship
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;

}
