package minchae.meme;

import minchae.meme.entity.Post;
import minchae.meme.request.PostCreate;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemeApplicationTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Post_MemeServiceImpl postService;


}
