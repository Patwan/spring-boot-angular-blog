package com.pwebk.SpringBootBlogApplication.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

//This annotation  generates getters and setters for our class
@Data
@Entity

//Generates builder methods for our class
@Builder

//These 2 annotations generates constructors for the class
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    //throws error if name field is blank
    @NotBlank(message = "Post Name cannot be empty or null")
    private String postName;

    //Nullable field
    @Nullable
    private String url;

    @Nullable
    @Lob  //Means we can have large chunks of text stored inside the description
    private String description;
    private Integer voteCount;

    //ManytoOne relationship (this means many users can have  1 post) and a table join.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId" , referencedColumnName = "userId")
    private User user;
    //It will store the actual date
    private Instant createdDate;

    //Reference to subreddit entity and a join
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Subreddit subreddit;
}
