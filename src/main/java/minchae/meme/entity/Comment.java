package minchae.meme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import minchae.meme.request.CommentEdit;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

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

    @Column
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime createdTime = LocalDateTime.now();
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
