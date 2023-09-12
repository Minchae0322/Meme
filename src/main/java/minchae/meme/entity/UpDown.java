package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class UpDown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Post post;

    @ManyToOne
    private User user;

    @Column
    private String type;

    @Builder
    public UpDown(Post post, User user, String type) {
        this.post = post;
        this.user = user;
        this.type = type;
    }
}
