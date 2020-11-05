package com.pwebk.SpringBootBlogApplication.service;

import com.pwebk.SpringBootBlogApplication.dto.SubredditDto;
import com.pwebk.SpringBootBlogApplication.model.Subreddit;
import com.pwebk.SpringBootBlogApplication.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SubredditService {

    //Injecting SubredditRepository depdedencyy into our class
    @Autowired
    private SubredditRepository subredditRepository;

    //As invoked from the conteroller, the main responsibility of this method is saving a subreddit
    //into a database table. Its returrn type is SubredditDto. We pass in the SubredditDto object
    //that is holding values after being mapped and added values to from the controller.

    //Tip: A Map is an object that maps keys to values.

    //Inside the method we reach out to save method and pass in a callback function (this function
    // extracts the Subreddit from the Dto object) and we store the value in a variable called
    //save of type Subreddit (our entity).

    //Nextw we reach out to subredditDto object and reach out to setId setter method (here we
    // set the id of that particular subreddit fro what was saved by invoking getId getter method).

    //We then return the subredditDto (having the new paramters) back to the controller,
    //The controller will return it with the HTTPs ststus e.g  API repsonse in our case.
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(mapSubredditDto(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    //As invoked from the conteroller, the main responsibility of this method is fetching all
    //subreddits from the database. Its returnType is a List of type SubredditDto Object.
    //Inside it we reach out to subredditRepositoty (interacts directly with the subreddit database
    // table) and invoke findAll method. We then reach out to Stream method

    //Introduced in Java 8, the Stream API is used to process collections of objects.
    // A stream is a sequence of objects that supports various methods which can be pipelined to
    // produce the desired result. After a stream, the immediate operations are: map, filter and sorted.

    //We reach out to map method. The map() function is a method in the Stream class that represents a
    // functional programming concept. In simple words, the map() is used to transform one object into
    // other by applying a function. That's why the Stream. map(Function mapper) takes a
    // function as an argument. In this map method, we invoke a callback function called mapToDto

    //The collect() method in Stream API collects all objects from a stream object and stored in the
    // type of collection. The user has to provide what type of collection the results can be stored.
    //There are different types and different operations can be present in the Collectors Enum,
    // but most of the time we can use Collectors.toList(), Collectors.toSet(), and Collectors.toMap().

    //Rememeber our return type is a List and is of type SubredditDto. This will be returned to
    //the controller as a List, The controller then sends it to the client together with the
    //HTTPstatus response.
    @Transactional
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit){
        return SubredditDto.builder().name(subreddit.getName())
                .id(subreddit.getId())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }
}
