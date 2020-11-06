package com.pwebk.SpringBootBlogApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsDto{
    private Long id;
    private Long postId;
    private String text;
    private String userName;
}
