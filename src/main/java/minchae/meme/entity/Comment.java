package minchae.meme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @JsonBackReference
    //@JoinColumn(name = "postId")
    private Post post;

    @Lob
    @Column
    @NotBlank
    private String comment;
    @ManyToOne()
    private User user;
    @ColumnDefault("0")
    private int recommendation;
    @ColumnDefault("0")
    private int bad;


    @Builder
    public Comment(Long commentId, Post post, String comment, User user, int recommendation, int bad) {
        this.commentId = commentId;
        this.post = post;
        this.comment = comment;
        this.user = user;
        this.recommendation = recommendation;
        this.bad = bad;
    }

    public void update(CommentEdit commentEdit) {
        this.comment = commentEdit.getComment();
    }
}
