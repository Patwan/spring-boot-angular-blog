package com.pwebk.SpringBootBlogApplication.exceptions;

public class SpringRedditException extends RuntimeException {

    //When building the backend, exceptions are pretty common in our code, when the exceptions occur
    //we dont want to expose the technical information to the user. We should always present this information
    //in an understandable format
    //We do that by creating custom exception classes and passing our own messages.

    public SpringRedditException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringRedditException(String exMessage) {
        super(exMessage);
    }
}
