package minchae.meme.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Comment;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentVo {
    private List<CommentCreate> commentCreateList;


    public CommentVo(List<CommentCreate> commentCreateList) {
        this.commentCreateList = commentCreateList;
    }
}
