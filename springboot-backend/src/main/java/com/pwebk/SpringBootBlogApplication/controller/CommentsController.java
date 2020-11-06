package main.java.com.pwebk.SpringBootBlogApplication.controller;

import com.pwebk.SpringBootBlogApplication.dto.CommentsDto;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

    @PostMapping
    public void createComment(@RequestBody CommentsDto commentsDto){

    }

}
