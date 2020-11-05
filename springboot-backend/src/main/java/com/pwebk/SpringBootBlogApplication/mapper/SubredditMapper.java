package com.pwebk.SpringBootBlogApplication.mapper;

import com.pwebk.SpringBootBlogApplication.dto.SubredditDto;
import com.pwebk.SpringBootBlogApplication.model.Post;
import com.pwebk.SpringBootBlogApplication.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    //This class uses MapStruct library as seen in our importation. Read the documentation
    //in mapstruct.org

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
