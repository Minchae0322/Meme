package minchae.meme;

import minchae.meme.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemeApplicationTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostServiceImpl postService;


}
