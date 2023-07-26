package minchae.meme.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NotFound;

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


}
