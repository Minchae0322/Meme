package minchae.meme.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;

import java.util.List;


public class ThreadResponse {
    private Post post;
    private List<Comment> comments;
}
