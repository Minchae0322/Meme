package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.sql.Update;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(columnDefinition = "DATA-TYPE")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Post post;

    @ManyToOne
    private User user;

    @Builder
    public Recommendation(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
