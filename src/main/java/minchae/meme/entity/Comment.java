package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private Long commentId;

    @ManyToOne
    private Post post;

    @Lob
    @Column
    private String comment;
    private Long writerId;
    @Column
    private int recommendation;
    @Column
    private int bad;


}
