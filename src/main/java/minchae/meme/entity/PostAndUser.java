package minchae.meme.entity;

import jakarta.persistence.ManyToOne;

public class PostAndUser {
    @ManyToOne
    private Post post;

    private Long userId;

}
