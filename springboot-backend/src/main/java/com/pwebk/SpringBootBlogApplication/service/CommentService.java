package com.pwebk.SpringBootBlogApplication.service;


import com.pwebk.SpringBootBlogApplication.dto.CommentsDto;
import com.pwebk.SpringBootBlogApplication.exceptions.PostNotFoundException;
import com.pwebk.SpringBootBlogApplication.mapper.CommentMapper;
import com.pwebk.SpringBootBlogApplication.model.Comment;
import com.pwebk.SpringBootBlogApplication.model.NotificationEmail;
import com.pwebk.SpringBootBlogApplication.model.Post;
import com.pwebk.SpringBootBlogApplication.model.User;
import com.pwebk.SpringBootBlogApplication.repository.CommentRepository;
import com.pwebk.SpringBootBlogApplication.repository.PostRepository;
import com.pwebk.SpringBootBlogApplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    //This method returns a List which is of type CommentsDto (Java Generics). Inside the method we
    //query the user table (via userRepository) by the username that was passed(if there are any errors
    // we propagate/throw the errors) and store the result in a user variable of type User entity.
    //Next we use CommentRepository to find all users then we straem the response. After the result
    //we map the response as CommentsDto object (to eneable it fit teh return type of this method).
    //We then convert the result into a List
    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
