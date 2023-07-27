package minchae.meme.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "POST-TYPE")
@NoArgsConstructor
@SuperBuilder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column
    @Lob
    @NotBlank
    private String title;

    @Column
    @Lob
    @NotBlank
    private String content;

    @ColumnDefault("0")
    private int recommendation;

    @ColumnDefault("0")
    private int bad;

    @ColumnDefault("0")
    private int views;

    @Column
    private Long writerId;

    public void plusRecommendation() {
        this.recommendation++;
    }

    public void update(PostEdit postEdit) {
        this.title = postEdit.getTitle();
        this.content = postEdit.getContent();
        this.views++;
    }


}
