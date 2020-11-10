package com.pwebk.SpringBootBlogApplication.controller;

import com.pwebk.SpringBootBlogApplication.dto.VoteDto;
import com.pwebk.SpringBootBlogApplication.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {

    //This API will to submit a vote on the Posts created by the user.
    //This functionality is similar to what we see in applications like Stackoverflow
    //The API Design for Votes is pretty simple as we have only one endpoint which just
    // submits the vote for a Post.

    //Mapping - /api/votes
    // Method - POST
    // Method Name - POST

    //The method is taking RequestBody as VoteDto.java , which represents the request coming
    // in from the client. //Inside the method we are calling the vote() method from the
    // VoteService.java class, and once this vote is saved to the database we are returning
    // an HttpStatus.OK response (ie. HTTP Status 200)

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
