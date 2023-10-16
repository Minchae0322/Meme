package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgFileName;

    private String storeFileName;

    public UploadFile(Long id, String orgFileName, String storeFileName) {
        this.id = id;
        this.orgFileName = orgFileName;
        this.storeFileName = storeFileName;
    }
}
