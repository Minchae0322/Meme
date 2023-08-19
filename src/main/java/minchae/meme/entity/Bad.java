package minchae.meme.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Bad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Post post;

    @ManyToOne
    private User user;

    @ColumnDefault("UP")
    private String type;

    @Builder
    public Bad(Post post, User user, String type) {
        this.post = post;
        this.user = user;
        this.type = type;
    }
}
