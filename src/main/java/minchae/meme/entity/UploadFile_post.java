package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@DiscriminatorValue("post")
@RequiredArgsConstructor
public class UploadFile_post extends UploadFile{

    @ManyToOne(cascade = CascadeType.MERGE)
    private Post post;
    @Builder
    public UploadFile_post(Long id, String orgFileName, String storeFileName, Post post) {
        super(id, orgFileName, storeFileName);
        this.post = post;
    }
}
