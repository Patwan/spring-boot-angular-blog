package com.pwebk.SpringBootBlogApplication.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.pwebk.SpringBootBlogApplication.dto.PostRequest;
import com.pwebk.SpringBootBlogApplication.dto.PostResponse;
import com.pwebk.SpringBootBlogApplication.model.*;
import com.pwebk.SpringBootBlogApplication.repository.CommentRepository;
import com.pwebk.SpringBootBlogApplication.repository.VoteRepository;
import com.pwebk.SpringBootBlogApplication.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.pwebk.SpringBootBlogApplication.model.VoteType.DOWNVOTE;
import static com.pwebk.SpringBootBlogApplication.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    //Now we have to update our PostMapper.java class to map also these new additional fields.
    // One advantage of creating this PostMapper.java is now our mapping logic is decoupled from the actual business logic.

    //instead of updating PostService.java class with this logic, we can just change the interface to abstract class and inject the needed
    // dependencies into our class to fill the new field’s information.

    //You can observe that the voteCount is set to a constant value – 0. This is because whenever we want to create a new Post object,
    // we set the default vote count as 0.

    //When mapping from Post to PostResponse, we explicitly need to map only the fields commentCount (handled through commentCount() method)
    // and duration (handled through getDuration())

    //The getDuration() is using a library called TimeAgo. This is a java library that shows us the dates in the relative Time Ago format.

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;


    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}