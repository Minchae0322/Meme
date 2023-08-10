package minchae.meme.controller;

import lombok.extern.slf4j.Slf4j;
import minchae.meme.exception.CommentNotFound;
import minchae.meme.exception.PostNotFound;
import minchae.meme.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse postNotFound(PostNotFound e) {
        return ErrorResponse.builder()
                .code("404")
                .message(e.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentNotFound.class)
    public ErrorResponse commentNotFound(CommentNotFound e) {
        return ErrorResponse.builder()
                .code("404")
                .message(e.getMessage())
                .build();
    }
}
