package minchae.meme.controller;

import lombok.extern.slf4j.Slf4j;
import minchae.meme.exception.CommentNotFound;
import minchae.meme.exception.IsExistedUser;
import minchae.meme.exception.PostNotFound;
import minchae.meme.exception.Unauthorized;
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

        //만약 @ResponseStatus 를 쓸 수 없는 상황이면
        //ResponseEntity<ErrorResponse> response = ResponseEntity.status(e.getStatusCode();)
        //                                                       .body(......);
        //을 사용하면 http status 를 알아서 골라서 내려준다.
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(Unauthorized.class)
    public ErrorResponse unauthorized(Unauthorized e) {
        return ErrorResponse.builder()
                .code("401")
                .message(e.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IsExistedUser.class)
    public ErrorResponse isExitedUser(IsExistedUser e) {
        return ErrorResponse.builder()
                .code("404")
                .message(e.getMessage())
                .build();
    }
}
