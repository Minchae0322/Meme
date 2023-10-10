package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgFileName;

    private String storeFileName;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Post post;


    @Builder
    public UploadFile(Long id, String orgFileName, String storeFileName, Post post) {
        this.id = id;
        this.orgFileName = orgFileName;
        this.storeFileName = storeFileName;
        this.post = post;
    }
}
