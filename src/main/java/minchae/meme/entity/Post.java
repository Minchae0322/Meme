package minchae.meme.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import minchae.meme.request.PostEdit;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "POST-TYPE")
@NoArgsConstructor
@SuperBuilder
@Transactional
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<Recommendation> recommendations = new ArrayList<>();

    @ColumnDefault("0")
    private int bad;

    @ColumnDefault("0")
    private int views;

    @Column
    private boolean isHot;

    @Column
    private final LocalDateTime createdTime = LocalDateTime.now();

    @ManyToOne()
    private User user;


    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();




    public void update(PostEdit postEdit) {
        this.title = postEdit.getTitle();
        this.content = postEdit.getContent();
        this.views++;
    }


}
