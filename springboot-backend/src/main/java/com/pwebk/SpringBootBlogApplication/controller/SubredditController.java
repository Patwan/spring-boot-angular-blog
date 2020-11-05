package com.pwebk.SpringBootBlogApplication.controller;

import com.pwebk.SpringBootBlogApplication.dto.SubredditDto;
import com.pwebk.SpringBootBlogApplication.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    //This createSubreddit method is responsible for creating a Subreddit
    //Its return type is ReponseEntity of type Subredditdto.  ResponseEntity is meant to represent the entire
    //HTTP response.you can control anything that goes into it: status code, headers, and body.
    //Once data comes it we map the data to SubredditDto dto (data transfer object which holds
    //the data temporalily and the data can be reused by different components).

    //It has has @PostMapping Spring web annotation means its a POST request..
    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getAll());
    }
}
