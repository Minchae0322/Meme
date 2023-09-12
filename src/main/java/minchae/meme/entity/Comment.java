package minchae.meme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import minchae.meme.request.CommentEdit;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Long commentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private Post post;

    @Lob
    @Column
    @NotBlank
    private String comment;
    @ManyToOne(cascade = CascadeType.MERGE)
    @NotNull
    private User user;

    @Embedded
    @NotNull
    private CommentFunction commentFunction;


    @Builder
    public Comment(Long commentId, Post post, String comment, User user, CommentFunction commentFunction) {
        this.commentId = commentId;
        this.post = post;
        this.comment = comment;
        this.user = user;
        this.commentFunction = commentFunction;
    }

    public void update(CommentEdit commentEdit) {
        this.comment = commentEdit.getComment();
    }
}
