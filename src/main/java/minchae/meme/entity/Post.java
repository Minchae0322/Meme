package minchae.meme.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
import minchae.meme.entity.enumClass.PostType;
import minchae.meme.request.PostEdit;
import org.hibernate.Session;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 100)
    @Lob
    @NotBlank
    private String title;

    @Lob
    @NotBlank
    @Column(length = 1000)
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


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private final List<UpDown> upDowns = new ArrayList<>();


    private String youtubeUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<UploadFile> uploadFiles = new ArrayList<>();

    private PostType postType;


    public void update(PostEdit postEdit) {
        this.title = postEdit.getTitle();
        this.content = postEdit.getContent();
        this.views++;

        //todo 이미지 파일이랑 url 수정도 같이
    }

    @Transactional
    public void upView() {
        this.views++;
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

    @Transactional
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
