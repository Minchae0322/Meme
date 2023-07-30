package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.request.CommentEdit;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Long commentId;

    @ManyToOne
    //@JoinColumn(name = "postId")
    private Post post;

    @Lob
    @Column
    private String comment;
    private Long writerId;
    @ColumnDefault("0")
    private int recommendation;
    @ColumnDefault("0")
    private int bad;

    @Builder
    public Comment(Long commentId, Post post, String comment, Long writerId, int recommendation, int bad) {
        this.commentId = commentId;
        this.post = post;
        this.comment = comment;
        this.writerId = writerId;
        this.recommendation = recommendation;
        this.bad = bad;
    }

    public void update(CommentEdit commentEdit) {
        this.comment = commentEdit.getComment();
    }
}
