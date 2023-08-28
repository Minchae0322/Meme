package minchae.meme.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime createdTime = LocalDateTime.now();

    @ColumnDefault("0")
    private int views;

    @ManyToOne()
    @NotNull
    private User author;

    @Embedded
    @NotNull
    private PostFunction postFunction;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<UpDown> upDowns = new ArrayList<>();


    private String youtubeUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<UploadFile> uploadFiles = new ArrayList<>();



    public void update(PostEdit postEdit) {
        this.title = postEdit.getTitle();
        this.content = postEdit.getContent();
        this.views++;
        //todo 이미지 파일이랑 url 수정도 같이
    }

    public int getRecommendation() {
        int up = 0;
        for (UpDown upDown : upDowns) {
            if (upDown.getType().equals("UP")) {
                up++;
            }
        }
        return up;
    }

    public int getBad() {
        int bad = 0;
        for (UpDown upDown : upDowns) {
            if (upDown.getType().equals("DOWN")) {
                bad++;
            }
        }
        return bad;
    }


}
