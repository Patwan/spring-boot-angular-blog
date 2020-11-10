package com.pwebk.SpringBootBlogApplication.service;

import com.pwebk.SpringBootBlogApplication.dto.VoteDto;
import com.pwebk.SpringBootBlogApplication.exceptions.PostNotFoundException;
import com.pwebk.SpringBootBlogApplication.exceptions.SpringRedditException;
import com.pwebk.SpringBootBlogApplication.model.Post;
import com.pwebk.SpringBootBlogApplication.model.Vote;
import com.pwebk.SpringBootBlogApplication.repository.PostRepository;
import com.pwebk.SpringBootBlogApplication.repository.VoteRepository;
import static com.pwebk.SpringBootBlogApplication.model.VoteType.UPVOTE;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {

        //Inside the vote() method of VoteService, we are first retrieving the Post object from the database using the PostRepository.findById() method.
        //If there is no Post with that id we are throwing a PostNotFoundException.
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));

        //We are retrieving the recent Vote submitted by the currently logged-in user for the given Post.
        // We are doing this using the method – findTopByPostAndUserOrderByVoteIdDesc()
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        // We are first checking whether the user has already performed the same Vote action or not. ie. If the user has already upvoted a particular post,
        // he/she is not allowed to upvote that particular post again.
        //SpringRedditException is a custome error handling class inside exceptions package
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }

        //We are setting the voteCount field according to the VoteType provided by the user, and then we are mapping the VoteDto
        // object to Vote object and saving the Vote as well as Post objects to the database.
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    //We are using the builder() from Lombok’s @Builder inside the mapToVote method. This builder method is an implementation of the Builder Design
    // Pattern.
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
