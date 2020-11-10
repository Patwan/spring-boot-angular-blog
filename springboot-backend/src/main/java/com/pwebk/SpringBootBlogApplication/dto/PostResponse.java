package com.pwebk.SpringBootBlogApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    //When the user queries the Post API to get a single post or all posts.
    // We have to also provide the vote information along with post details.

    //We added 3 new fields inside the PostResponse.java class – voteCount,
    // commentCount, duration
    private Long id;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String subredditName;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
    private boolean upVote;
    private boolean downVote;
}
